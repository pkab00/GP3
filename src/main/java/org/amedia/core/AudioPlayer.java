package org.amedia.core;

import javafx.application.Platform;
import javafx.scene.media.*;
import javafx.embed.swing.*;
import javafx.util.Duration;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.amedia.gui.PlayerGUI;

/**
 * Логическое "ядро" приложения. Реализует интерфейсы {@link IPlayer} и {@link IMediaObservable}.
 * <ol>
 *     <li>Управляет воспроизведением аудиофайлов посредством инкапсуляции {@link MediaPlayer}</li>
 *     <li>Хранит информацию об очереди воспроизведения</li>
 *     <li>Изменяет поведение при достижении конца очереди (паттерн Стратегия)</li>
 *     <li>Регистрирует, удаляет и уведомляет слушателей об изменении состояния (паттерн Наблюдатель)</li>
 * </ol>
 * <pre>{@code
 *     ArrayList<Playable> songs = new ArrayList<>;
 *
 *     Playable song1 = new Playable("path/to/song1");
 *     Playable song2 = new Playable("path/to/song2");
 *     ...
 *     Playable songN = new Playable("path/to/songN");
 *
 *     songs.add(song1);
 *     songs.add(song2);
 *     ...
 *     songs.add(songN);
 *
 *     AudioPlayer player = new AudioPlayer();
 *     player.setPlaylist(songs);
 *     player.play();
 * }</pre>
 * @see #mediaPlayer
 * @see PlayQueue
 * @see PlayMode
 * @see #addPCL(PropertyChangeListener)
 * @see #removePCL(PropertyChangeListener)
 */
public class AudioPlayer implements IPlayer, IMediaObservable {
    private static boolean jfxInitialized = false;
    private PlayQueue playQueue;
    private MediaPlayer mediaPlayer;
    private IPlayable current;
    private Timer timer;

    // OBSERVER pattern in action
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    // STRATEGY pattern in action
    private final PlayModeIterator playModeIterator;
    private PlayMode playMode;

    /**
     * Базовая настройка плеера перед стартом:
     * <ol>
     *     <li>Инициализация окружения JavaFX</li>
     *     <li>Подключение GUI в качестве слушателя</li>
     *     <li>Создание очереди режимов воспроизведения</li>
     * </ol>
     * @param gui графический интерфейс
     * @see #initializeJFX()
     * @see #addPCL(PropertyChangeListener)
     */
    public AudioPlayer(PlayerGUI gui){
        if(!jfxInitialized){
            initializeJFX(); // Инициализируем окружение JavaFX при первом вызове
        }
        addPCL(gui); // Подключаем GUI в качестве слушателя
        playModeIterator = new PlayModeIterator(
                List.of(new DefaultMode(this),
                        new RepeatAllMode(this),
                        new RepeatOneMode(this)));
        playMode = playModeIterator.next(); // Начинаем с режима по умолчанию
        setPlaylist(new ArrayList<>()); // По умолчанию - пустой плейлист
        System.out.println("AudioPlayer: Setup finished!");
    }

    /**
     * Статический метод для инициализации окружения JavaFX.
     */
    private static void initializeJFX(){
        new JFXPanel();
        Platform.setImplicitExit(false);
        jfxInitialized = true;
        System.out.println("AudioPlayer: JFX initialized!");
    }

    /**
     * Установка текущего плейлиста и выбор трека для начала воспроизведения.
     * Преобразует список песен в объект PlayQueue.
     * @param playlist объект {@link ArrayList<IPlayable>}, представляющий плейлист
     * @see PlayQueue
     */
    @Override
    public void setPlaylist(ArrayList<IPlayable> playlist){
        this.playQueue = new PlayQueue(playlist);
        this.current = playQueue.next(); // Устанавливаем первый трек в качестве текущего
        notifyPlaylistChange(playlist); // Уведомляем слушателей о смене плейлиста
        System.out.println("AudioPlayer: Playlist set!");
        play();
    }

    /**
     * Вспомогательный метод для замены невалидных символов в пути к файлу.
     * @return отформатированный путь
     */
    private String getCorrectURI(){
        return new File(current.getFilePath()).toURI().toString()
                .replace("+", "%20") // Заменяем + на %20 для пробелов
                .replace("%28", "(") // Заменяем кодированные символы обратно
                .replace("%29", ")");
    }

    /**
     * Инициализирует и создаёт таймер, раз в 0.5 секунды,
     * сообщающий о необходимости, обновить компоненты UI.
     */
    public void startTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                    notifyProgressChange();
            }
        };
        timer.schedule(task, 0, 500); // Раз в 0.5 секунды
    }

    /**
     * Останавливает и обнуляет таймер.
     */
    public void stopTimer(){
        timer.cancel();
        timer = null;
    }

    /**
     * Запуск проигрывания новой песни.
     * <ol>
     *     <li>Останавливает воспроизведение, если оно велось ранее</li>
     *     <li>Дожидается загрузки нового аудио и воспроизводит его</li>
     *     <li>Уведомляет слушатели об изменении состояния</li>
     * </ol>
     */
    @Override
    public void play() {
        if(current == null) return;
        if(mediaPlayer != null) stop();
        if(timer != null) stopTimer();

        Platform.runLater(() -> {this.mediaPlayer = new MediaPlayer(new Media(getCorrectURI()));
        this.mediaPlayer.setOnEndOfMedia(playMode);
        this.mediaPlayer.setOnReady(() -> {
            this.mediaPlayer.play();
            System.out.println("AudioPlayer: Playing: " + current.getFilePath());
            notifySongChange(null);
            startTimer();
        });
            this.mediaPlayer.setOnPlaying(this::notifyPlayChange);
        });
    }

    /**
     * Ставит трек на паузу и уведомляет слушателей.
     */
    @Override
    public void pause() {
        Platform.runLater(() -> {
            mediaPlayer.pause();
            mediaPlayer.setOnPaused(this::notifyPlayChange);
        });
    }

    /**
     * Возобновляет воспроизведение трека и уведомляет слушателей.
     */
    @Override
    public void resume() {
        Platform.runLater(() -> {
            mediaPlayer.play();
            mediaPlayer.setOnPlaying(this::notifyPlayChange);
        });
    }

    /**
     * Начинает воспроизведение заново.
     */
    @Override
    public void rewind() {
        Platform.runLater(() -> mediaPlayer.seek(new Duration(0)));
        notifyProgressChange();
    }

    /**
     * Останавливает воспроизведение и освобождает ресурсы плеера.
     */
    @Override
    public void stop() {
        if(mediaPlayer != null){
            Platform.runLater(() -> {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                notifyPlayChange();
            });
        }
    }

    /**
     * Переход к следующему треку.
     */
    @Override
    public void toNext() {
        if(playQueue.hasNext()){
            IPlayable oldCurrent = current;
            current = playQueue.next();
            notifySongChange(oldCurrent);
        }
    }

    /**
     * Переход к предыдущему треку.
     */
    @Override
    public void toPrevious() {
        if(playQueue.hasPrevious()){
            IPlayable oldCurrent = current;
            current = playQueue.previous();
            notifySongChange(oldCurrent);
        }
    }

    /**
     * Изменение громкости.
     * @param volume новое значение громкости
     */
    @Override
    public void setVolume(double volume) {
        Platform.runLater(() -> mediaPlayer.setVolume(volume));
    }

    /**
     * Изменяет текущую позицию воспроизведение на указанное количество секунд.
     * Работает в обоих направлениях.
     * @param secDelta кол-во секунд сдвига
     */
    @Override
    public void setPosition(double secDelta) {
        Platform.runLater(() -> {
            Duration duration = mediaPlayer.getMedia().getDuration(); // получаем длину
            Duration currentTime = mediaPlayer.getCurrentTime(); // получаем текущее время

            double newTime = currentTime.toMillis() + (secDelta * 1000); // вычисляем новое (желаемое) время
            newTime = Math.max(0, Math.min(newTime, duration.toMillis())); // устанавливаем допустимые границы

            mediaPlayer.seek(new Duration(newTime)); // устанавливаем новое время
            notifyProgressChange();
        });
    }

    /**
     * Устанавливает новый режим воспроизведения.
     * Если в качестве параметра передано {@code null}, устанавливается следующий режим в очереди.
     * @param playMode новый режим воспроизведения
     */
    @Override
    public void setPlayMode(PlayMode playMode) {
        if(playMode != null) this.playMode = playMode;
        else this.playMode = playModeIterator.next();
        mediaPlayer.setOnEndOfMedia(this.playMode);
        notifyPlayModeChange(this.playMode);
    }

    /**
     * @return текущая громкость
     */
    @Override
    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    /**
     * @return очередь воспроизведения
     */
    @Override
    public PlayQueue getQueue() {
        return playQueue;
    }

    /**
     * @return текущая позиция
     */
    @Override
    public double getPosition() {
        return mediaPlayer.getCurrentTime().toSeconds();
    }

    /**
     * @return текущий воспроизводимый трек
     */
    @Override
    public IPlayable getPlaying() {
        return current;
    }

    /**
     * @return текущий режим воспроизведения
     */
    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    /**
     * @return текущее состояние воспроизведения
     * {@code true} - если музыка воспроизводится, {@code false} - в противном случае.
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    /**
     * Уведомляет слушателей об окончании/остановке/возобновлении воспроизведения трека.
     * <p>
     * {@code oldValue} - старое значение isPlaying
     * {@code newValue} - новое значение isPlaying
     */
    @Override
    public void notifyPlayChange(){
        support.firePropertyChange(MediaEvents.PLAY_CHANGE, !isPlaying(), isPlaying());
    }

    /**
     * Уведомляет слушателей о смене текущего воспроизводимого трека.
     * <p>
     * {@code oldValue} - старый трек
     * {@code newValue} - новый трек
     */
    @Override
    public void notifySongChange(IPlayable oldCurrent){
        support.firePropertyChange(MediaEvents.SONG_CHANGE, oldCurrent, current);
    }

    /**
     * Ежесекундно уведомляет слушателей о текущем времени воспроизведения.
     * Если аудио ещё загружается - вызов сбрасывается.
     * <p>
     * {@code oldValue} - текущая позиция
     * {@code newValue} - оставшееся время (длительность - позиция)
     */
    @Override
    public void notifyProgressChange(){
        if (mediaPlayer == null || mediaPlayer.getMedia() == null) return;

        int duration = (int)mediaPlayer.getMedia().getDuration().toSeconds();
        int current = (int)mediaPlayer.getCurrentTime().toSeconds();
        support.firePropertyChange(MediaEvents.PROGRESS_CHANGE, current, duration - current);
    }

    /**
     * Уведомляет слушателей о смене режима воспроизведения.
     * @param playMode новый режим воспроизведения
     */
    @Override
    public void notifyPlayModeChange(PlayMode playMode){
        support.firePropertyChange(MediaEvents.PLAY_MODE_CHANGE, null, playMode);
    }

/**
 * Уведомляет слушателей об изменении плейлиста.
 * Передаёт новое количество элементов в плейлисте.
 * @param playlist текущий плейлист
 */
    @Override
    public void notifyPlaylistChange(ArrayList<IPlayable> playlist){
        support.firePropertyChange(MediaEvents.PLAYLIST_CHANGE, null, playlist.size());
    }

    /**
     * Регистрирует новый слушатель состояния.
     * @param pcl объект {@link PropertyChangeListener}
     */
    @Override
    public void addPCL(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    /**
     * Удаляет слушатель состояния.
     * @param pcl объект {@link PropertyChangeListener}
     */
    @Override
    public void removePCL(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @Override
    public void release() {
        stop();
    }
}

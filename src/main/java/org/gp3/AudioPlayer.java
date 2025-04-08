package org.gp3;

import javafx.application.Platform;
import javafx.scene.media.*;
import javafx.embed.swing.*;
import javafx.util.Duration;
import org.gp3.gui.PlayerGUI;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

/**
 * Логическое "ядро" приложения. Реализует интерфейс {@link IPlayer}.
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
 * @see NextSongBehaviour
 * @see #addPCL(PropertyChangeListener)
 * @see #removePCL(PropertyChangeListener)
 */
public class AudioPlayer implements IPlayer {
    private static boolean jfxInitialized = false;
    private PlayQueue playQueue;
    private MediaPlayer mediaPlayer;
    private IPlayable current;

    // OBSERVER pattern in action
    private PropertyChangeSupport support = new PropertyChangeSupport(this);
    // STRATEGY pattern in action
    private NextSongBehaviour nextSongBehaviour = new PlayUntillTheEndBehaviour(this);

    /**
     * Базовая настройка плеера перед стартом:
     * <ol>
     *     <li>Инициализация окружения JavaFX</li>
     *     <li>Подключение GUI в качестве слушателя</li>
     * </ol>
     * @param gui графический интерфейс
     * @see #initializeJFX()
     * @see #addPCL(PropertyChangeListener)
     */
    public AudioPlayer(PlayerGUI gui){
        if(!jfxInitialized){
            initializeJFX();
        }
        addPCL(gui);
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
        if(playlist.isEmpty()){
            return;
        }
        this.playQueue = new PlayQueue(playlist);
        this.current = playQueue.next();
        System.out.println("AudioPlayer: Playlist set!");
        System.out.println("Current song: " + current.getFilePath());
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

        Platform.runLater(() -> {this.mediaPlayer = new MediaPlayer(new Media(getCorrectURI()));
        this.mediaPlayer.setOnEndOfMedia(nextSongBehaviour);
        this.mediaPlayer.setOnReady(() -> {
            this.mediaPlayer.play();
            System.out.println("AudioPlayer: Playing: " + current.getFilePath());
            support.firePropertyChange("playing", false, true);
        });});
    }

    /**
     * Ставит трек на паузу и уведомляет слушателей.
     */
    @Override
    public void pause() {
        Platform.runLater(mediaPlayer::pause);
        support.firePropertyChange("playing", isPlaying(), false);
    }

    /**
     * Возобновляет воспроизведение трека и уведомляет слушателей.
     */
    @Override
    public void resume() {
        Platform.runLater(mediaPlayer::play);
        support.firePropertyChange("playing", isPlaying(), true);
    }

    /**
     * Начинает воспроизведение заново.
     */
    @Override
    public void rewind() {
        Platform.runLater(() -> mediaPlayer.seek(new Duration(0)));
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
                support.firePropertyChange("playing", isPlaying(), false);
            });
        }
    }

    /**
     * Переход к следующему треку.
     */
    @Override
    public void toNext() {
        if(playQueue.hasNext()){
            current = playQueue.next();
        }
    }

    /**
     * Переход к предыдущему треку.
     */
    @Override
    public void toPrevious() {
        if(playQueue.hasPrevious()){
            current = playQueue.previous();
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
        });
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
     * @return текущее состояние воспроизведения
     * {@code true} - если музыка воспроизводится, {@code false} - в противном случае.
     */
    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
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
}

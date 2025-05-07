package org.gp3.controller;

import org.gp3.core.AudioPlayer;
import org.gp3.core.IPlayable;
import org.gp3.core.IPlayer;
import org.gp3.core.SongLoader;
import org.gp3.gui.MusicFileChooser;
import org.gp3.gui.PlayQueueGUI;
import org.gp3.gui.ProgressGUI;
import org.gp3.utils.Callback;

import java.io.File;
import java.util.ArrayList;

/**
 * Промежуточное звено между плеером и его интерфейсом.
 * Содержит объект {@link IPlayer}, с его помощью обрабатывает поведение при нажатии кнопок.
 */
public class PlayerController implements IPlayerController {
    private final IPlayer audioPlayer;

    public PlayerController(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Обработка запуска воспроизведения.
     */
    @Override
    public void handlePlay() {
        audioPlayer.play();
    }
    /**
     * Обработка паузы.
     */
    @Override
    public void handlePause() {
        audioPlayer.pause();
    }

    /**
     * Обработка возобновления воспроизведения.
     */
    @Override
    public void handleResume() {
        audioPlayer.resume();
    }

    /**
     * Обработка запуска воспроизведения с начала.
     */
    @Override
    public void handleRewind() {
        audioPlayer.rewind();
    }

    /**
     * Обработка перемещения позиции на 10 секунд вперёд.
     */
    @Override
    public void handleJumpForward() {
        audioPlayer.setPosition(10);
    }

    /**
     * Обработка перемещения позиции на 10 секунд назад.
     */
    @Override
    public void handleJumpBackward() {
        audioPlayer.setPosition(-10);
    }

    /**
     * Обработка перемещения к следующему треку.
     */
    @Override
    public void handleNext() {
        audioPlayer.getPlayMode().nextSong();
    }

    /**
     * Обработка перемещения к предыдущему треку.
     */
    @Override
    public void handlePrevious() {
        audioPlayer.toPrevious();
        audioPlayer.play();
    }

    /**
     * Обработка взаимодействия со слайдером.
     * @param newPosition позиция после отпускания слайдера
     * @param oldPosition позиция до отпускания слайдера
     */
    @Override
    public void handleSongSlider(int newPosition, int oldPosition) {
        audioPlayer.setPosition(newPosition - oldPosition);
    }

    /**
     * Обработка смены режима воспроизведения.
     * Меняет текущий {@code PlayMode} на следующий в списке.
     */
    @Override
    public void handlePlayMode() {
        audioPlayer.setPlayMode(null);
    }

    /**
     * Обработка выбора плейлиста с помощью кастомного класса.
     * @see MusicFileChooser
     */
    @Override
    public void handleFilesSelection(File[] selectedFiles) {
        ProgressGUI progressGUI = new ProgressGUI(); // окно с прогрессом загрузки
        // используем callback для обработки стадий загрузки
        SongLoader loader = new SongLoader(selectedFiles, new Callback<>() {
            @Override
            public void onStarted() {
                progressGUI.setMaximum(selectedFiles.length); // на старте устанавливаем максимальное значение
            }
            @Override
            public void onProgress(Integer progress) {
                progressGUI.updateProgress(progress); // обновляем прогресс
            }
            @Override
            public void onFinished(ArrayList<IPlayable> parameter) {
                audioPlayer.setPlaylist(parameter); // установка плейлиста
                progressGUI.done(); // закрываем окно
            }
        });
        loader.execute(); // запуск процесса загрузки
    }

    /**
     * Обработка открытия окна просмотра плейлиста.
     * @see org.gp3.gui.PlayQueueGUI PlaylistGUI
     */
    @Override
    public void handlePlayQueueView() {
        PlayQueueGUI gui = new PlayQueueGUI();
        PlayQueueController controller = new PlayQueueController(audioPlayer, gui);
        controller.handlePlaylistChange();
        gui.setController(controller);
        gui.showScreen();
    }
}

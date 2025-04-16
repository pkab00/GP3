package org.gp3;

import org.gp3.gui.MusicFileChooser;
import org.gp3.gui.PlaylistGUI;

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
    public void handleFilesSelection(ArrayList<IPlayable> selectedSongs) {
        audioPlayer.setPlaylist(selectedSongs);
    }

    /**
     * Обработка открытия окна просмотра плейлиста.
     * @see org.gp3.gui.PlaylistGUI PlaylistGUI
     */
    @Override
    public void handlePlaylistView() {
        PlaylistGUI gui = new PlaylistGUI();
        PlaylistController controller = new PlaylistController(audioPlayer, gui);
        controller.handlePlaylistChange();
        gui.setController(controller);
        gui.showScreen();
    }
}

package org.gp3;

/**
 * Промежуточное звено между плеером и его интерфейсом.
 * Содержит объект {@link IPlayer}, с его помощью обрабатывает поведение при нажатии кнопок.
 */
public class Controller implements IController {
    private final IPlayer audioPlayer;

    public Controller(AudioPlayer audioPlayer) {
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
        audioPlayer.toNext();
        audioPlayer.play();
    }

    /**
     * Обработка перемещения к предыдущему треку.
     */
    @Override
    public void handlePrevious() {
        audioPlayer.toPrevious();
        audioPlayer.play();
    }
}

package org.gp3;

public class Controller implements ControllerInterface{
    private final PlayerInterface audioPlayer;

    public Controller(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void handlePlay() {
        audioPlayer.play();
    }

    @Override
    public void handlePause() {
        audioPlayer.pause();
    }

    @Override
    public void handleResume() {
        audioPlayer.resume();
    }

    @Override
    public void handleRewind() {
        audioPlayer.rewind();
    }

    @Override
    public void handleJumpForward() {
        audioPlayer.setPosition(10);
    }

    @Override
    public void handleJumpBackward() {
        audioPlayer.setPosition(-10);
    }

    @Override
    public void handleNext() {
        audioPlayer.toNext();
        audioPlayer.play();
    }

    @Override
    public void handlePrevious() {
        audioPlayer.toPrevious();
        audioPlayer.play();
    }
}

package org.gp3;

public interface IController {
    void handlePlay();
    void handlePause();
    void handleResume();
    void handleRewind();
    void handleJumpForward();
    void handleJumpBackward();
    void handleNext();
    void handlePrevious();
    void handleSongSlider(int newPosition, int oldPosition);
    void handlePlayMode();
    void handleFilesSelection();
}

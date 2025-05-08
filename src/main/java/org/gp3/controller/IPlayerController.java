package org.gp3.controller;

import java.io.File;

public interface IPlayerController {
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
    void handleFilesSelection(File[] selectedFiles);
    void handlePlayQueueView();
    void handleSaveAsPlaylist();
    void handleOpenPlaylistGUI();
}

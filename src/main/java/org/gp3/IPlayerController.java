package org.gp3;

import java.io.File;
import java.util.ArrayList;

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
    void handlePlaylistView();
}

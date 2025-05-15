package org.gp3.core;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface IMediaObservable {
    void addPCL(PropertyChangeListener pcl);
    void removePCL(PropertyChangeListener pcl);

    void notifyPlayChange();
    void notifySongChange(IPlayable oldCurrent);
    void notifyProgressChange();
    void notifyPlaylistChange(ArrayList<IPlayable> playlist);
    void notifyPlayModeChange(PlayMode playMode);

    public interface MediaEvents{
        String PLAY_CHANGE = "playChange";
        String SONG_CHANGE = "songChange";
        String PROGRESS_CHANGE = "progressChange";
        String PLAYLIST_CHANGE = "playlistChange";
        String PLAY_MODE_CHANGE = "playModeChange";
    }
}
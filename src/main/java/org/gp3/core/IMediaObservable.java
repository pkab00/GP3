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
}
package org.gp3.logic;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;

public interface IPlayer {
    void play();
    void pause();
    void resume();
    void rewind();
    void stop();
    void toNext();
    void toPrevious();

    void setPlaylist(ArrayList<IPlayable> playlist);
    void setVolume(double volume);
    void setPosition(double position);
    void setPlayMode(PlayMode playMode);

    double getVolume();
    PlayQueue getQueue();
    double getPosition();
    IPlayable getPlaying();
    PlayMode getPlayMode();
    boolean isPlaying();

    void addPCL(PropertyChangeListener pcl);
    void removePCL(PropertyChangeListener pcl);
}

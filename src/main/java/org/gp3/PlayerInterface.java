package org.gp3;

import java.beans.PropertyChangeListener;

public interface PlayerInterface {
    void play();
    void pause();
    void resume();
    void rewind();
    void stop();
    void toNext();
    void toPrevious();

    void setVolume(double volume);

    double getVolume();
    TwoWayIterator<?> getQueue();
    Playable getPlaying();
    boolean isPlaying();

    void addPCL(PropertyChangeListener pcl);
    void removePCL(PropertyChangeListener pcl);
}

package org.amedia.core;

import java.util.ArrayList;

public interface IPlayer {
    void play();
    void pause();
    void resume();
    void rewind();
    void stop();
    void toNext();
    void toPrevious();
    void release();

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
}

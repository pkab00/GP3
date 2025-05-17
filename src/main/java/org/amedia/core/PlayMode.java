package org.amedia.core;

public interface PlayMode extends Runnable {
    void nextSong();
    default void run() {
        nextSong();
    }
}

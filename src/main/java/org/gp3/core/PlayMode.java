package org.gp3.core;

public interface PlayMode extends Runnable {
    void nextSong();
    default void run() {
        nextSong();
    }
}

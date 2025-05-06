package org.gp3.logic;

public interface PlayMode extends Runnable {
    void nextSong();
    default void run() {
        nextSong();
    }
}

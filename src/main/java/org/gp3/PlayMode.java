package org.gp3;

public interface PlayMode extends Runnable {
    void nextSong();
    default void run() {
        nextSong();
    }
}

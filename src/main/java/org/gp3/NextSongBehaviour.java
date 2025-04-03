package org.gp3;

public interface NextSongBehaviour extends Runnable {
    void nextSong();
    default void run() {
        nextSong();
    }
}

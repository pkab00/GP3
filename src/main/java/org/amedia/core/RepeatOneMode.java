package org.amedia.core;

public class RepeatOneMode implements PlayMode {
    private final IPlayer audioPlayer;

    public RepeatOneMode(IPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        audioPlayer.play();
    }
}

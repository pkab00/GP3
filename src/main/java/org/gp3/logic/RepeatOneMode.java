package org.gp3.logic;

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

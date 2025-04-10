package org.gp3;

public class RepeatOneMode implements PlayMode {
    private final AudioPlayer audioPlayer;

    public RepeatOneMode(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        audioPlayer.play();
    }
}

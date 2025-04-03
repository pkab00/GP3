package org.gp3;

public class RepeatBehaviour implements NextSongBehaviour{
    private final AudioPlayer audioPlayer;

    public RepeatBehaviour(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        audioPlayer.play();
    }
}

package org.gp3;

public class RepeatCurrentPlaylistBehaviour implements NextSongBehaviour{
    private final AudioPlayer audioPlayer;

    public RepeatCurrentPlaylistBehaviour(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        System.out.println("AudioPlayer: Repeating playlist...");
        while(audioPlayer.getQueue().hasPrevious()){
            audioPlayer.toPrevious();
        }
        audioPlayer.play();
    }
}

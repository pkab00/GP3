package org.amedia.core;

public class DefaultMode implements PlayMode {
    private final IPlayer audioPlayer;

    public DefaultMode(IPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        if(audioPlayer.getQueue().hasNext()){
            System.out.println("AudioPlayer: Moving to next song...");
            audioPlayer.toNext();
            audioPlayer.play();
        }
        else {
            System.out.println("AudioPlayer: No more songs!");
        }
    }
}

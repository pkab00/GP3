package org.gp3.core;

public class RepeatAllMode implements PlayMode {
    private final IPlayer audioPlayer;

    public RepeatAllMode(IPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    @Override
    public void nextSong() {
        if(!audioPlayer.getQueue().hasNext()){
            System.out.println("AudioPlayer: Repeating playlist...");
            while(audioPlayer.getQueue().hasPrevious()){
                audioPlayer.toPrevious();
            }
            audioPlayer.play();
        } else {
            new DefaultMode(audioPlayer).nextSong(); // используем поведение DefaultMode
        }
    }
}

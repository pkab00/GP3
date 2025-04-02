package org.gp3;

import javafx.scene.media.*;
import javafx.embed.swing.*;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayer implements PlayerInterface{
    static {
        new JFXPanel();
    }

    private final PlayQueue playQueue;
    private MediaPlayer mediaPlayer;
    private Playable current;

    private boolean looping;

    public AudioPlayer(ArrayList<Playable> songs){
        this.playQueue = new PlayQueue(songs);
        if(playQueue.hasNext()){
            current = playQueue.next();
        }
    }

    private String getCorrectURI(){
        return new File(current.getFilePath()).toURI().toString()
                .replace("+", "%20") // Заменяем + на %20 для пробелов
                .replace("%28", "(") // Заменяем кодированные символы обратно
                .replace("%29", ")");
    }

    @Override
    public void play() {
        this.mediaPlayer = new MediaPlayer(new Media(getCorrectURI()));
        this.mediaPlayer.play();
        System.out.println("Playing: " + current.getFilePath());
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public void resume() {
        mediaPlayer.play();
    }

    @Override
    public void rewind() {
        mediaPlayer.seek(new Duration(0));
    }

    @Override
    public void stop() {
        mediaPlayer.stop();
    }

    @Override
    public void toNext() {
        if(playQueue.hasNext()){
            current = playQueue.next();
        }
    }

    @Override
    public void toPrevious() {
        if(playQueue.hasPrevious()){
            current = playQueue.previous();
        }
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.setVolume(volume);
    }

    @Override
    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    @Override
    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    @Override
    public boolean isLooping() {
        return looping;
    }
}

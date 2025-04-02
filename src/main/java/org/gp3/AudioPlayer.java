package org.gp3;

import javafx.application.Platform;
import javafx.scene.media.*;
import javafx.embed.swing.*;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class AudioPlayer implements PlayerInterface{
    static {
        new JFXPanel();
    }

    private PlayQueue playQueue;
    private MediaPlayer mediaPlayer;
    private Playable current;

    // TODO: not working
    private final Runnable defaultNextSongBehaviour = () -> {
        stop();
        System.out.println("Moving to next song...");
        toNext();
        play();
    };
    private final Runnable repeatSongBehaviour = () -> {
        stop();
        play();
    };
    private Runnable nextSongBehaviour = defaultNextSongBehaviour;

    public AudioPlayer(ArrayList<Playable> songs){
        if(songs.isEmpty()){
            return;
        }
        this.playQueue = new PlayQueue(songs);
        this.current = playQueue.next();
    }

    private String getCorrectURI(){
        return new File(current.getFilePath()).toURI().toString()
                .replace("+", "%20") // Заменяем + на %20 для пробелов
                .replace("%28", "(") // Заменяем кодированные символы обратно
                .replace("%29", ")");
    }

    @Override
    public void play() {
        if(mediaPlayer != null){
            stop();
        }
        Platform.runLater(() -> {this.mediaPlayer = new MediaPlayer(new Media(getCorrectURI()));
        this.mediaPlayer.setOnEndOfMedia(nextSongBehaviour);
        this.mediaPlayer.setOnReady(() -> {
            this.mediaPlayer.play();
            System.out.println("Playing: " + current.getFilePath());
        });});
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
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
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
    public double getVolume() {
        return mediaPlayer.getVolume();
    }
}

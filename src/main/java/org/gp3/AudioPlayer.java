package org.gp3;

import javafx.application.Platform;
import javafx.scene.media.*;
import javafx.embed.swing.*;
import javafx.util.Duration;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.util.ArrayList;

public class AudioPlayer implements PlayerInterface{
    private static boolean jfxInitialized = false;
    private PlayQueue playQueue;
    private MediaPlayer mediaPlayer;
    private Playable current;

    private PropertyChangeSupport support = new PropertyChangeSupport(this);

    // STRATEGY pattern in action
    private NextSongBehaviour nextSongBehaviour = new PlayUntillTheEndBehaviour(this);

    public AudioPlayer(ArrayList<Playable> songs){
        if(!jfxInitialized){
            initializeJFX();
        }
        if(songs.isEmpty()){
            return;
        }
        this.playQueue = new PlayQueue(songs);
        this.current = playQueue.next();
        System.out.println("AudioPlayer: Current song: " + current.getFilePath());
    }

    private static void initializeJFX(){
        new JFXPanel();
        Platform.setImplicitExit(false);
        jfxInitialized = true;
        System.out.println("AudioPlayer: JFX initialized!");
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
            System.out.println("AudioPlayer: Playing: " + current.getFilePath());
            support.firePropertyChange("playing", false, true);
        });});
    }

    @Override
    public void pause() {
        Platform.runLater(mediaPlayer::pause);
        support.firePropertyChange("playing", isPlaying(), false);
    }

    @Override
    public void resume() {
        Platform.runLater(mediaPlayer::play);
        support.firePropertyChange("playing", isPlaying(), true);
    }

    @Override
    public void rewind() {
        Platform.runLater(() -> mediaPlayer.seek(new Duration(0)));
    }

    @Override
    public void stop() {
        if(mediaPlayer != null){
            Platform.runLater(() -> {
                mediaPlayer.stop();
                mediaPlayer.dispose();
                mediaPlayer = null;
                support.firePropertyChange("playing", isPlaying(), false);
            });
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
        Platform.runLater(() -> mediaPlayer.setVolume(volume));
    }

    @Override
    public double getVolume() {
        return mediaPlayer.getVolume();
    }

    @Override
    public TwoWayIterator<?> getQueue() {
        return playQueue;
    }

    @Override
    public Playable getPlaying() {
        return current;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;
    }

    @Override
    public void addPCL(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePCL(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }
}

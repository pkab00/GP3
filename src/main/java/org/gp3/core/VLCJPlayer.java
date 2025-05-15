package org.gp3.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.gp3.gui.PlayerGUI;

import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class VLCJPlayer implements IPlayer, IMediaObservable {
    private PlayQueue queue;
    private MediaPlayer mediaPlayer;
    private IPlayable current;
    
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final PlayModeIterator playModeIterator;
    private PlayMode playMode;

    public VLCJPlayer(PlayerGUI gui) {
        addPCL(gui);
        playModeIterator = new PlayModeIterator(
                List.of(new DefaultMode(this),
                        new RepeatAllMode(this),
                        new RepeatOneMode(this)));
        playMode = playModeIterator.next(); // Начинаем с режима по умолчанию
        setPlaylist(new ArrayList<>()); // По умолчанию - пустой плейлист
        System.out.println("VLCJPlayer: Setup finished!");
    }

    @Override
    public void addPCL(PropertyChangeListener pcl) {
        support.addPropertyChangeListener(pcl);
    }

    @Override
    public void removePCL(PropertyChangeListener pcl) {
        support.removePropertyChangeListener(pcl);
    }

    @Override
    public void notifyPlayChange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyPlayChange'");
    }

    @Override
    public void notifySongChange(IPlayable oldCurrent) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifySongChange'");
    }

    @Override
    public void notifyProgressChange() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyProgressChange'");
    }

    @Override
    public void notifyPlaylistChange(ArrayList<IPlayable> playlist) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyPlaylistChange'");
    }

    @Override
    public void notifyPlayModeChange(PlayMode playMode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'notifyPlayModeChange'");
    }

    @Override
    public void play() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'play'");
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void rewind() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rewind'");
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stop'");
    }

    @Override
    public void toNext() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toNext'");
    }

    @Override
    public void toPrevious() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toPrevious'");
    }

    @Override
    public void setPlaylist(ArrayList<IPlayable> playlist) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPlaylist'");
    }

    @Override
    public void setVolume(double volume) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVolume'");
    }

    @Override
    public void setPosition(double position) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPosition'");
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setPlayMode'");
    }

    @Override
    public double getVolume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVolume'");
    }

    @Override
    public PlayQueue getQueue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getQueue'");
    }

    @Override
    public double getPosition() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPosition'");
    }

    @Override
    public IPlayable getPlaying() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlaying'");
    }

    @Override
    public PlayMode getPlayMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayMode'");
    }

    @Override
    public boolean isPlaying() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isPlaying'");
    }
    
}

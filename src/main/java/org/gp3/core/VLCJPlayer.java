package org.gp3.core;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

import org.gp3.gui.PlayerGUI;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class VLCJPlayer implements IPlayer, IMediaObservable {
    private PlayQueue queue;
    private MediaPlayer mediaPlayer;
    private IPlayable current;
    private Timer timer;
    
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);
    private final PlayModeIterator playModeIterator;
    private PlayMode playMode;

    public VLCJPlayer(PlayerGUI gui) {
        addPCL(gui);
        mediaPlayer = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
    
        playModeIterator = new PlayModeIterator(
                List.of(new DefaultMode(this),
                        new RepeatAllMode(this),
                        new RepeatOneMode(this)));
        playMode = playModeIterator.next(); // Начинаем с режима по умолчанию
        setPlaylist(new ArrayList<>()); // По умолчанию - пустой плейлист
        mediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            // установка обработчиков событий воспроизведения
            @Override
            public void playing(MediaPlayer mediaPlayer) { // начало воспроизведения
                if(timer == null) startTimer();
                notifyPlayChange();
                notifySongChange(null);
            }
            @Override
            public void finished(MediaPlayer mediaPlayer) { // конец воспроизведения
                SwingUtilities.invokeLater(getPlayMode()::nextSong);
            }
        });
        System.out.println("VLCJPlayer: Setup finished!");
    }

    /**
     * Инициализирует и создаёт таймер, раз в секунду,
     * сообщающий о необходимости, обновить компоненты UI.
     */
    public void startTimer(){
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                notifyProgressChange();
            }
        };
        timer.schedule(task, 0, 1000); // Раз в 1 секунду
    }

    /**
     * Останавливает и обнуляет таймер.
     */
    public void stopTimer(){
        timer.cancel();
        timer = null;
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
        support.firePropertyChange(MediaEvents.PLAY_CHANGE, !isPlaying(), isPlaying());
    }

    @Override
    public void notifySongChange(IPlayable oldCurrent) {
        support.firePropertyChange(MediaEvents.SONG_CHANGE, oldCurrent, current);
    }

    @Override
    public void notifyProgressChange() {
        if (mediaPlayer == null || mediaPlayer.media() == null) return;

        int duration = (int)mediaPlayer.media().info().duration() / 1000;
        int current = (int)mediaPlayer.status().time() / 1000;
        support.firePropertyChange(MediaEvents.PROGRESS_CHANGE, current, duration - current);
    }

    @Override
    public void notifyPlaylistChange(ArrayList<IPlayable> playlist) {
        support.firePropertyChange(MediaEvents.PLAYLIST_CHANGE, null, playlist.size());
    }

    @Override
    public void notifyPlayModeChange(PlayMode playMode) {
        support.firePropertyChange(MediaEvents.PLAY_MODE_CHANGE, null, playMode);
    }

    @Override
    public void play() {
        if(isPlaying()) stop();
        if(current == null) current = queue.next();
        
        mediaPlayer.media().play(current.getFilePath());
    }

    @Override
    public void pause() {
        mediaPlayer.controls().setPause(true);
        notifyPlayChange();
    }

    @Override
    public void resume() {
        mediaPlayer.controls().setPause(false);
        notifyPlayChange();
    }

    @Override
    public void rewind() {
        mediaPlayer.controls().skipTime(-1000000);
        notifyProgressChange();
    }

    @Override
    public void stop() {
        if(mediaPlayer.status().isPlaying()) mediaPlayer.controls().stop();
    }

    @Override
    public void toNext() {
        if(queue.hasNext()){
            IPlayable oldCurrent = current;
            current = queue.next();
            notifySongChange(oldCurrent);
        }
    }

    @Override
    public void toPrevious() {
        if(queue.hasPrevious()){
            IPlayable oldCurrent = current;
            current = queue.previous();
            notifySongChange(oldCurrent);
        }
    }

    @Override
    public void setPlaylist(ArrayList<IPlayable> playlist) {
        this.queue = new PlayQueue(playlist);
        notifyPlaylistChange(playlist);
        System.out.println("VLCJPlayer: Playlist set!");
    }

    @Override
    public void setVolume(double volume) {
        mediaPlayer.audio().setVolume((int)volume);
    }

    @Override
    public void setPosition(double secDelta) {
        double duration = mediaPlayer.media().info().duration();
        double current = mediaPlayer.status().time();
        double newTime = current + (secDelta * 1000);
        newTime = Math.max(0, Math.min(newTime, duration));
        mediaPlayer.controls().setTime((int)newTime);
    }

    @Override
    public void setPlayMode(PlayMode playMode) {
        if(playMode != null) this.playMode = playMode;
        else this.playMode = playModeIterator.next();
        notifyPlayModeChange(this.playMode);
    }

    @Override
    public double getVolume() {
        return mediaPlayer.audio().volume();
    }

    @Override
    public PlayQueue getQueue() {
        return queue;
    }

    @Override
    public double getPosition() {
        return mediaPlayer.status().time();
    }

    @Override
    public IPlayable getPlaying() {
        return current;
    }

    @Override
    public PlayMode getPlayMode() {
        return playMode;
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.status().isPlaying();
    }

    @Override
    public void release() {
        mediaPlayer.release();
    }
    
}

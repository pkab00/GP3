package org.gp3;

import org.gp3.gui.PlaylistGUI;

import javax.swing.*;

// TODO: ДОКУМЕНТИРОВАТЬ
public class PlaylistController implements IPlaylistController {
    private final IPlayer player;
    private final PlaylistGUI playlistGUI;

    public PlaylistController(IPlayer player, PlaylistGUI gui) {
        this.player = player;
        this.playlistGUI = gui;
        player.addPCL(gui);
    }

    @Override
    public void handlePlaylistChange() {
        PlayQueue queue = (PlayQueue) player.getQueue().copy();
        DefaultListModel<IPlayable> listModel = new DefaultListModel<>();
        int itemsCounter = 0;
        while (queue.hasNext()) {
            IPlayable playable = queue.next();
            listModel.addElement(playable);
            itemsCounter++;
        }
        playlistGUI.setListModel(listModel);
        playlistGUI.setItemsButtonText("Items : " + itemsCounter);
    }

    @Override
    public void handleSelectedItem(IPlayable playable) {
        String songData = "";
        songData += "Title: " + (playable.getTitle() == null ? "Unknown" : playable.getTitle()) + "\n";
        songData += "Artist: " + (playable.getArtist() == null? "Unknown" : playable.getArtist()) + "\n";
        songData += "Album: " + (playable.getAlbum() == null ? "Unknown" : playable.getAlbum()) + "\n";
        songData += "Year: " + (playable.getYear() == null ? "Unknown" : playable.getYear()) + "\n";
        songData += "Genre: " + (playable.getGenre() == null ? "Unknown" : playable.getGenre()) + "\n";
        playlistGUI.setSongDataText(songData);
    }
}

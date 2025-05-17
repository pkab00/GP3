package org.amedia;

import java.io.File;
import java.util.ArrayList;

import org.amedia.controller.PlayerController;
import org.amedia.core.IPlayable;
import org.amedia.core.IPlayer;
import org.amedia.core.Song;
import org.amedia.core.VLCJPlayer;
import org.amedia.gui.PlayerGUI;

public class Main {
    @SuppressWarnings("unused")
    private static ArrayList<IPlayable> setUpPlaylist(){
        ArrayList<IPlayable> playlist = new ArrayList<>();
        File playlistFolder = new File("src/test/test_audio");
        File[] files = playlistFolder.listFiles();
        for (File file : files) {
            IPlayable song = new Song(file.getPath());
            playlist.add(song);
        }
        return playlist;
    }

    private static void setUpPlayer(){
        PlayerGUI gui = new PlayerGUI();
        IPlayer player = new VLCJPlayer(gui);
        PlayerController controller = new PlayerController(player);
        gui.setController(controller);
    }

    public static void main(String[] args) {
        setUpPlayer();
    }
}

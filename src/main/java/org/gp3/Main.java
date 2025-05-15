package org.gp3;

import org.gp3.controller.PlayerController;

import org.gp3.core.IPlayable;
import org.gp3.core.IPlayer;
import org.gp3.core.Song;
import org.gp3.core.VLCJPlayer;
import org.gp3.gui.PlayerGUI;

import java.io.File;
import java.util.ArrayList;

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

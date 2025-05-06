package org.gp3;

import org.gp3.controller.PlayerController;
import org.gp3.gui.PlayerGUI;
import org.gp3.logic.AudioPlayer;
import org.gp3.logic.IPlayable;
import org.gp3.logic.Song;

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
        AudioPlayer player = new AudioPlayer(gui);
        PlayerController controller = new PlayerController(player);
        gui.setController(controller);
    }

    public static void main(String[] args) {
        setUpPlayer();
    }
}

package org.gp3;

import org.gp3.gui.PlayerGUI;
import java.util.ArrayList;

public class Main {
    private static void setupPlayer(ArrayList<IPlayable> songs){
        PlayerGUI gui = new PlayerGUI();
        AudioPlayer player = new AudioPlayer(gui);
        player.setPlaylist(songs);
        Controller controller = new Controller(player);
        gui.setController(controller);
    }

    public static void main(String[] args) {
        Song fate = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Fate.mp3");
        Song klaxon = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");
        Song hann = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - HANN (Alone).mp3");

        ArrayList<IPlayable> songs = new ArrayList<>();
        songs.add(fate);
        songs.add(klaxon);
        songs.add(hann);
        setupPlayer(songs);
    }
}

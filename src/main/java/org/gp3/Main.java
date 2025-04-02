package org.gp3;

import javafx.application.Platform;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Song fate = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Fate.mp3");
        Song hann = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - HANN (Alone).mp3");
        Song klaxon = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");

        ArrayList<Playable> songs = new ArrayList<>();
        songs.add(fate);
        songs.add(hann);
        songs.add(klaxon);

        AudioPlayer player = new AudioPlayer(songs);
        player.play();
        Thread.sleep(5000);
        player.stop();
        player.toNext();
        player.play();
    }
}

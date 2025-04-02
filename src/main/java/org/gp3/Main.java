package org.gp3;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String fullPath = "D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3";
        Song klaxon = new Song(fullPath);
        System.out.println(klaxon);
        ArrayList<Playable> songs = new ArrayList<>();
        songs.add(klaxon);
    }
}

package org.gp3;

public class Main {
    public static void main(String[] args) {
        Song klaxon = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");
        System.out.println("Title: " + klaxon.getTitle());
        System.out.println("Artist: " + klaxon.getArtist());
        System.out.println("Album: " + klaxon.getAlbum());
        System.out.println("Year: " + klaxon.getYear());
        System.out.println("Genre: " + klaxon.getGenre());
        System.out.println("Duration: " + klaxon.getDurationMillis());
    }
}

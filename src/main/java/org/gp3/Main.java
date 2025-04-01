package org.gp3;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Song klaxon1 = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");
        Song klaxon2 = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");
        Song klaxon3 = new Song("D:\\GP3\\src\\test\\test_audio\\(G)I-DLE - Klaxon.mp3");

        ArrayList<Playable> lst = new ArrayList<Playable>();
        lst.add(klaxon1);
        lst.add(klaxon2);
        lst.add(klaxon3);

        PlayQueue queue = new PlayQueue(lst);
        System.out.println("From start");
        while(queue.hasNext()){
            Playable p = queue.next();
            System.out.println(p);
        }
        System.out.println("From end");
        while(queue.hasPrevious()){
            Playable p = queue.previous();
            System.out.println(p);
        }
    }
}

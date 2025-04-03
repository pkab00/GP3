package org.gp3.gui;

import org.gp3.AudioPlayer;
import org.gp3.Playable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class SimpleTestGUI extends JFrame {
    private AudioPlayer player;
    private JLabel currentPlaying = new JLabel();

    public SimpleTestGUI(ArrayList<Playable> audio) {
        super("Simple Test GUI");
        setLayout(new FlowLayout());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                player.stop();
                dispose();
                System.exit(0);
            }
        });

        initAudioPlayer(audio);
        createGUI();
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initAudioPlayer(ArrayList<Playable> audio) {
        player = new AudioPlayer(audio);
        System.out.println("Audio player initialized");
    }

    private void createGUI() {
        JPanel panel = new JPanel();
        JButton play = new JButton("Play");
        JButton stop = new JButton("Stop");
        JButton pause = new JButton("Pause");
        JButton resume = new JButton("Resume");
        JButton rewind = new JButton("Rewind");
        stop.setEnabled(false);
        pause.setEnabled(false);
        resume.setEnabled(false);
        rewind.setEnabled(false);

        play.addActionListener(e -> {
            player.play();
            play.setEnabled(false);
            stop.setEnabled(true);
            pause.setEnabled(true);
            resume.setEnabled(false);
            rewind.setEnabled(true);
            updateCurrentPlaying();
        });
        stop.addActionListener(e -> {
            player.stop();
            play.setEnabled(true);
            stop.setEnabled(false);
            pause.setEnabled(false);
            resume.setEnabled(false);
            rewind.setEnabled(false);
            updateCurrentPlaying();
        });
        pause.addActionListener(e -> {
            player.pause();
            pause.setEnabled(false);
            resume.setEnabled(true);
            rewind.setEnabled(false);
        });
        resume.addActionListener(e -> {
            player.resume();
            pause.setEnabled(true);
            resume.setEnabled(false);
            rewind.setEnabled(true);
        });
        rewind.addActionListener(e -> {
            player.rewind();
        });

        updateCurrentPlaying();
        panel.add(currentPlaying);
        panel.add(play);
        panel.add(stop);
        panel.add(pause);
        panel.add(resume);
        panel.add(rewind);

        add(panel, BorderLayout.CENTER);
        pack();
        System.out.println("GUI created");
    }

    private void updateCurrentPlaying() {
        if(player.getPlaying() == null) {
            currentPlaying.setText("Playing: None");
        } else {
            currentPlaying.setText(String.format("Playing: %s - %s",
                    player.getPlaying().getArtist(), player.getPlaying().getTitle()));
        }

    }
}

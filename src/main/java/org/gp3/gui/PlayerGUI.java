package org.gp3.gui;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class PlayerGUI extends JFrame implements PropertyChangeListener {
    private JPanel rootPanel;
    private JButton aboutButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton jumpBackButton;
    private JButton jumpForwardButton;
    private JButton playPauseButton;
    private JPanel playPanel;
    private JToolBar menuBar;
    private JLabel nowPlayingLabel;
    private JButton selectButton;

    public PlayerGUI() {
        super("GP3 Player GUI");
        setSize(750, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setVisible(true);
    }

    public void updatePlayback(boolean isPlaying) {
        playPauseButton.setText(isPlaying ? "./icons/pause.png" : "./icons/play.png");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if(propertyName.equals("playing")){
            updatePlayback((boolean) evt.getNewValue());
        }
    }

    public static void main(String[] args) {
        PlayerGUI gui = new PlayerGUI();
    }
}

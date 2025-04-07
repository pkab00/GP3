package org.gp3.gui;

import org.gp3.*;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

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

    private ControllerInterface controller;

    public PlayerGUI() {
        super("GP3 Player GUI");
        setSize(750, 200);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setVisible(true);
    }

    public void setController(ControllerInterface controller) {
        this.controller = controller;
        previousButton.addActionListener(e -> controller.handlePrevious());
        nextButton.addActionListener(e -> controller.handleNext());
        jumpBackButton.addActionListener(e -> controller.handleJumpBackward());
        jumpForwardButton.addActionListener(e -> controller.handleJumpForward());
        playPauseButton.addActionListener(e -> controller.handlePlay());
    }

    public void updatePlayback(boolean isPlaying) {
        Arrays.stream(playPauseButton.getActionListeners()).forEach(playPauseButton::removeActionListener);

        playPauseButton.setIcon(new ImageIcon(isPlaying ? "src/main/resources/icons/pause.png" : "src/main/resources/icons/play.png"));
        playPauseButton.addActionListener(isPlaying ? (e -> controller.handlePause()) : (e -> controller.handleResume()));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        if(propertyName.equals("playing")){
            updatePlayback((boolean) evt.getNewValue());
        }
    }
}

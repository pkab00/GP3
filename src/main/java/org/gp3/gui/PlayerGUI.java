package org.gp3.gui;

import javax.swing.*;

public class PlayerGUI extends JFrame {
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

    public static void main(String[] args) {
        PlayerGUI gui = new PlayerGUI();
    }
}

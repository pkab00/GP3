package org.gp3.gui;

import javax.swing.*;

public class ProgressGUI extends JFrame {
    private JProgressBar bar;
    private JPanel rootPane;
    private JLabel label;

    public ProgressGUI() {
        setTitle("Processing songs...");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setResizable(false);
        setContentPane(rootPane);
        setVisible(true);
    }

    public void setMaximum(int maximum){
        bar.setMaximum(maximum);
    }

    public void updateProgress(int progress){
        bar.setValue(progress);
        label.setText(String.format("Добавлено песен: %d/%d", progress, bar.getMaximum()));
    }

    public void done(){
        dispose();
    }
}

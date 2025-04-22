package org.gp3.gui;

import javax.swing.*;

/**
 * Мини-окно для отображения прогресса добавления песен в плейлист.
 */
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

    /**
     * Устанавливает максимальное значение прогресса
     * @param maximum максимальное значение
     */
    public void setMaximum(int maximum){
        bar.setMaximum(maximum);
    }

    /**
     * Обновляет прогресс добавления песен.
     * @param progress текущее значение
     */
    public void updateProgress(int progress){
        bar.setValue(progress);
        label.setText(String.format("Добавлено песен: %d/%d", progress, bar.getMaximum()));
    }

    /**
     * Закрывает окно по завершении процесса.
     */
    public void done(){
        dispose();
    }
}

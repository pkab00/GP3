package org.amedia.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Мини-окно для отображения прогресса добавления песен в плейлист.
 */
public class ProgressGUI extends SuperGUI {
    private JProgressBar bar;
    private JLabel label;

    /**
     * Конструктор окна прогресса.
     */
    public ProgressGUI() {
        super("Загрузка...");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        setResizable(false);

        // Инициализация компонентов
        initComponents();
        setVisible(true);
    }

    /**
     * Инициализация компонентов GUI.
     */
    private void initComponents() {
        // Основная панель с GridLayout (2 строки, 1 столбец)
        JPanel rootPane = new JPanel(new GridLayout(2, 1, 10, 10));
        rootPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Прогресс-бар
        bar = new JProgressBar();
        bar.setStringPainted(true); // Отображать текст на прогресс-баре
        rootPane.add(bar);

        // Метка для отображения текста
        label = new JLabel("Добавлено песен: 0/0", SwingConstants.CENTER);
        label.setFont(new Font("Yu Gothic UI", Font.PLAIN, 16));
        rootPane.add(label);

        // Установка основной панели в окно
        setContentPane(rootPane);
    }

    /**
     * Устанавливает максимальное значение прогресса.
     * @param maximum максимальное значение
     */
    public void setMaximum(int maximum) {
        bar.setMaximum(maximum);
    }

    /**
     * Обновляет прогресс добавления песен.
     * @param progress текущее значение
     */
    public void updateProgress(int progress) {
        bar.setValue(progress);
        label.setText(String.format("Добавлено песен: %d/%d", progress, bar.getMaximum()));
    }

    /**
     * Закрывает окно по завершении процесса.
     */
    public void done() {
        dispose();
    }
}

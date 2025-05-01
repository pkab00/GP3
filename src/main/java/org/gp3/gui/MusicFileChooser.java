package org.gp3.gui;

import org.gp3.SongLoader;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

/**
 * Кастомный класс, основанный на {@link JFileChooser}. <p>
 * Поддерживает только аудио-форматы, а собранные файлы передаёт для дальнейшей обработки в {@link SongLoader
 */
public class MusicFileChooser extends JFileChooser {
    private final String[][] FILTERS = {
            {"mp3", "MP3 files (*.mp3)"},
            {"wav", "WAV files (*.wav)"}
    };

    /**
     * Инициализация и установка фильтров.
     */
    public MusicFileChooser() {
        setMultiSelectionEnabled(true);
        setFileSelectionMode(JFileChooser.FILES_ONLY);
        for (String[] filter : FILTERS) {
            addChoosableFileFilter(new FileNameExtensionFilter(filter[1], filter[0]));
        }

        UIManager.put("FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI"); // Стиль Metal
        UIManager.put("FileChooser.iconSize", 32); // Размер иконок
        UIManager.put("FileChooser.font", new Font("Segoe UI", Font.PLAIN, 12)); // Шрифт
    }

    /**
     * Вывод на экран диалогового окна и получение выбранных файлов.
     * @param parent родительский компонент
     */
    public void showDialog(JFrame parent) {
        int returnVal = showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            getSelectedFiles();
        }
    }
}

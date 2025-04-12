package org.gp3.gui;

import org.gp3.IPlayable;
import org.gp3.Song;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * Кастомный класс, основанный на {@link JFileChooser}. <p>
 * Поддерживает только аудио-форматы, а собранные файлы конвертирует в {@link IPlayable}. <p>
 * Используйте {@link #showDialog(JComponent)} для отображения диалогового окна
 * и {@link #getOutputSongs()} для получения вывода.
 */
public class MusicFileChooser extends JFileChooser {
    private final String[][] FILTERS = {
            {"mp3", "MP3 files (*.mp3)"},
            {"wav", "WAV files (*.wav)"}
    };
    private ArrayList<IPlayable> output = new ArrayList<>();

    /**
     * Инициализация и установка фильтров.
     */
    public MusicFileChooser() {
        setMultiSelectionEnabled(true);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        for (String[] filter : FILTERS) {
            addChoosableFileFilter(new FileNameExtensionFilter(filter[1], filter[0]));
        }

        UIManager.put("FileChooserUI", "javax.swing.plaf.metal.MetalFileChooserUI"); // Стиль Metal
        UIManager.put("FileChooser.iconSize", 32); // Размер иконок
        UIManager.put("FileChooser.font", new Font("Segoe UI", Font.PLAIN, 12)); // Шрифт
    }

    /**
     * Вывод на экран диалогового окна и последующая обработка выбранных файлов.
     * @param parent родительский компонент
     */
    public void showDialog(JFrame parent) {
        output.clear();
        int returnVal = showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = getSelectedFiles();
            for (File file : files) {
                if(file.isFile()) {
                    handleFiles(file);
                }
                else if(file.isDirectory()) {
                    handleDirectories(file);
                }
            }
        }
    }

    /**
     * Конвертация объекта File в Song.
     * @param file исходный файл
     * @see #handleDirectories(File)
     */
    private void handleFiles(File file){
        IPlayable song = new Song(file.getPath());
        output.add(song);
    }

    /**
     * Обработка директорий. Каждый музыкальный файл будет конвертирован в Song.
     * @param dir исходная директория
     * @see #handleFiles(File)
     */
    private void handleDirectories(File dir){
        if(dir.listFiles() == null) {return;}
        for(File file : dir.listFiles()){
            for (String[] filter : FILTERS) {
                if (file.getName().endsWith(filter[0])) {
                    handleFiles(file);
                }
            }
        }
    }

    /**
     * @return ArrayList с объектами Song, пригодный для использования
     * {@link org.gp3.IPlayer#setPlaylist(ArrayList) методом плеера}.
     */
    public ArrayList<IPlayable> getOutputSongs(){
        return output;
    }
}

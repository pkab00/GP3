package org.gp3;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * SwingWorker, занимающийся фоновой обработкой музыки и формированием списка воспроизведения.
 */
public class SongLoader extends SwingWorker<ArrayList<IPlayable>, Integer> {
    private final File[] files; // выбранные файлы
    private final Callback<Integer, ArrayList<IPlayable>> callback; // коллбэк
    private final ArrayList<IPlayable> output = new ArrayList<>(); // результат - список воспроизведения

    /**
     * Конструктор по умолчанию. После выполнения вызывает стартовый метод коллбэка.
     * @param files список файлов
     * @param callback коллбэк
     */
    public SongLoader(File[] files, Callback<Integer, ArrayList<IPlayable>> callback) {
        this.files = files;
        this.callback = callback;
        callback.onStarted();
    }

    /**
     * Конвертация объекта File в Song.
     * @param file исходный файл
     */
    private void handleFiles(File file){
        IPlayable song = new Song(file.getPath());
        output.add(song);
    }

    /**
     * Метод, выполняющийся в фоне. Проходит по всем файлам и добавляет их в список воспроизведения.
     * После каждой итерации вызывает коллбэк для обновления прогресса.
     * @return готовый список воспроизведения
     */
    @Override
    protected ArrayList<IPlayable> doInBackground() {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if(file.isFile() && file.canRead()) {
                handleFiles(file);
                publish(i);
            }
        }
        return output;
    }

    /**
     * Метод, вызываемый после каждой итерации. Обращается к колбэку для обновления прогресса.
     * @param chunks intermediate results to process
     *
     */
    @Override
    protected void process(List<Integer> chunks) {
        callback.onProgress(chunks.getLast());
    }

    /**
     * Метод, вызываемый по завершению. Передаёт результат обработки плееру через колбэк.
     */
    @Override
    protected void done() {
        callback.onFinished(output);
    }
}

package org.gp3;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SongLoader extends SwingWorker<ArrayList<IPlayable>, Integer> {
    private final File[] files;
    private final Callback<Integer, ArrayList<IPlayable>> callback;
    private final ArrayList<IPlayable> output = new ArrayList<>();

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

    @Override
    protected ArrayList<IPlayable> doInBackground() throws Exception {
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if(file.isFile() && file.canRead()) {
                handleFiles(file);
                publish(i);
            }
        }
        return output;
    }

    @Override
    protected void process(List<Integer> chunks) {
        callback.onProgress(chunks.getLast());
    }

    @Override
    protected void done() {
        callback.onFinished(output);
    }
}

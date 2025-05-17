package org.amedia.data;

import java.util.ArrayList;
import javax.swing.SwingWorker;

import org.amedia.core.IPlayable;
import org.amedia.utils.Callback;

/**
 * Класс, который загружает очередь воспроизведения
 * из указанной таблицы БД. Он реализует интерфейс
 * {@link SwingWorker} и может быть использован
 * для фоновой загрузки очереди.
 * <p>
 * Результатом является список {@link IPlayable},
 * который может быть получен с помощью коллбэка.
 * 
 * @see SongLoader
 */
public class DBLoader extends SwingWorker<ArrayList<IPlayable>, Void> {
    private ArrayList<IPlayable> output = new ArrayList<>();
    private final Callback<Void, ArrayList<IPlayable>> callback;
    private final DBManager manager;
    private final String playlistName;

    public DBLoader(String playlistName, Callback<Void, ArrayList<IPlayable>> callback){
        this.callback = callback;
        this.manager = new DBManager();
        this.playlistName = playlistName;
        callback.onStarted();
    }

    /**
     * Фоновый метод, который производит загрузку
     * очереди воспроизведения из указанной таблицы
     * БД. Результатом является список {@link IPlayable},
     * который будет передан в коллбэк.
     * @return список {@link IPlayable} из указанной таблицы
     * @throws Exception если возникла ошибка
     */
    @Override
    protected ArrayList<IPlayable> doInBackground() throws Exception {
        output = manager.getPlaylist(playlistName);
        return output;
    }

    /**
     * Метод, который будет вызван после
     * завершения фоновой задачи. Он
     * передает результат загрузки
     * очереди воспроизведения в коллбэк
     * {@link Callback#onFinished(Object)}
     */
    @Override
    protected void done(){
        manager.close();
        callback.onFinished(output);
    }
    
}

package org.gp3.data;

import java.sql.*;

import org.gp3.core.IPlayable;
import org.gp3.core.PlayQueue;

public class DBManager {
    private final String DB_NAME = "__PLAYLISTS__";
    private Connection conn;
    private PreparedStatement prep;

    public DBManager() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:src\\main\\resources\\DB\\playlists.db");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Добавляет новую запись в БД.
     * @param playlistName имя плейлиста
     * @param queue текущий список воспроизвежения
     * @return {@code true} если добавление прошло успешно, {@code false} в противном случае
     */
   public boolean addNewRecord(String playlistName, PlayQueue queue) {
            try {
                prep = conn.prepareStatement("INSERT INTO " + DB_NAME + " (name) VALUES (?)");
                prep.setString(1, playlistName);
                prep.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
        }
        createNewPlaylist(playlistName);
        setPlaylistData(playlistName, queue);
        return true;
    }

    /**
     * Создает новую таблицу, соответствующую плейлисту, 
     * если она не существует.
     * @param playlistName имя плейлиста
     */
    private void createNewPlaylist(String playlistName){
        try {
            prep = conn.prepareStatement(String.format(
                "CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL);", playlistName));
            prep.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Записывает данные очереди воспроизведения в соответствующую таблицу БД.
     * @param playlistName имя плейлиста
     * @param queue очередь воспроизведения
     */
    private void setPlaylistData(String playlistName, PlayQueue queue){
        while(queue.hasNext()){
            IPlayable song = queue.next();
            String path = song.getFilePath();

            String statement = String.format("INSERT INTO %s (path) VALUES (?)", playlistName);
            try {
                prep = conn.prepareStatement(statement);
                prep.setString(1, path);
                prep.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Закрывает соединение с БД.
     * @see java.sql.Connection#close()
     */
    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

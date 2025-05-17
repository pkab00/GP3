package org.gp3.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.gp3.core.IPlayable;
import org.gp3.core.PlayQueue;
import org.gp3.core.Song;
import org.gp3.utils.HashUtil;

/**
 * Класс, который управляет БД, хранящей информацию 
 * о плейлистах.
 * Самый низкий уровень работы с БД. 
 * Задействуется в controller-ах и worker-ах.
 * 
 * @see DBLoader
 */
public class DBManager implements AutoCloseable {
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
     * Вспомагательный метод хэширования.
     * Использует алгоритм SHA256.
     * @param playlistName имя плейлиста
     * @return хэш
     * @see HashUtil
     */
    private String getHash(String playlistName){
        return HashUtil.sha256(playlistName);
    }

    /**
     * Добавляет новую запись в БД.
     * @param playlistName имя плейлиста
     * @param queue текущий список воспроизвежения
     * @return {@code true} если добавление прошло успешно, {@code false} в противном случае
     */
    public boolean addNewRecord(String playlistName, PlayQueue queue) {
        String hash = getHash(playlistName);
        try {
            prep = conn.prepareStatement("INSERT INTO " + DB_NAME + " (hash, name) VALUES (?, ?)");
            prep.setString(1, hash);
            prep.setString(2, playlistName);
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
        String hash = getHash(playlistName);
        try {
            prep = conn.prepareStatement(String.format(
                "CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT, path TEXT NOT NULL);", hash));
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
        String hash = getHash(playlistName);
        while(queue.hasNext()){
            IPlayable song = queue.next();
            String path = song.getFilePath();

            String statement = String.format("INSERT INTO %s (path) VALUES (?)", hash);
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
     * Возвращает список {@link IPlayable} из указанной таблицы,
     * содержащий информацию о песнях, которые есть в плейлисте.
     * @param playlistName имя плейлиста
     * @return список {@link IPlayable} или {@code null} если при получении данных произошла ошибка
     */
    public ArrayList<IPlayable> getPlaylist(String playlistName){
        ArrayList<IPlayable> output = new ArrayList<>();
        String hash = getHash(playlistName);
        try {
            prep = conn.prepareStatement(String.format("SELECT path FROM %s ORDER BY id", hash));
            ResultSet res = prep.executeQuery();
            while(res.next()){
                String path = res.getString("path");
                IPlayable song = new Song(path);
                if(song.getFilePath() != null){
                    output.add(song);
                } else{
                    removeRecordByPath(playlistName, path);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return output;
    }

    /**
     * Удаляет запись с указанным путем из указанной таблицы.
     * @param playlistName имя плейлиста
     * @param path путь к файлу песни
     * @return {@code true} если удаление прошло успешно, {@code false} в противном случае
     */
    private boolean removeRecordByPath(String playlistName, String path) {
        String hash = getHash(playlistName);
        try {
            String statement = String.format("DELETE FROM %s WHERE path = ?", hash);
            prep = conn.prepareStatement(statement);
            prep.setString(1, path);
            int rowsAffected = prep.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Возвращает список имён существующих плейлистов.
     * @return список имён плейлистов
     */
    public List<String> getPlaylistNames(){
        List<String> output = new ArrayList<>();
        try {
            String statement = String.format("SELECT name FROM %s", DB_NAME);
            prep = conn.prepareStatement(statement);
            ResultSet res = prep.executeQuery();
            while(res.next()){
                String name = res.getString("name");
                output.add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return output;
    }

    /**
     * Возвращает список путей к трекам указанного плейлиста.
     * @param playlistName имя плейлиста
     * @return список путей к трекам или {@code null} если при получении данных произошла ошибка
     */
    public List<String> getPlaylistPaths(String playlistName){
        List<String> output = new ArrayList<>();
        String hash = getHash(playlistName);
        try {
            String statement = String.format("SELECT path FROM %s", hash);
            prep = conn.prepareStatement(statement);
            ResultSet res = prep.executeQuery();
            while(res.next()){
                String path = res.getString("path");
                output.add(path);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return output;
    }

    /**
     * Закрывает соединение с БД.
     * @see java.sql.Connection#close()
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close(){
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

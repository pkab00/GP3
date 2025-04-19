package org.gp3.parse;

/**
 * Класс для хранения метаданных песни.
 */
public class SongMetadata {
    private final String title;
    private final String artist;
    private final String album;
    private final String genre;
    private final String year;
    private final double durationMillis;

    /**
     * Закрытый конструктор.
     * Используется методом {@link #getInstance}
     * @param title заголовок песни
     * @param artist исполнитель
     * @param album альбом
     * @param genre жанр
     * @param year год
     * @param durationMillis длительность в миллисекундах
     */
    private SongMetadata(String title,
                         String artist,
                         String album,
                         String genre,
                         String year,
                         double durationMillis){
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.genre = genre;
        this.year = year;
        this.durationMillis = durationMillis;
    }

    /**
     * @return заголовок песни
     */
    public String title(){
        return title;
    }

    /**
     * @return исполнитель
     */
    public String artist(){
        return artist;
    }
    /**
     * @return альбом
     */
    public String album(){
        return album;
    }
    /**
     * @return жанр
     */
    public String genre(){
        return genre;
    }
    /**
     * @return год выпуска
     */
    public String year(){
        return year;
    }
    /**
     * @return длительность в миллисекундах
     */
    public double durationMillis(){
        return durationMillis;
    }

    /**
     * Статический метод для создания объекта {@link SongMetadata}.
     * Присваивает значения по умолчанию, если аргументы равны null.
     * @param title заголовок
     * @param artist исполнитель
     * @param album альбом
     * @param genre жанр
     * @param year год
     * @param duration длительность
     * @return объект {@link SongMetadata}
     */
    public static SongMetadata getInstance(String title, String artist,
                                           String album, String genre,
                                           String year, double duration) {
        title = (title != null) ? (!title.isEmpty() ? title : "UNTITLED") : "UNTITLED";
        artist = (artist != null) ? (!artist.isEmpty() ? artist : "UNKNOWN") : "UNKNOWN";
        album = (album != null) ? (!album.isEmpty() ? album : "UNKNOWN") : "UNKNOWN";
        genre = (genre != null) ? (!genre.isEmpty() ? genre : "UNKNOWN") : "UNKNOWN";
        year = (year != null) ? (!year.isEmpty() ? year : "UNKNOWN") : "UNKNOWN";
        duration = (duration > 0) ? duration : 0;
        return new SongMetadata(title, artist, album, genre, year, duration);
    }
}

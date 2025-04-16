package org.gp3;

import org.apache.tika.metadata.*;
import org.apache.tika.parser.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.nio.file.*;

/**
 * Класс, содержащий информацию о песне и реализующий интерфейс IPlayable.
 * Парсит информацию о песне с помощью Tika, содержит методы для доступа к метаданным.
 */
public class Song implements IPlayable {
    private String filePath;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String year;
    private double durationMillis;

    /**
     * Конструктор класса.
     * Если указанный файл существует, выполняет парсинг метаданных.
     * @param filePath путь к файлу
     */
    public Song(String filePath) {
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) {
            System.out.println("File not found: " + filePath);
            return;
        }
        this.filePath = filePath;
        extractMetadata();
    }

    /**
     * Возвращает информацию о песне в виде строки.
     * @return строка формата "Исполнитель - Название песни"
     */
    @Override
    public String toString() {
        return artist + " - " + title;
    }

    /**
     * Метод для парсинга метаданных песни.
     */
    private void extractMetadata(){
        try{
            InputStream inputStream = new FileInputStream(new File(filePath));
            Parser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            DefaultHandler handler = new DefaultHandler();
            parser.parse(inputStream, handler, metadata, context);
            inputStream.close();

            this.title = metadata.get("dc:title"); // название песни
            this.artist = metadata.get("xmpDM:artist"); // исполнитель
            this.album = metadata.get("xmpDM:album"); // альбом
            this.genre = metadata.get("xmpDM:genre"); // жанр
            this.year = metadata.get("xmpDM:releaseDate"); // год выпуска
            this.durationMillis = Double.parseDouble(metadata.get("xmpDM:duration")); // длительность в миллисекундах
        } catch(Exception e){ // обработка исключения парсинга
            System.out.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return путь к файлу песни
     */
    @Override
    public String getFilePath() {
        return filePath;
    }

    /**
     * @return название песни
     */
    @Override
    public String getTitle() {
        return title;
    }

    /**
     * @return исполнитель песни
     */
    @Override
    public String getArtist() {
        return artist;
    }

    /**
     * @return альбом, в котором выпущена песня
     */
    @Override
    public String getAlbum() {
        return album;
    }

    /**
     * @return жанр песни
     */
    @Override
    public String getGenre() {
        return genre;
    }

    /**
     * @return год выпуска песни
     */
    @Override
    public String getYear() {
        return year;
    }

    /**
     * @return длительность песни в миллисекундах
     */
    @Override
    public double getDurationMillis() {
        return durationMillis;
    }
}

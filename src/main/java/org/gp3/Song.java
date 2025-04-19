package org.gp3;

import org.gp3.parse.SmartParser;
import org.gp3.parse.SongMetadata;
import org.gp3.parse.Mp3Parser;
import org.gp3.parse.WavParser;

import java.nio.file.*;

/**
 * Класс, содержащий информацию о песне и реализующий интерфейс IPlayable.
 * Парсит информацию о песне с помощью Tika, содержит методы для доступа к метаданным.
 */
public class Song implements IPlayable {
    private String filePath;
    private SongMetadata metadata;

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
        this.metadata = new SmartParser().parse(filePath);
    }

    /**
     * Возвращает информацию о песне в виде строки.
     * @return строка формата "Исполнитель - Название песни"
     */
    @Override
    public String toString() {
        return metadata.artist() + " - " + metadata.title();
    }

    /**
     * @return путь к файлу песни
     */
    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public SongMetadata getMetadata() {
        return metadata;
    }
}

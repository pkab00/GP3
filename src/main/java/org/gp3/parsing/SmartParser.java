package org.gp3.parsing;

import java.util.Map;

/**
 * Универсальный парсер для аудиофайлов различных форматов.
 * Реализует паттерн "Стратегия", делегируя процесс парсинга другим классам.
 * @implNote Парсер выбирает класс-реализацию IParser в зависимости от расширения файла.
 */
public class SmartParser implements IParser{
    // карта доступных парсеров, где ключ - расширение файла, а значение - класс-реализация IParser
    private final Map<String, Class<? extends IParser>> AVAILABLE_PARSERS =
            Map.of(
                    ".mp3", Mp3Parser.class, // .mp3
                    ".wav", WavParser.class // .wav
            );

    /**
     * Метод, непосредственно отвечающий за парсинг.
     * Выбирает класс-реализацию IParser в зависимости от расширения файла.
     * Затем вызывает метод {@code parse()} конкретного класса-реализации.
     * @param filePath путь к файлу
     * @return объект {@link SongMetadata}
     */
    @Override
    public SongMetadata parse(String filePath) {
        for(String key : AVAILABLE_PARSERS.keySet()){
            if(filePath.endsWith(key)){
                Class<? extends IParser> parserClass = AVAILABLE_PARSERS.get(key);
                try {
                    IParser parser = parserClass.getDeclaredConstructor().newInstance();
                    return parser.parse(filePath);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}

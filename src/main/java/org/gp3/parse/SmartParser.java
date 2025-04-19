package org.gp3.parse;

import java.util.Map;

public class SmartParser implements IParser{
    private final Map<String, Class<? extends IParser>> AVAILABLE_PARSERS =
            Map.of(
                    ".mp3", Mp3Parser.class,
                    ".wav", WavParser.class);

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

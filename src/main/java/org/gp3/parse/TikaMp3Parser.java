package org.gp3.parse;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TikaMp3Parser implements IParser{
    @Override
    public SongMetadata parse(String filePath) {
        try{
            InputStream inputStream = new FileInputStream(new File(filePath));
            Parser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            DefaultHandler handler = new DefaultHandler();
            parser.parse(inputStream, handler, metadata, context);
            inputStream.close();

            String title = metadata.get("dc:title"); // название песни
            String artist = metadata.get("xmpDM:artist"); // исполнитель
            String album = metadata.get("xmpDM:album"); // альбом
            String genre = metadata.get("xmpDM:genre"); // жанр
            String year = metadata.get("xmpDM:releaseDate"); // год выпуска
            double durationMillis = Double.parseDouble(metadata.get("xmpDM:duration")); // длительность в миллисекундах

            return new SongMetadata(title, artist, album, genre, year, durationMillis);
        } catch(Exception e){
            System.out.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

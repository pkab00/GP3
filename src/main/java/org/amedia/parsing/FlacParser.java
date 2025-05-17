package org.amedia.parsing;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

public class FlacParser implements IParser {
    static{
        Logger logger = Logger.getLogger("org.jaudiotagger"); // отключение лишнего логирования
        logger.setLevel(Level.OFF);
    }
    @Override
    public SongMetadata parse(String filePath) {
        try{
            AudioFile audioFile = AudioFileIO.read(new File(filePath));
            Tag tag = audioFile.getTag();
            
            String title = tag.getFirst(FieldKey.TITLE);
            String artist = tag.getFirst(FieldKey.ARTIST);
            String album = tag.getFirst(FieldKey.ALBUM);
            String genre = tag.getFirst(FieldKey.GENRE);
            String year = tag.getFirst(FieldKey.YEAR);
            double durationMillis = audioFile.getAudioHeader().getTrackLength();

            return SongMetadata.getInstance(title, artist, album, genre, year, durationMillis);
        } catch(Exception e){
            System.out.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

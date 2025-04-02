package org.gp3;

import org.apache.tika.metadata.*;
import org.apache.tika.parser.*;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;

public class Song implements Playable {
    private final String filePath;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String year;
    private long durationMillis;

    public Song(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath);
            filePath = null;
        }
        this.filePath = filePath;
        extractMetadata();
    }

    @Override
    public String toString() {
        String output = "";
        output += "Title: " + title + "\n";
        output += "Artist: " + artist + "\n";
        output += "Album: " + album + "\n";
        output += "Genre: " + genre + "\n";
        output += "Year: " + year + "\n";
        output += "Duration: " + durationMillis + "\n";
        return output;
    }

    private void extractMetadata(){
        try{
            InputStream inputStream = new FileInputStream(new File(filePath));
            Parser parser = new AutoDetectParser();
            Metadata metadata = new Metadata();
            ParseContext context = new ParseContext();
            DefaultHandler handler = new DefaultHandler();
            parser.parse(inputStream, handler, metadata, context);
            inputStream.close();

            this.title = metadata.get("dc:title");
            this.artist = metadata.get("xmpDM:artist");
            this.album = metadata.get("xmpDM:album");
            this.genre = metadata.get("xmpDM:genre");
            this.year = metadata.get("xmpDM:releaseDate");
            this.durationMillis = Math.round(Double.parseDouble(metadata.get("xmpDM:duration")));
        } catch(Exception e){
            System.out.println("Parsing failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getArtist() {
        return artist;
    }

    @Override
    public String getAlbum() {
        return album;
    }

    @Override
    public String getGenre() {
        return genre;
    }

    @Override
    public String getYear() {
        return year;
    }

    @Override
    public long getDurationMillis() {
        return durationMillis;
    }
}

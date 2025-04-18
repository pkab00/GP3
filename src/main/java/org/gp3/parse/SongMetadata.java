package org.gp3.parse;

public record SongMetadata(String title,
                           String artist,
                           String album,
                           String genre,
                           String year,
                           double durationMillis) {
}

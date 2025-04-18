package org.gp3;

public record SongMetadata(String title,
                           String artist,
                           String album,
                           String genre,
                           String year,
                           double durationMillis) {
}

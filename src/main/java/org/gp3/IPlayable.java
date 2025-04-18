package org.gp3;

import org.gp3.parse.SongMetadata;

public interface IPlayable {
    String getFilePath();
    SongMetadata getMetadata();
}

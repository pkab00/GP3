package org.gp3.logic;

import org.gp3.parsing.SongMetadata;

public interface IPlayable {
    String getFilePath();
    SongMetadata getMetadata();
}

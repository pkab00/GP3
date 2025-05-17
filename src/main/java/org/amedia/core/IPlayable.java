package org.amedia.core;

import org.amedia.parsing.SongMetadata;

public interface IPlayable {
    String getFilePath();
    SongMetadata getMetadata();
}

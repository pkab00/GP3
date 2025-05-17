package org.amedia.controller;

import org.amedia.core.IPlayable;

public interface IPlayQueueController {
    void handleSongChange();
    void handleSelectedItem(IPlayable playable);
}

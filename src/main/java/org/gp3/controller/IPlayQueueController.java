package org.gp3.controller;

import org.gp3.core.IPlayable;

public interface IPlayQueueController {
    void handleSongChange();
    void handleSelectedItem(IPlayable playable);
}

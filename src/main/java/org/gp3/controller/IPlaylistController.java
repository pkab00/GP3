package org.gp3.controller;

import org.gp3.logic.IPlayable;

public interface IPlaylistController {
    void handlePlaylistChange();
    void handleSelectedItem(IPlayable playable);
}

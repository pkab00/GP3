package org.amedia.gui;

import java.util.List;

public interface IPlaylistMenuGUI {
    void updatePlaylistNames(List<String> playlistNames);
    void updatePlaylistData(String data);
    String getSelectedPlaylistName();
}

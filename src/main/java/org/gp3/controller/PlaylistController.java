package org.gp3.controller;

import java.util.ArrayList;
import java.util.List;

import org.gp3.core.IPlayable;
import org.gp3.core.IPlayer;
import org.gp3.data.DBLoader;
import org.gp3.data.DBManager;
import org.gp3.gui.IPlaylistMenuGUI;
import org.gp3.utils.Callback;

public class PlaylistController implements IPlaylistController {
    private final IPlaylistMenuGUI gui;
    private final IPlayer player;

    public PlaylistController(IPlaylistMenuGUI gui, IPlayer player){
        this.gui = gui;
        this.player = player;
    }

    /**
     * Обрабатывает обновление доступных плейлистов.
     * Получает список имён плейлистов из базы данных
     * и обновляет интерфейс с помощью метода updatePlaylistNames.
     */
    @Override
    public void handleAviablePlaylistsUpdate() {
        try(DBManager dbManager = new DBManager()){
            List<String> playlistNames = dbManager.getPlaylistNames();
            gui.updatePlaylistNames(playlistNames);
        }
    }

    /**
     * Обработчик выбора элемента в списке плейлистов.
     * Получает список файлов, входящих в выбранный плейлист,
     * и отображает информацию о нём в правой части интерфейса
     * с помощью метода updatePlaylistData.
     */
    @Override
    public void handleItemSelection() {
        try(DBManager dbManager = new DBManager()){
            List<String> paths = dbManager.getPlaylistPaths(gui.getSelectedPlaylistName());
            String text = "";
            for(int i = 0; i < paths.size(); i++){
                text += String.format("%d. %s\n", i+1, paths.get(i));
            }
            gui.updatePlaylistData(text);
        }
    }

    /**
    * Обработчик загрузки выбранного плейлиста.
    * Использует {@link DBLoader} для загрузки очереди воспроизведения,
    * после чего отображает информацию о загруженном плейлисте
    * с помощью метода updatePlaylistData.
    * TODO: использовать ProgressGUI
    */
    @Override
    public void handleLoadingPlaylist() {
        DBLoader loader = new DBLoader(gui.getSelectedPlaylistName(), new Callback<Void,ArrayList<IPlayable>>() {
            @Override
            public void onStarted() {
                return; // TODO: ПРИМЕНИТЬ ЗДЕСЬ ProgressGUI
            }
            @Override
            public void onProgress(Void progress) {}
            @Override
            public void onFinished(ArrayList<IPlayable> parameter) {
                player.setPlaylist(parameter);
            }
        });
        loader.execute();
    }
}

package org.gp3;

import org.gp3.gui.PlaylistGUI;

import javax.swing.*;

/**
 * Контроллер для окна с очередью воспроизведения.
 * Аналогично {@link PlayerController}, связывает модель и внешнее представление.
 * Реализует {@link IPlaylistController}.
 */
public class PlaylistController implements IPlaylistController {
    private final IPlayer player;
    private final PlaylistGUI playlistGUI;

    /**
     * Конструктор контроллера очереди воспроизведения.
     * @param player модель плеера
     * @param gui окно очереди воспроизведения
     */
    public PlaylistController(IPlayer player, PlaylistGUI gui) {
        this.player = player;
        this.playlistGUI = gui;
        player.addPCL(gui);
    }

    /**
     * Обработчик изменения очереди воспроизведения.
     * Использует копию очереди воспроизведения для получения списка элементов, затем заполняет модель.
     */
    @Override
    public void handlePlaylistChange() {
        PlayQueue queue = (PlayQueue) player.getQueue().copy(); // создаём копию очереди
        DefaultListModel<IPlayable> listModel = new DefaultListModel<>();
        int itemsCounter = 0;
        while (queue.hasNext()) { // заполняем модель очереди элементами
            IPlayable playable = queue.next();
            listModel.addElement(playable);
            itemsCounter++;
        }
        playlistGUI.setListModel(listModel);
        playlistGUI.setItemsButtonText("Items : " + itemsCounter); // отображаем количество элементов очереди
    }

    /**
     * Обработчик выбора элемента очереди воспроизведения.
     * Формирует и отображает информацию о выбранном элементе.
     * @param playable выбранный элемент очереди
     */
    @Override
    public void handleSelectedItem(IPlayable playable) {
        String songData = "";
        songData += "Title: " + (playable.getTitle() == null ? "Unknown" : playable.getTitle()) + "\n";
        songData += "Artist: " + (playable.getArtist() == null? "Unknown" : playable.getArtist()) + "\n";
        songData += "Album: " + (playable.getAlbum() == null ? "Unknown" : playable.getAlbum()) + "\n";
        songData += "Year: " + (playable.getYear() == null ? "Unknown" : playable.getYear()) + "\n";
        songData += "Genre: " + (playable.getGenre() == null ? "Unknown" : playable.getGenre()) + "\n";
        songData += "Duration: " + String.format("%02d:%02d", (int)playable.getDurationMillis()/60, (int)playable.getDurationMillis()%60);
        playlistGUI.setSongDataText(songData);
    }
}

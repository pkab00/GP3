package org.gp3.controller;

import org.gp3.core.IPlayable;
import org.gp3.core.IPlayer;
import org.gp3.core.PlayQueue;
import org.gp3.gui.PlayQueueGUI;
import org.gp3.parsing.SongMetadata;

import javax.swing.*;

/**
 * Контроллер для окна с очередью воспроизведения.
 * Аналогично {@link PlayerController}, связывает модель и внешнее представление.
 * Реализует {@link IPlayQueueController}.
 */
public class PlayQueueController implements IPlayQueueController {
    private final IPlayer player;
    private final PlayQueueGUI playlistGUI;

    /**
     * Конструктор контроллера очереди воспроизведения.
     * @param player модель плеера
     * @param gui окно очереди воспроизведения
     */
    public PlayQueueController(IPlayer player, PlayQueueGUI gui) {
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
        playlistGUI.setItemButtonText("Items : " + itemsCounter); // отображаем количество элементов очереди
    }

    /**
     * Обработчик выбора элемента очереди воспроизведения.
     * Формирует и отображает информацию о выбранном элементе.
     * @param playable выбранный элемент очереди
     */
    @Override
    public void handleSelectedItem(IPlayable playable) {
        SongMetadata metadata = playable.getMetadata();
        playlistGUI.setSongDataText(metadata.toString());
    }
}

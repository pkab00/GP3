package org.amedia.controller;

import javax.swing.*;

import org.amedia.core.IMediaObservable;
import org.amedia.core.IPlayable;
import org.amedia.core.IPlayer;
import org.amedia.core.PlayQueue;
import org.amedia.gui.PlayQueueGUI;
import org.amedia.parsing.SongMetadata;

/**
 * Контроллер для окна с очередью воспроизведения.
 * Аналогично {@link PlayerController}, связывает модель и внешнее представление.
 * Реализует {@link IPlayQueueController}.
 */
public class PlayQueueController implements IPlayQueueController {
    private final IPlayer player;
    private final PlayQueueGUI playQueueGUI;

    /**
     * Конструктор контроллера очереди воспроизведения.
     * @param player модель плеера
     * @param gui окно очереди воспроизведения
     */
    public PlayQueueController(IPlayer player, PlayQueueGUI gui) {
        this.player = player;
        this.playQueueGUI = gui;
        ((IMediaObservable)player).addPCL(gui);
    }

    /**
     * Обработчик изменения очереди воспроизведения.
     * Использует копию очереди воспроизведения для получения списка элементов, затем заполняет модель.
     */
    @Override
    public void handleSongChange() {
        PlayQueue queue = (PlayQueue) player.getQueue().copy(); // создаём копию очереди
        DefaultListModel<IPlayable> listModel = new DefaultListModel<>();
        int itemsCounter = 0;
        while (queue.hasNext()) { // заполняем модель очереди элементами
            IPlayable playable = queue.next();
            listModel.addElement(playable);
            itemsCounter++;
        }
        playQueueGUI.setListModel(listModel);
        playQueueGUI.setItemButtonText("Items : " + itemsCounter); // отображаем количество элементов очереди
    }

    /**
     * Обработчик выбора элемента очереди воспроизведения.
     * Формирует и отображает информацию о выбранном элементе.
     * @param playable выбранный элемент очереди
     */
    @Override
    public void handleSelectedItem(IPlayable playable) {
        SongMetadata metadata = playable.getMetadata();
        playQueueGUI.setSongDataText(metadata.toString());
    }
}

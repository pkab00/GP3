package org.amedia.gui;

import javax.swing.*;

import org.amedia.controller.IPlayQueueController;
import org.amedia.controller.PlayQueueController;
import org.amedia.core.IMediaObservable;
import org.amedia.core.IPlayable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.BorderLayout;

/**
 * Класс GUI, отображающий очередь воспроизведения.
 * Не влияет на сам процесс воспроизведения, только визуально отображает очередь.
 * Взаимодействует с контроллером через интерфейс IPlaylistController.
 * @see IPlayQueueController
 */
public class PlayQueueGUI extends SuperGUI implements PropertyChangeListener {
    private JList<IPlayable> songList;
    private JTextArea songDataTextArea;
    private JPanel rootPanel;
    private JButton itemButton;

    private IPlayQueueController controller;

    /**
     * Конструктор по умолчанию.
     * Отображает окно с пустой информацией.
     */
    public PlayQueueGUI() {
        super("Playlist");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); 
        setContentPane(this.createPanel());
        setLocationRelativeTo(null);
    }

    private JPanel createPanel() {
        rootPanel = new JPanel(new BorderLayout());

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // устанавливаем font и текст панели
        songDataTextArea = new JTextArea();
        songDataTextArea.setText("SELECT ITEM TO SEE INFO");
        songDataTextArea.setEditable(false);
        songDataTextArea.setLineWrap(true);

        splitPane.setRightComponent(songDataTextArea);

        // устанавливаем список треков
        songList = new JList<>();
        songList.setForeground(new java.awt.Color(0xFFFFFF));
        songList.setBackground(new java.awt.Color(0x1faee9));
        songList.setSelectionBackground(new java.awt.Color(0x025669));
        songList.setSelectionForeground(new java.awt.Color(0xFFFFFF));
        JScrollPane scrollPane = new JScrollPane(songList);

        splitPane.setLeftComponent(scrollPane);
        rootPanel.add(splitPane, BorderLayout.CENTER);

        // добавляем кнопку
        itemButton = new JButton("Items: N/A");
        itemButton.setText("Items: N/A");
        itemButton.setFont(new java.awt.Font("Yu Gothic UI", 3, 18));
        itemButton.setRolloverEnabled(true);
        itemButton.setFocusPainted(false);
        itemButton.setHorizontalAlignment(JButton.CENTER);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(itemButton, BorderLayout.CENTER);

        rootPanel.add(buttonPanel, BorderLayout.SOUTH);

        return rootPanel;
    }  

    /**
     * Устанавливает контроллер.
     * Интерфейс IPlaylistController обеспечивает связь между контроллером и GUI.
     * @param controller объект контроллера
     */
    public void setController(IPlayQueueController controller) {
        this.controller = controller;
        songList.addListSelectionListener(evt -> {
            if (evt.getValueIsAdjusting()) {
                controller.handleSelectedItem(songList.getSelectedValue());
            }
        });
    }

    /**
     * Отображает экран.
     */
    public void showScreen() {
        setVisible(true);
    }

    /**
     * Устанавливает модель списка. Метод используется контроллером для обновления содержимого списка.
     * @param listModel новая модель списка
     * @see PlayQueueController#handleSongChange()
     */
    public void setListModel(DefaultListModel<IPlayable> listModel) {
        songList.setModel(listModel);
    }

    /**
     * Обновляет поле с информацией о текущем треке.
     * Используется контроллером при выборе нового трека.
     * @param infoText информация о текущем треке
     * @see PlayQueueController#handleSelectedItem(IPlayable)
     */
    public void setSongDataText(String infoText) {
        songDataTextArea.setText(infoText);
    }

    /**
     * Обновляет текст кнопки с количеством элементов в очереди воспроизведения.
     * @param text новый текст кнопки
     */
    public void setItemButtonText(String text) {
        itemButton.setText(text);
    }

    /**
     * Метод обработки события изменения свойства контроллера.
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch(propertyName) {
            case IMediaObservable.MediaEvents.SONG_CHANGE: // обновить listModel
                controller.handleSongChange(); break;
        }
    }
}

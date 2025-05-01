package org.gp3.gui;

import org.gp3.IPlayable;
import org.gp3.IPlaylistController;
import org.gp3.PlaylistController;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 * Класс GUI, отображающий очередь воспроизведения.
 * Не влияет на сам процесс воспроизведения, только визуально отображает очередь.
 * Взаимодействует с контроллером через интерфейс IPlaylistController.
 * 
 * @see IPlaylistController
 */
public class PlaylistGUI extends javax.swing.JFrame implements PropertyChangeListener {
    private JList<IPlayable> songList;
    private JTextArea songDataTextArea;
    private JPanel rootPanel;
    private JButton itemButton;

    private IPlaylistController controller;

    /**
     * Конструктор по умолчанию.
     * Отображает окно с пустой информацией.
     */
    public PlaylistGUI() {
        super("Playlist");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600); 
        setContentPane(this.createPanel());
        setLocationRelativeTo(null);
    }


    private JPanel createPanel() {
        rootPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // устанавливаем font и текст панели
        songDataTextArea = new JTextArea();
        songDataTextArea.setText("NO DATA");
        songDataTextArea.setFont(new java.awt.Font("Yu Gothic UI", 1, 18));
        songDataTextArea.setEditable(false);

        songDataTextArea.setLineWrap(true);
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        JScrollPane songDataScrollPane = new JScrollPane(songDataTextArea);
        rootPanel.add(songDataScrollPane, constraints);
        

        // устанавливаем лист
        songList = new JList<>();

        songList.setForeground(new java.awt.Color(0xFFFFFF));
        songList.setBackground(new java.awt.Color(0x1faee9));
        songList.setSelectionBackground(new java.awt.Color(0x025669));
        songList.setSelectionForeground(new java.awt.Color(0xFFFFFF));

       
        JScrollPane scrollPane = new JScrollPane(songList);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        rootPanel.add(scrollPane, constraints);

        // добавляем кнопку
        itemButton = new JButton("Items: N/A");

        itemButton = new JButton();
        itemButton.setText("Items: N/A");
        itemButton.setFont(new java.awt.Font("Yu Gothic UI", 3, 18));

        itemButton.setRolloverEnabled(true);
        itemButton.setFocusPainted(false);
        itemButton.setHorizontalAlignment(JButton.CENTER);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 0.0;
        rootPanel.add(itemButton, constraints);

        return rootPanel;
    }    
    /**
     * Устанавливает контроллер.
     * Интерфейс IPlaylistController обеспечивает связь между контроллером и GUI.
     * @param controller объект контроллера
     */
    public void setController(IPlaylistController controller) {
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
     * 
     * @see PlaylistController#handlePlaylistChange()
     */
    public void setListModel(DefaultListModel<IPlayable> listModel) {
        songList.setModel(listModel);
    }

    /**
     * Обновляет поле с информацией о текущем треке.
     * Используется контроллером при выборе нового трека.
     * 
     * @param infoText информация о текущем треке
     * @see PlaylistController#handleSelectedItem(IPlayable)
     */
    public void setSongDataText(String infoText) {
        songDataTextArea.setText(infoText);
    }

    /**
     * Обновляет текст кнопки с количеством элементов в очереди воспроизведения.
     * 
     * @param text новый текст кнопки
     */
    public void setItemButtonText(String text) {
        itemButton.setText(text);
    }

    /**
     * Метод обработки события изменения свойства контроллера.
     * 
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch(propertyName) {
            case "newSong": // обновить listModel
                controller.handlePlaylistChange(); break;
        }
    }
}

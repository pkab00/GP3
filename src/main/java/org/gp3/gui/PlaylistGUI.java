package org.gp3.gui;

import org.gp3.IPlayable;
import org.gp3.IPlaylistController;
import org.gp3.PlaylistController;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Класс GUI, отображающий очередь воспроизведения.
 * Не влияет на сам процесс воспроизведения, только визуально отображает очередь.
 * Взаимодействует с контроллером через интерфейс IPlaylistController.
 * @see IPlaylistController
 */
public class PlaylistGUI extends JFrame implements PropertyChangeListener {
    private JList<IPlayable> songsList;
    private JTextArea songDataTextArea;
    private JPanel rootPanel;
    private JButton itemsButton;

    private IPlaylistController controller;

    /**
     * Конструктор по умолчанию.
     * Отображает окно с пустой информацией.
     */
    public PlaylistGUI() {
        super("Playlist");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
    }

    /**
     * Устанавливает контроллер.
     * Интерфейс IPlaylistController обеспечивает связь между контроллером и GUI.
     * @param controller объект контроллера
     */
    public void setController(IPlaylistController controller) {
        this.controller = controller;
        songsList.addListSelectionListener(evt -> {
            if (evt.getValueIsAdjusting()) {
                controller.handleSelectedItem(songsList.getSelectedValue());
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
     * @see PlaylistController#handlePlaylistChange()
     */
    public void setListModel(DefaultListModel<IPlayable> listModel) {
        songsList.setModel(listModel);
    }

    /**
     * Обновляет поле с информацией о текущем треке.
     * Используется контроллером при выборе нового трека.
     * @param  infoText информация о текущем треке
     * @see PlaylistController#handleSelectedItem(IPlayable)
     */
    public void setSongDataText(String infoText) {
        songDataTextArea.setText(infoText);
    }

    /**
     * Обновляет текст кнопки с количеством элементов в очереди воспроизведения.
     * @param text новый текст кнопки
     */
    public void setItemsButtonText(String text) {
        itemsButton.setText(text);
    }

    /**
     * Метод обработки события изменения свойства контроллера.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch(propertyName) {
            case "newSong": // обновить listModel
                controller.handlePlaylistChange();
                break;
        }
    }
}

package org.gp3.gui;

import org.gp3.IPlayable;
import org.gp3.IPlaylistController;

import javax.swing.*;
import java.awt.*;
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
        setLocationRelativeTo(null);

        // Создание и настройка компонентов
        initComponents();
    }

    /**
     * Инициализация компонентов GUI.
     */
    private void initComponents() {
        // Основная панель с GridLayout (2 строки, 2 столбца)
        JPanel rootPanel = new JPanel(new GridLayout(2, 2, -1, -1));
        rootPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // JTextArea для отображения данных о песне
        songDataTextArea = new JTextArea("NO DATA");
        songDataTextArea.setBackground(new Color(0xE1E1E1)); // Цвет фонa
        songDataTextArea.setEditable(false);
        songDataTextArea.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        songDataTextArea.setForeground(Color.WHITE);
        songDataTextArea.setLineWrap(true);
        JPanel textAreaPanel = new JPanel(new BorderLayout());
        textAreaPanel.add(new JScrollPane(songDataTextArea), BorderLayout.CENTER);
        rootPanel.add(textAreaPanel);

        // JList для отображения списка песен
        songsList = new JList<>();
        songsList.setBackground(new Color(0xFEEDED)); // Цвет фона
        songsList.setForeground(Color.WHITE);
        songsList.setSelectionBackground(new Color(0xE1E1E1));
        songsList.setSelectionForeground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(songsList);
        rootPanel.add(scrollPane);

        // Кнопка для отображения количества элементов
        itemsButton = new JButton("Items: N/A");
        itemsButton.setEnabled(true);
        itemsButton.setFocusPainted(false);
        itemsButton.setFocusable(false);
        itemsButton.setFont(new Font("Yu Gothic UI", Font.PLAIN, 18));
        itemsButton.setHorizontalAlignment(SwingConstants.CENTER);
        itemsButton.setRolloverEnabled(true);
        rootPanel.add(itemsButton);

        // Установка основной панели в окно
        setContentPane(rootPanel);
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
     * @see org.gp3.PlaylistController#handlePlaylistChange()
     */
    public void setListModel(DefaultListModel<IPlayable> listModel) {
        songsList.setModel(listModel);
    }

    /**
     * Обновляет поле с информацией о текущем треке.
     * Используется контроллером при выборе нового трека.
     * @param infoText информация о текущем треке
     * @see org.gp3.PlaylistController#handleSelectedItem(IPlayable)
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
        if ("newSong".equals(propertyName)) {
            controller.handlePlaylistChange();
        }
    }
}

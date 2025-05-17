package org.amedia.gui;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.*;

import org.amedia.controller.IPlaylistController;

/**
 * Класс, представляющий графический интерфейс для управления плейлистами.
 * <p>
 * Содержит список плейлистов и зону отображения информации 
 * о текущем выбранном плейлисте. 

 */
public class PlaylistMenuGUI extends SuperGUI implements IPlaylistMenuGUI {
    private JList<String> playlistList;
    private JTextArea playlistDataZone;
    private JButton loadButton;
    private JButton cancelButton;
    
    public PlaylistMenuGUI() {
        super("Мои плейлисты");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        initComponents(); 
    }

    /**
     * Инициализирует графические компоненты.
     * Создает интерфейс, состоящий из двух частей:
     * - левая часть - список, отображающий названия
     *   плейлистов;
     * - правая часть - поле отображения информации
     *   о текущем выбранном плейлисте.
     */
    private void initComponents(){
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        playlistList = new JList<>();
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        splitPane.setLeftComponent(new JScrollPane(playlistList));
        
        playlistDataZone = new JTextArea(10, 10);
        playlistDataZone.setEditable(false);
        splitPane.setRightComponent(new JScrollPane(playlistDataZone));

        JPanel buttonPanel = new JPanel();
        loadButton = new JButton("Загрузить");
        loadButton.setEnabled(false);
        cancelButton = new JButton("Отмена");
        buttonPanel.add(loadButton);
        buttonPanel.add(cancelButton);
        
        add(buttonPanel, BorderLayout.SOUTH);

        add(splitPane);
    }

    /**
     * Устанавливает контроллер.
     * @param controller объект контроллера
     */
    public void setController(IPlaylistController controller){
        controller.handleAviablePlaylistsUpdate();
        playlistList.addListSelectionListener(e -> {controller.handleItemSelection(); loadButton.setEnabled(true);});
        loadButton.addActionListener(e -> controller.handleLoadingPlaylist());
        cancelButton.addActionListener(e -> dispose());
    }

    /**
     * Устанавливает список имён плейлистов, доступных 
     * для выбора. 
     * @param names список имён плейлистов
     */
    @Override
    public void updatePlaylistNames(List<String> playlistNames){
        DefaultListModel<String> model = new DefaultListModel<>();
        for(String name : playlistNames){
            model.addElement(name);
        }
        playlistList.setModel(model);
    }

    /**
     * Возвращает имя выбранного плейлиста или null,
     * если выбор не сделан.
     * @return имя выбранного плейлиста
     */
    @Override
    public String getSelectedPlaylistName(){
        return playlistList.getSelectedValue();
    }

    /**
     * Устанавливает текст, отображаемый в правой
     * части интерфейса, описывающий выбранный
     * плейлист.
     * @param data текст, отображаемый в правой
     *             части интерфейса
     */
    @Override
    public void updatePlaylistData(String data){
        playlistDataZone.setText(data);
    }

/**
 * Отображает окно с доступными плейлистами.
 */
    public void showScreen(){
        setVisible(true);
    }
}

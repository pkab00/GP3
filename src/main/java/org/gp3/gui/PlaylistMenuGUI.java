package org.gp3.gui;

import javax.swing.*;

/**
 * Класс, представляющий графический интерфейс для управления плейлистами.
 * <p>
 * Содержит список плейлистов и зону отображения информации 
 * о текущем выбранном плейлисте. 

 */
public class PlaylistMenuGUI extends JFrame {
    private JList<String> playlistList;
    private JTextArea playlistDataZone;
    
    public PlaylistMenuGUI() {
        setTitle("Мои плейлисты");
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

        add(splitPane);
    }

/**
 * Отображает окно с доступными плейлистами.
 */
    public void showScreen(){
        setVisible(true);
    }
}

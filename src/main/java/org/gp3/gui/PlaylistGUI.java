package org.gp3.gui;

import org.gp3.IPlayable;
import org.gp3.IPlaylistController;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

// TODO: ДОКУМЕНТИРОВАТЬ
public class PlaylistGUI extends JFrame implements PropertyChangeListener {
    private JList<IPlayable> songsList;
    private JTextArea songDataTextArea;
    private JPanel rootPanel;
    private JButton itemsButton;

    private IPlaylistController controller;

    public PlaylistGUI() {
        super("Playlist");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setContentPane(rootPanel);
        setLocationRelativeTo(null);
    }

    public void setController(IPlaylistController controller) {
        this.controller = controller;
        songsList.addListSelectionListener(evt -> {
            if (evt.getValueIsAdjusting()) {
                controller.handleSelectedItem(songsList.getSelectedValue());
            }
        });
    }

    public void showScreen() {
        setVisible(true);
    }

    public void setListModel(DefaultListModel<IPlayable> listModel) {
        songsList.setModel(listModel);
    }

    public void setSongDataText(String infoText) {
        songDataTextArea.setText(infoText);
    }

    public void setItemsButtonText(String text) {
        itemsButton.setText(text);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        switch(propertyName) {
            case "newSong": // update listModel
                controller.handlePlaylistChange();
                break;
        }
    }
}

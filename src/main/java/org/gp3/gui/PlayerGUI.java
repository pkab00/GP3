package org.gp3.gui;

import org.gp3.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;

/**
 * Графический интерфейс приложения, созданный с помощью GUI-формы.
 * <p>
 * <b>Перед использованием требует подключения контроллера, реализующего интерфейс {@link IController}.</b>
 */
public class PlayerGUI extends JFrame implements PropertyChangeListener {
    private JPanel rootPanel;
    private JButton aboutButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton fastBackwardButton;
    private JButton jumpForwardButton;
    private JButton playPauseButton;
    private JPanel playPanel;
    private JToolBar menuBar;
    private JLabel nowPlayingLabel;
    private JButton selectButton;

    private IController controller;

    /**
     * Создаёт новый экземпляр PlayerGUI.
     */
    public PlayerGUI() {
        super("GP3 Player GUI");
        setSize(750, 250);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(rootPanel);
        setVisible(true);
    }

    /**
     * Метод для установки контроллера.
     * @param controller объект {@link IController}
     */
    public void setController(IController controller) {
        this.controller = controller;
        previousButton.addActionListener(e -> controller.handlePrevious());
        nextButton.addActionListener(e -> controller.handleNext());
        fastBackwardButton.addActionListener(e -> controller.handleJumpBackward());
        jumpForwardButton.addActionListener(e -> controller.handleJumpForward());
        playPauseButton.addActionListener(e -> controller.handlePlay());
    }

    /**
     * Метод, обновляющий кнопку паузы при изменении состояния воспроизведения.
     * @param isPlaying текущее состояние воспроизведения.
     * {@code true} - если трек играет, {@code false} - в противном случае
     * @see #playPauseButton
     */
    private void updatePlayback(boolean isPlaying) {
        // удаляем все слушатели кнопки
        Arrays.stream(playPauseButton.getActionListeners()).forEach(playPauseButton::removeActionListener);

        // обновляем изображение и слушатель в соответствии с новым состоянием
        String icon = isPlaying ? "pause.png" : "play.png";
        ActionListener listener = isPlaying ? (e -> controller.handlePause()) : (e -> controller.handlePlay());
        playPauseButton.setIcon(new ImageIcon(getClass().getResource("/icons/"+icon)));
        playPauseButton.addActionListener(listener);
    }

    private void updateSongLabel(IPlayable song) {
        nowPlayingLabel.setText("<html><div style=\"text-align: center;\"><b>"
                +song.getTitle()+"</b><br>"+song.getArtist()+"</div></html>");
    }

    /**
     * Метод, ответственный за реакцию на изменение отслеживаемых параметров плсеера.
     * @param evt A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     * @see IPlayer источник уведомлений об изменении
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();

        switch (propertyName) {
            case "playing":
                updatePlayback((boolean) evt.getNewValue());
                break;
            case "newSong":
                updateSongLabel((IPlayable) evt.getNewValue());
        }
    }
}

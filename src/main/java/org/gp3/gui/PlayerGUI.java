package org.gp3.gui;

import org.gp3.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Map;

/**
 * Графический интерфейс приложения, созданный с помощью GUI-формы.
 * <p>
 * <b>Перед использованием требует подключения контроллера, реализующего интерфейс {@link IController}.</b>
 */
public class PlayerGUI extends JFrame implements PropertyChangeListener {
    private JPanel rootPanel;
    private JButton infoButton;
    private JButton previousButton;
    private JButton nextButton;
    private JButton fastBackwardButton;
    private JButton jumpForwardButton;
    private JButton playPauseButton;
    private JPanel playPanel;
    private JToolBar menuBar;
    private JLabel nowPlayingLabel;
    private JButton selectFilesButton;
    private JSlider songSlider;
    private JLabel leftTimeLabel;
    private JLabel rightTimeLabel;
    private JButton playModeButton;
    private JButton playlistButton;

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
        songSlider.setValue(0);
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
        songSlider.addMouseListener(new MouseAdapter() {
            private int lastPosition = 0;
            @Override public void mousePressed(MouseEvent e) {lastPosition = songSlider.getValue();}
            @Override public void mouseReleased(MouseEvent e) {controller.handleSongSlider(songSlider.getValue(), lastPosition);}
        });
        playModeButton.addActionListener(e -> controller.handlePlayMode());
        selectFilesButton.addActionListener(e -> controller.handleFilesSelection());
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
        ActionListener listener = isPlaying ? (e -> controller.handlePause()) : (e -> controller.handleResume());
        playPauseButton.setIcon(new ImageIcon(getClass().getResource("/icons/"+icon)));
        playPauseButton.addActionListener(listener);
    }

    /**
     * Обновление метки с названием песни.
     * В случае, если данные не указаны, {@code title = "NO TITLE"} и {@code artist = "UNKNOWN"}
     * @param song текущая воспроизводимая песня
     */
    private void updateSongLabel(IPlayable song) {
        String title = song.getTitle().isBlank() ? "NO TITLE" : song.getTitle();
        String artist = song.getArtist().isBlank() ? "UNKNOWN" : song.getArtist();

        nowPlayingLabel.setText(String.format("<html><div style=\"text-align: center;\">" +
                "<b>%s</b><br>%s</div></html>", title, artist));
    }

    /**
     * Обновление временных меток.
     * @param leftValue текущая позиция воспроизведения
     * @param rightValue оставшееся время
     */
    private void updateTimeLabels(int leftValue, int rightValue) {
        String leftTime = String.format("%02d:%02d", leftValue/60, leftValue%60);
        String rightTime = String.format("-%02d:%02d", rightValue/60, rightValue%60);

        leftTimeLabel.setText(leftTime);
        rightTimeLabel.setText(rightTime);
    }

    /**
     * Обновление слайдера с позицией воспроизведения.
     * @param position позиция воспроизведения
     * @param duration длительность текущего трека
     */
    private void updateSongSlider(int position, int duration) {
        if(position == 0) songSlider.setMaximum(duration);
        songSlider.setValue(position);
    }

    /**
     * Обновление кнопки режима воспроизведения.
     * @param playMode новый режим воспроизведения
     */
    private void updatePlayModeButton(PlayMode playMode){
        final Map<Class<? extends PlayMode>, String> modeMap = Map.of(
                DefaultMode.class, "repeat_off",
                RepeatAllMode.class, "repeat_all",
                RepeatOneMode.class, "repeat_one"
        );

        String desiredFileName = modeMap.get(playMode.getClass());
        playModeButton.setIcon(new ImageIcon(getClass().getResource("/icons/"+desiredFileName+".png")));
        playModeButton.setPressedIcon(new ImageIcon(getClass().getResource("/icons/"+desiredFileName+"_prs.png")));
    }

    /**
     * Метод, ответственный за реакцию на изменение отслеживаемых параметров плеера.
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
                break;
            case "progress":
                Object position = evt.getOldValue();
                Object duration = evt.getNewValue();
                if (duration != null && position != null){
                    updateTimeLabels((int) position, (int) duration);
                    updateSongSlider((int) position, (int) duration);
                }
                break;
            case "playMode":
                PlayMode playMode = (PlayMode) evt.getNewValue();
                updatePlayModeButton(playMode);
        }
    }
}

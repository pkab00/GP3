package org.amedia.gui;

import javax.swing.*;

import org.amedia.controller.IPlayerController;
import org.amedia.core.DefaultMode;
import org.amedia.core.IPlayable;
import org.amedia.core.IPlayer;
import org.amedia.core.PlayMode;
import org.amedia.core.RepeatAllMode;
import org.amedia.core.RepeatOneMode;
import org.amedia.core.IMediaObservable.MediaEvents;
import org.amedia.parsing.SongMetadata;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

/**
 * Графический интерфейс приложения, созданный с помощью Swing API.
 * <p>
 * <b>Перед использованием требует подключения контроллера, реализующего интерфейс {@link IPlayerController}.</b>
 */
public class PlayerGUI extends SuperGUI implements PropertyChangeListener {
    private JButton previousButton;
    private JButton nextButton;
    private JButton fastBackwardButton;
    private JButton jumpForwardButton;
    private JButton playPauseButton;
    private JLabel nowPlayingLabel;
    private JSlider songSlider;
    private JLabel leftTimeLabel;
    private JLabel rightTimeLabel;
    private JButton playModeButton;

    private JMenuItem infoItem;
    private JMenuItem selectFilesItem;
    private JMenuItem playQueueItem;
    private JMenuItem saveAsPlaylistItem;
    private JMenuItem openPlaylistMenuItem;

    private IPlayerController controller;

    /**
     * Создаёт новый экземпляр PlayerGUI.
     */
    public PlayerGUI() {
        super("[a]media");
        setSize(750, 300);
        setResizable(false);

        // Инициализация компонентов
        initComponents();
        setVisible(true);
        lockInterface(true);
    }

    /**
     * Впомагательный метод загрузки медиа. Возвращает изображение иконки.
     * @param iconName имя иконки
     * @return изображение иконки
     */
    private Icon getIcon(String iconName) {
        return new ImageIcon("src\\main\\resources\\icons\\" + iconName + ".png");
    }

    /**
     * Инициализация компонентов GUI.
     */
    private void initComponents() {
        // Основная панель с GridBagLayout
        JPanel rootPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(5, 5, 5, 5); // Отступы

        // Метка "Сейчас играет"
        nowPlayingLabel = new JLabel("PRESS \"PLAY\" BUTTON", SwingConstants.CENTER);
        nowPlayingLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 24));
        nowPlayingLabel.setOpaque(true);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        rootPanel.add(nowPlayingLabel, gbc);

        // Слайдер воспроизведения
        songSlider = new JSlider();
        songSlider.setPaintTicks(true);
        songSlider.setPaintTrack(true);
        songSlider.setValue(0);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 4;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        rootPanel.add(songSlider, gbc);

        // Метка времени слева
        leftTimeLabel = new JLabel("00:00", SwingConstants.LEFT);
        leftTimeLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 18));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        rootPanel.add(leftTimeLabel, gbc);

        // Метка времени справа
        rightTimeLabel = new JLabel("-00:00", SwingConstants.RIGHT);
        rightTimeLabel.setFont(new Font("Yu Gothic UI", Font.PLAIN, 18));
        gbc.gridx = 4;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;
        rootPanel.add(rightTimeLabel, gbc);

        // Панель для нижнего ряда кнопок
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbcButton = new GridBagConstraints();
        gbcButton.fill = GridBagConstraints.NONE;
        gbcButton.insets = new Insets(5, 5, 5, 5); // Отступы

        // Кнопка "Предыдущий трек"
        gbcButton.gridx = 0;
        gbcButton.gridy = 0;
        gbcButton.weightx = 0.0; // Не растягиваем по горизонтали
        previousButton = new JButton(getIcon("previous"));
        previousButton.setBorderPainted(false);
        previousButton.setContentAreaFilled(false);
        previousButton.setFocusPainted(false);
        previousButton.setPressedIcon(getIcon("previous_prs"));
        previousButton.setToolTipText("To previous song");
        buttonPanel.add(previousButton, gbcButton);

        // Кнопка "Перемотка назад"
        gbcButton.gridx = 1;
        fastBackwardButton = new JButton(getIcon("backward"));
        fastBackwardButton.setBorderPainted(false);
        fastBackwardButton.setContentAreaFilled(false);
        fastBackwardButton.setFocusPainted(false);
        fastBackwardButton.setPressedIcon(getIcon("backward_prs"));
        fastBackwardButton.setToolTipText("Jump back (-10 seconds)");
        buttonPanel.add(fastBackwardButton, gbcButton);

        // Кнопка "Play/Pause"
        gbcButton.gridx = 2;
        playPauseButton = new JButton(getIcon("play"));
        playPauseButton.setBorderPainted(false);
        playPauseButton.setContentAreaFilled(false);
        playPauseButton.setFocusPainted(false);
        playPauseButton.setPressedIcon(getIcon("pause_prs"));
        playPauseButton.setToolTipText("Play/Pause");
        buttonPanel.add(playPauseButton, gbcButton);

        // Кнопка "Перемотка вперед"
        gbcButton.gridx = 3;
        jumpForwardButton = new JButton(getIcon("forward"));
        jumpForwardButton.setBorderPainted(false);
        jumpForwardButton.setContentAreaFilled(false);
        jumpForwardButton.setFocusPainted(false);
        jumpForwardButton.setPressedIcon(getIcon("forward_prs"));
        jumpForwardButton.setToolTipText("Jump forward (+10 seconds)");
        buttonPanel.add(jumpForwardButton, gbcButton);

        // Кнопка "Следующий трек"
        gbcButton.gridx = 4;
        nextButton = new JButton(getIcon("next"));
        nextButton.setBorderPainted(false);
        nextButton.setContentAreaFilled(false);
        nextButton.setFocusPainted(false);
        nextButton.setPressedIcon(getIcon("next_prs"));
        nextButton.setToolTipText("To next song");
        buttonPanel.add(nextButton, gbcButton);

        // Кнопка смены режима воспроизведения
        gbcButton.gridx = 5;
        playModeButton = new JButton(getIcon("repeat_off"));
        playModeButton.setBorderPainted(false);
        playModeButton.setContentAreaFilled(false);
        playModeButton.setFocusPainted(false);
        playModeButton.setPressedIcon(getIcon("repeat_off_prs"));
        playModeButton.setToolTipText("Change play mode");
        buttonPanel.add(playModeButton, gbcButton);

        // Добавляем нижний ряд кнопок в основную панель
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0; // Растягиваем по горизонтали
        gbc.weighty = 0.0; // Не растягиваем по вертикали
        gbc.fill = GridBagConstraints.HORIZONTAL; // Растягиваем только по горизонтали
        rootPanel.add(buttonPanel, gbc);

        // Установка основной панели в окно
        setContentPane(rootPanel);

        // Инициализация меню
        initMenu();
    }

    /**
     * Инициализация меню приложения.
     */
    private void initMenu() {
        // Панель меню
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setBorderPainted(true);

        // Раздел меню "Справка"
        JMenu aboutMenu = new JMenu("Справка");

        // Кнопка "О программе"
        infoItem = new JMenuItem("О программе");
        infoItem.setBorderPainted(false);
        infoItem.setContentAreaFilled(false);
        infoItem.setFocusPainted(false);
        infoItem.setOpaque(false);
        aboutMenu.add(infoItem);

        menuBar.add(aboutMenu);

        // Раздел меню "Управление воспроизведением"
        JMenu playControlMenu = new JMenu("Воспроизведение");

        // Кнопка "Выбрать файлы"
        selectFilesItem = new JMenuItem("Выбрать файлы");
        selectFilesItem.setBorderPainted(false);
        selectFilesItem.setContentAreaFilled(false);
        selectFilesItem.setFocusPainted(false);
        selectFilesItem.setOpaque(false);
        playControlMenu.add(selectFilesItem);

        // Кнопка "Очередь воспроизведения"
        playQueueItem = new JMenuItem("Очередь");
        playQueueItem.setBorderPainted(false);
        playQueueItem.setContentAreaFilled(false);
        playQueueItem.setFocusPainted(false);
        playControlMenu.add(playQueueItem);

        menuBar.add(playControlMenu);

        // Меню управления плейлистами
        JMenu playlistMenu = new JMenu("Плейлист");

        // Кнопка "Меню управления плейлистами"
        openPlaylistMenuItem = new JMenuItem("Меню управления плейлистами");
        openPlaylistMenuItem.setBorderPainted(false);
        openPlaylistMenuItem.setContentAreaFilled(false);
        openPlaylistMenuItem.setFocusPainted(false);
        playlistMenu.add(openPlaylistMenuItem);

        // Кнопка "Сохранить как плейлист"
        saveAsPlaylistItem = new JMenuItem("Сохранить как плейлист");
        saveAsPlaylistItem.setBorderPainted(false);
        saveAsPlaylistItem.setContentAreaFilled(false);
        saveAsPlaylistItem.setFocusPainted(false);
        playlistMenu.add(saveAsPlaylistItem);
        
        menuBar.add(playlistMenu);
        setJMenuBar(menuBar);
    }

    /**
     * Метод для установки контроллера.
     * @param controller объект {@link IPlayerController}
     */
    public void setController(IPlayerController controller) {
        this.controller = controller;
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                controller.handleExit();
                dispose();
                System.exit(0);
            }
        });
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
        selectFilesItem.addActionListener(e -> {
            MusicFileChooser chooser = new MusicFileChooser(); // выводим диалог выбора файлов
            chooser.showDialog(this);
            File[] files = chooser.getSelectedFiles(); // получаем выбранные файлы
            if(files.length != 0) { // если файлы выбраны
                controller.handleFilesSelection(files); // передаем их контроллеру
            }});
        infoItem.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "[a]media music player\nVersion: 2.0.0\nDeveloped by vbshkn\n2025",
                    "[a]media", JOptionPane.INFORMATION_MESSAGE);
        });
        playQueueItem.addActionListener(e -> controller.handlePlayQueueView()); // выводим окно просмотра плейлиста
        saveAsPlaylistItem.addActionListener(e -> controller.handleSaveAsPlaylist());
        openPlaylistMenuItem.addActionListener(e -> controller.handleOpenPlaylistGUI());
    }

    /**
     * Устанавливает/снимает блокировку с кнопок управления воспроизведением.
     * @param lock логическое значение блокировки
     *             {@code true} - если нужно заблокировать,
     *             {@code false} - если нужно разблокировать
     */
    private void lockInterface(boolean lock) {
        playPauseButton.setEnabled(!lock);
        previousButton.setEnabled(!lock);
        nextButton.setEnabled(!lock);
        fastBackwardButton.setEnabled(!lock);
        jumpForwardButton.setEnabled(!lock);
        playModeButton.setEnabled(!lock);
        playQueueItem.setEnabled(!lock);
        songSlider.setEnabled(!lock);
        saveAsPlaylistItem.setEnabled(!lock);
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
        String icon = isPlaying ? "pause" : "play";
        ActionListener listener = isPlaying ? (e -> controller.handlePause()) : (e -> controller.handleResume());
        playPauseButton.setIcon(getIcon(icon));
        playPauseButton.addActionListener(listener);
    }

    /**
     * Обновление метки с названием песни.
     * В случае, если данные не указаны, {@code title = "NO TITLE"} и {@code artist = "UNKNOWN"}
     * @param song текущая воспроизводимая песня
     */
    private void updateSongLabel(IPlayable song) {
        SongMetadata metadata = song.getMetadata();
        String title = metadata.title();
        String artist = metadata.artist();

        nowPlayingLabel.setText(String.format("<html><div style=\"text-align: center;\">" +
                "<b>%s</b><br>%s</div></html>", title, artist));
    }

    /**
     * Обновление временных метки.
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
        playModeButton.setIcon(getIcon(desiredFileName));
        playModeButton.setPressedIcon(getIcon(desiredFileName + "_prs"));
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
            case MediaEvents.PLAY_CHANGE:
                updatePlayback((boolean) evt.getNewValue());
                break;
            case MediaEvents.SONG_CHANGE:
                updateSongLabel((IPlayable) evt.getNewValue());
                break;
            case MediaEvents.PROGRESS_CHANGE:
                Object position = evt.getOldValue();
                Object duration = evt.getNewValue();
                if (duration != null && position != null){
                    updateTimeLabels((int) position, (int) duration);
                    updateSongSlider((int) position, (int) duration);
                }
                break;
            case MediaEvents.PLAY_MODE_CHANGE:
                PlayMode playMode = (PlayMode) evt.getNewValue();
                updatePlayModeButton(playMode);
                break;
            case MediaEvents.PLAYLIST_CHANGE:
                int numOfSongs = (Integer)evt.getNewValue();
                if(numOfSongs > 0) lockInterface(false);
                break;
            default:
                System.out.println("Unsupported property change called: "+evt.getPropertyName());
        }
    }
}

package org.amedia.gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * Класс, представляющий базовый графический интерфейс приложения.
 * Устанавливает общую для всех интерфейсов иконку.
 * @param title название окна
 */
public class SuperGUI extends JFrame{
    protected SuperGUI(String title) {
        super(title);
        setIconImage(Toolkit.getDefaultToolkit().getImage("src\\main\\resources\\icons\\icon.png"));
    }
}

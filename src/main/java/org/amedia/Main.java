package org.amedia;

import org.amedia.controller.PlayerController;
import org.amedia.core.IPlayer;
import org.amedia.core.VLCJPlayer;
import org.amedia.gui.PlayerGUI;

public class Main {
    private static void setUpPlayer(){
        PlayerGUI gui = new PlayerGUI();
        IPlayer player = new VLCJPlayer(gui);
        PlayerController controller = new PlayerController(player);
        gui.setController(controller);
    }

    public static void main(String[] args) {
        setUpPlayer();
    }
}

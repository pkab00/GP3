package org.gp3;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;

public class Test {
    public static void main(String[] args) {
        System.setProperty("jna.library.path", "src/main/resources/vlc/bin");
        System.setProperty("VLC_PLUGIN_PATH", "src/main/resources/vlc/plugins");
        
        MediaPlayer player = new MediaPlayerFactory().mediaPlayers().newMediaPlayer();
        player.media().play("C:\\Users\\Vadim Bushukin\\Music\\아이들\\2\\07 Rollie.flac");
        try{
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        player.release();
    }
}

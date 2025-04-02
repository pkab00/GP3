package org.gp3;

public interface PlayerInterface {
    void play();
    void pause();
    void resume();
    void rewind();
    void stop();
    void toNext();
    void toPrevious();

    void setVolume(int volume);
    void setLooping(boolean looping);
    void setPosition(int position);

    int getVolume();
    boolean isLooping();
    int getPosition();
}

package org.gp3;

public interface PlayerInterface {
    void play();
    void pause();
    void resume();
    void rewind();
    void stop();
    void toNext();
    void toPrevious();

    void setVolume(double volume);
    void setLooping(boolean looping);

    double getVolume();
    boolean isLooping();
}

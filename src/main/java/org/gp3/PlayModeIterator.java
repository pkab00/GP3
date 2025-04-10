package org.gp3;

import java.util.List;

public class PlayModeIterator implements CycledIterator<PlayMode>{
    private final List<PlayMode> behaviours;
    int position = -1;

    public PlayModeIterator(List<PlayMode> behaviours){
        this.behaviours = behaviours;
    }

    @Override
    public boolean hasNext() {
        return position < behaviours.size() - 1;
    }

    @Override
    public PlayMode next() {
        if(!hasNext()){
            position = -1;
        }
        return behaviours.get(++position);
    }
}

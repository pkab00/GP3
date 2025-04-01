package org.gp3;

import java.util.ArrayList;

public class PlayQueue implements TwoWayIterator<Playable>{
    private final ArrayList<Playable> lst;
    private int position = 0;

    public PlayQueue(ArrayList<Playable> lst) {
        this.lst = lst;
    }

    @Override
    public boolean hasNext() {
        return position < lst.size();
    }

    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public Playable next() {
        if(hasNext()){
            return lst.get(position++);
        }
        return null;
    }

    @Override
    public Playable previous() {
        if(hasPrevious()){
            return lst.get(--position);
        }
        return null;
    }
}

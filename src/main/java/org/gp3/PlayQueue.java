package org.gp3;

import java.util.ArrayList;

public class PlayQueue implements TwoWayIterator<Playable>{
    private final ArrayList<Playable> lst;
    private int position = -1;

    public PlayQueue(ArrayList<Playable> lst) {
        this.lst = lst;
    }

    @Override
    public boolean hasNext() {
        return position < lst.size() - 1;
    }

    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public Playable next() {
        if(!hasNext()) return null;
        return lst.get(++position);
    }

    @Override
    public Playable previous() {
        if(!hasPrevious()) return null;
        return lst.get(--position);
    }
}

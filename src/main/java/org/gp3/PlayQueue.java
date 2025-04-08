package org.gp3;

import java.util.ArrayList;

public class PlayQueue implements TwoWayIterator<IPlayable>{
    private final ArrayList<IPlayable> lst;
    private int position = -1;

    public PlayQueue(ArrayList<IPlayable> lst) {
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
    public IPlayable next() {
        if(!hasNext()) return null;
        return lst.get(++position);
    }

    @Override
    public IPlayable previous() {
        if(!hasPrevious()) return null;
        return lst.get(--position);
    }
}

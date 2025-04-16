package org.gp3;

public interface TwoWayIterator<T> {
    boolean hasNext();
    boolean hasPrevious();
    T next();
    T previous();
    TwoWayIterator<T> copy();
}

package org.amedia.utils;

public interface TwoWayIterator<T> {
    boolean hasNext();
    boolean hasPrevious();
    T next();
    T previous();
    TwoWayIterator<T> copy();
}

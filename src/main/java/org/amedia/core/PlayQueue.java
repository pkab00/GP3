package org.amedia.core;

import java.util.ArrayList;

import org.amedia.utils.TwoWayIterator;

/**
 * Очередь воспроизведения аудиозаписей.
 * Реализует интерфейс двунаправленного итератора {@link TwoWayIterator}.
 */
public class PlayQueue implements TwoWayIterator<IPlayable>{
    private final ArrayList<IPlayable> lst;
    private int position = -1; // текущая позиция

    /**
     * Конструктор по умолчанию.
     * @param lst список аудиозаписей
     */
    public PlayQueue(ArrayList<IPlayable> lst) {
        this.lst = lst;
    }

    /**
     * Закрытый конструктор, используемый для копирования объекта
     * @param lst список аудиозаписей исходного объекта
     * @param position текущая позиция исходного объекта
     */
    private PlayQueue(ArrayList<IPlayable> lst, int position) {
        this.lst = lst;
        this.position = position;
    }

    /**
     * Проверка наличия следующего элемента.
     * @return {@code true} если следующий элемент существует, {@code false} иначе.
     */
    @Override
    public boolean hasNext() {
        return position < lst.size() - 1;
    }

    /**
     * Проверка наличия предыдущего элемента.
     * @return {@code true} если предыдущий элемент существует, {@code false} иначе.
     */
    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    /**
     * Возвращает следующий элемент.
     * @return следующий элемент, если он существует, или {@code null} иначе.
     */
    @Override
    public IPlayable next() {
        if(!hasNext()) return null;
        return lst.get(++position);
    }

    /**
     * Возвращает предыдущий элемент.
     * @return предыдущий элемент, если он существует, или {@code null} иначе.
     */
    @Override
    public IPlayable previous() {
        if(!hasPrevious()) return null;
        return lst.get(--position);
    }

    /**
     * Создание копии текущего объекта для безопасной итерации без изменения текущей позиции.
     * @return копия текущего объекта
     */
    @Override
    public PlayQueue copy() {
        return new PlayQueue(lst, position);
    }

    /**
     * Создание копии текущего объекта для безопасной итерации, но
     * начиная с начала очереди.
     * @return копия текущего объекта
     */
    public PlayQueue copyFromFirst(){
        return new PlayQueue(lst, -1);
    }
}

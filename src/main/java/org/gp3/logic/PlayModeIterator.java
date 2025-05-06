package org.gp3.logic;

import java.util.List;

import org.gp3.utils.CycledIterator;

/**
 * Итератор для переключения режимов воспроизведения.
 * Реализует интерфейс циклического итератора {@link CycledIterator}.
 */
public class PlayModeIterator implements CycledIterator<PlayMode>{
    private final List<PlayMode> behaviours; // список режимов воспроизведения
    int position = -1; // начинаем с -1 чтобы в первый раз было с первого элемента

    /**
     * Инициализация итератора.
     * @param behaviours список режимов воспроизведения.
     */
    public PlayModeIterator(List<PlayMode> behaviours){
        this.behaviours = behaviours;
    }

    /**
     * Возвращает true, если есть следующий элемент.
     * @return {@code true} если есть следующий элемент, иначе {@code false}
     */
    @Override
    public boolean hasNext() {
        return position < behaviours.size() - 1;
    }

    /**
     * Возвращает следующий элемент.
     * Если достигли последнего, начинает сначала.
     * @return следующий элемент
     */
    @Override
    public PlayMode next() {
        if(!hasNext()){
            position = -1;
        }
        return behaviours.get(++position);
    }
}

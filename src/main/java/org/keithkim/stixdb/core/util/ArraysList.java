package org.keithkim.stixdb.core.util;

import java.util.*;
import java.util.ArrayList;
import java.util.function.UnaryOperator;

import static java.util.Arrays.asList;

public class ArraysList<E>  extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

    private static final int DEFAULT_CAPACITY = 10;
    private static final int INITIAL_MAX_ARRAY_SIZE = 0x40000000;

    private final List<Object[]> arrays;
    private final List<List<Object>> lists;

    public static <E> ArraysList<E> of(E... source) {
        if (source == null) {
            ArraysList arraysList = new ArraysList(1);
            arraysList.set(0, null);
            return arraysList;
        }
        ArraysList<E> arraysList = new ArraysList<>(source.length);
        if (source.length > 0) {
            System.arraycopy(source, 0, arraysList.arrays.get(0), 0, source.length);
        }
        return arraysList;
    }

    public ArraysList() {
        this(DEFAULT_CAPACITY);
    }

    public ArraysList(ArraysList<E>... sources) {
        arrays = new ArrayList<>();
        lists = new ArrayList<>();

        for (ArraysList<E> source : sources) {
            for (Object[] array : source.arrays) {
                arrays.add(array);
                lists.add(asList(array));
            }
        }
    }

    public ArraysList(int size) {
        arrays = new ArrayList<>();
        lists = new ArrayList<>();

        while (size > 0) {
            int size1 = size >= INITIAL_MAX_ARRAY_SIZE ? INITIAL_MAX_ARRAY_SIZE : size;

            Object[] array = new Object[size1];
            arrays.add(array);
            lists.add(asList(array));

            size -= size1;
        }
    }

    @Override
    public E get(int index) {
        for (Object[] array : arrays) {
            if (index < array.length) {
                return (E) array[index];
            }
            index -= array.length;
        }
        throw new IndexOutOfBoundsException();
    }

    public int indexOf(Object o) {
        int offset = 0;
        for (List<Object> list : lists) {
            int index = list.indexOf(o);
            if (index >= 0) {
                return index;
            }
            offset += list.size();
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int offset = size();
        for (int i = lists.size() - 1; i >= 0; i--) {
            List<Object> list = lists.get(i);
            offset -= list.size();
            int index = list.lastIndexOf(o);
            if (index >= 0) {
                return offset + index;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        long size = 0;
        for (Object[] array : arrays) {
            size += array.length;
        }
        return size <= Integer.MAX_VALUE ? (int) size : Integer.MAX_VALUE;
    }

    @Override
    public E set(int index, E element) {
        for (Object[] array : arrays) {
            if (index < array.length) {
                E old = (E) array[index];
                array[index] = element;
                return old;
            }
            index -= array.length;
        }
        throw new IndexOutOfBoundsException();
    }

    // Unimplemented for now...

    @Override
    public ListIterator<E> listIterator()   {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof List))
            return false;

        Iterator<E> e1 = iterator();
        Iterator<?> e2 = ((List<?>) o).iterator();
        while (e1.hasNext() && e2.hasNext()) {
            E o1 = e1.next();
            Object o2 = e2.next();
            if (!(o1==null ? o2==null : o1.equals(o2)))
                return false;
        }
        return e1.hasNext() == e2.hasNext();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("[");
        String sep = "";
        for (Object[] array : arrays) {
            for (Object o : array) {
                buf.append(sep);
                if (o instanceof String) {
                    buf.append('"');
                    buf.append(((String) o).replace("\"", "\\\""));
                    buf.append('"');
                } else if (o instanceof Character) {
                    buf.append('\'');
                    buf.append(o);
                    buf.append('\'');
                } else {
                    buf.append(o);
                }
                sep = ", ";
            }
        }
        buf.append(']');
        return buf.toString();
    }

    // Unsupported mutating operations

    @Override
    public void add(int index, E element) {
        throw new UnsupportedOperationException();
    }
    @Override
    public E remove(int index) {
        throw new UnsupportedOperationException();
    }
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }
    @Override
    public void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }
}
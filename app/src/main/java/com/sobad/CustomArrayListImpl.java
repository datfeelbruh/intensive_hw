package com.sobad;

import java.util.*;

public class CustomArrayListImpl<E> extends AbstractList<E> implements List<E>{
    private Object[] elements;
    private int size;
    private static final int DEFAULT_SIZE = 10;

    CustomArrayListImpl() {
        this.elements = new Object[DEFAULT_SIZE];
    }

    CustomArrayListImpl(int initialSize) {
        if (initialSize > 0) {
            this.elements = new Object[initialSize];
        } else {
            throw new RuntimeException("Invalid initial size");
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean add(E e) {
        if (size == elements.length) {
            enlarge();
        }
        elements[size++] = e;
        return true;
    }

    @Override
    public void clear() {
        this.elements = new Object[this.size];
        size = 0;
    }

    @Override
    public E get(int index) {
        return (E) this.elements[index];
    }

    @Override
    public void add(int index, E element) {
        checkIndex(index);
        if (size + 1 == elements.length) {
            enlarge();
        }
        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        Object[] incomeCollection = c.toArray();
        int incomeCollectionSize = incomeCollection.length;
        if (incomeCollectionSize == 0) {
            return false;
        }
        if (incomeCollectionSize + this.size > elements.length) {
            enlarge(incomeCollectionSize);
        }
        System.arraycopy(incomeCollection, 0, elements, size, incomeCollectionSize);
        size = this.size + incomeCollectionSize;
        return true;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if (o.equals(elements[i])) {
                remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public E remove(int index) {
        checkIndex(index);
        Object[] data = elements;
        E element = get(index);
        if (index + 1 == size) {
            elements[index] = null;
        } else {
            System.arraycopy(data, index + 1, elements, index, size - 1);
        }
        size--;
        return element;
    }

    @Override
    public void sort(Comparator<? super E> c) {
        quickSort(elements, 0, size - 1, c);
    }


    private void enlarge() {
        Object[] oldData = elements;
        Object[] enlargedData = new Object[elements.length * 2];
        System.arraycopy(oldData, 0, enlargedData, 0, oldData.length);
        elements = enlargedData;
    }

    private void enlarge(int length) {
        Object[] oldData = elements;
        Object[] enlargedData = new Object[(elements.length + length) * 2];
        System.arraycopy(oldData, 0, enlargedData, 0, oldData.length);
        elements = enlargedData;
    }

    private void checkIndex(int index) {
        if (index < 0 || index > elements.length) {
            throw new RuntimeException("Invalid index");
        }
    }

    private void quickSort(Object[] elements, int from, int to, Comparator<? super E> c) {
        if (from < to) {
            int divideIndex = partition(elements, from, to, c);
            quickSort(elements, from, divideIndex - 1, c);
            quickSort(elements, divideIndex, to, c);
        }
    }

    private int partition(Object[] elements, int from, int to, Comparator<? super E> c) {
        int rightIndex = to;
        int leftIndex = from;

        E pivot = (E) elements[from];

        while (leftIndex <= rightIndex) {
            while (c.compare((E) elements[leftIndex], pivot) < 0) {
                leftIndex++;
            }

            while (c.compare((E) elements[rightIndex], pivot) > 0) {
                rightIndex--;
            }

            if (leftIndex <= rightIndex) {
                E temp = (E) elements[rightIndex];
                elements[rightIndex] = elements[leftIndex];
                elements[leftIndex] = temp;
                leftIndex++;
                rightIndex--;
            }
        }
        return leftIndex;
    }
}

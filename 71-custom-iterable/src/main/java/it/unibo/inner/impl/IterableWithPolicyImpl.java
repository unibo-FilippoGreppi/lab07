package it.unibo.inner.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

import it.unibo.inner.api.IterableWithPolicy;
import it.unibo.inner.api.Predicate;

public class IterableWithPolicyImpl<T> implements IterableWithPolicy<T> {
    private T[] elements;
    private Predicate<T> filter;

    public IterableWithPolicyImpl(T[] elements, Predicate<T> filter) {
        this.elements = Arrays.copyOf(elements, elements.length);
        this.setIterationPolicy(filter);
    }

    public IterableWithPolicyImpl(T[] elements) {
        this(
            elements,
            new Predicate<T>() {
                @Override
                public boolean test(T elem) {
                    return true;
                }
            }
        );
    }

    @Override
    public void setIterationPolicy(Predicate<T> filter) {
        this.filter = filter;
    }
    
    private class Iterable implements Iterator<T> {
        private int currentElement = 0;

        @Override
        public boolean hasNext() {
            while (this.currentElement < elements.length) {
                if(filter.test(elements[currentElement])) {
                    return true;
                }
                currentElement++;
            }
           return false;
        }

        @Override
        public T next() {
            if(this.hasNext()) {
                return elements[currentElement++]; 
            }
            throw new NoSuchElementException();
        }
    }

    public Iterable iterator() {
        return new Iterable();
    }
}
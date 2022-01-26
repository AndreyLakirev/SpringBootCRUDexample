package com.lakirev.util.model;

import java.util.Iterator;

public class CustomLinkedList<T> implements Iterable<T> {
    private int size = 0;

    public int size() {
        return size;
    }

    private Node<T> first;
    private Node<T> last;

    public boolean isEmpty() {
        return size() == 0;
    }

    public void add(T data) {
        Node<T> node = new Node<>(data);
        if (first == null) {
            first = node;
            size++;
            return;
        }
        if (last == null) {
            last = node;
            last.previous = first;
            first.next = last;
            size++;
            return;
        }
        node.previous = last;
        last.next = node;
        last = node;
        size++;
    }

    public void add(T data, int index) {
        Node<T> right = getNode(index);
        Node<T> target = new Node<>(data);
        if (right.previous == null) {
            target.next = right;
            right.previous = target;
            first = target;
        } else {
            Node<T> left = right.previous;
            left.next = target;
            right.previous = target;
            target.previous = left;
            target.next = right;
        }
        size++;
    }

    public T get(int index) {
        return getNode(index).data;
    }

    public T getLast() {
        return last.data;
    }

    public T getFirst() {
        return first.data;
    }

    private Node<T> getNode(int index) {
        checkForIndex(index);
        Node<T> node;
        boolean reversedDirection = false;
        if (index < size / 2 + size % 2) {
            node = first;
        } else {
            reversedDirection = true;
            index = size - index - 1;
            node = last;
        }
        int startIndex = 0;
        while (startIndex != index) {
            startIndex++;
            if (reversedDirection) {
                node = node.previous;
            } else {
                node = node.next;
            }
        }
        return node;
    }

    private void set(Node<T> target, int index) {
        target = new Node<>(target.data);
        Node<T> old = getNode(index);
        Node<T> left = old.previous;
        Node<T> right = old.next;
        if (left == null) {
            first = target;
            target.next = right;
            right.previous = target;
        } else if (right == null) {
            last = target;
            target.previous = left;
            left.next = target;
        } else {
            target.previous = left;
            target.next = right;
            left.next = target;
            right.previous = target;
        }
    }

    public void swap(int index1, int index2) {
        Node<T> node1 = getNode(index1);
        set(getNode(index2), index1);
        set(node1, index2);
    }

    private void checkForIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Node<T> node = first;
        stringBuilder.append(node);
        while (node.next != null) {
            node = node.next;
            stringBuilder.append(", ");
            stringBuilder.append(node);
        }
        return stringBuilder.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new RisingIterator(first);
    }

    private class RisingIterator implements Iterator<T> {
        private Node<T> current;

        RisingIterator(Node<T> startNode) {
            this.current = startNode;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            T data = current.data;
            current = current.next;
            return data;
        }
    }

    private static class Node<T> {
        private Node<T> next;
        private Node<T> previous;
        private final T data;

        public Node(T data) {
            this.data = data;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }
}

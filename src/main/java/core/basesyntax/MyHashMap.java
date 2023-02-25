package core.basesyntax;

import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final float LOAD_FACTOR = 0.75f;
    private static final int DEFAULT_CAPACITY = 16;
    private Node<K, V>[] table;
    private int size;
    private int threshold;

    public MyHashMap() {
        table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
        threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
    }

    @Override
    public void put(K key, V value) {
        putValue(hash(key), key, value);
    }

    @Override
    public V getValue(K key) {
        int pos = hash(key);
        Node<K, V> node = table[pos];
        while (node != null) {
            if (Objects.equals(key, node.key)) {
                return node.value;
            }
            node = node.next;
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putValue(int index, K key, V value) {
        if (table == null || size > threshold) {
            resize();
        }

        if (table[index] == null) {
            table[index] = new Node<>(key, value, null);
            size++;
            return;
        }

        Node<K, V> curr = table[index];
        while (curr != null) {
            if (Objects.equals(key, curr.key)) {
                curr.value = value;
                return;
            }

            if (curr.next == null) {
                curr.next = new Node<>(key, value, null);
                size++;
                return;
            }
            curr = curr.next;
        }
    }

    private void resize() {
        final Node<K, V>[] oldTab = table;
        table = new Node[table.length << 1];
        threshold = (int) (table.length * LOAD_FACTOR);
        size = 0;
        for (Node<K, V> node : oldTab) {
            while (node != null) {
                put(node.key, node.value);
                node = node.next;
            }
        }
    }

    private int hash(K key) {
        return (key == null) ? 0 : Math.abs(key.hashCode() % table.length);
    }

    private static class Node<K, V> {
        private final K key;
        private V value;
        private Node<K, V> next;

        public Node(K key, V value, Node<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}

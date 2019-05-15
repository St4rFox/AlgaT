package algat.hashtable;

import algat.controller.ViewerController;

import java.util.Iterator;
import java.util.Optional;

public class HashTable implements Iterable<Optional<HashTable.HashTableNode>> {
    private int capacity;
    private HashTableNode[] elements;
    private Hasher hasher;
    private final int step = 1;
    private ViewerController viewer;

    public HashTable(int capacity, Hasher hasher) {
        this.capacity = capacity;
        this.elements = new HashTableNode[capacity];
        this.hasher = hasher;
    }

    public void setViewer(ViewerController viewer) {
        this.viewer = viewer;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean contains(String key) {
        HashTableNode node = this.elements[this.scan(key)];
        return node != null && !node.deleted && node.key.equals(key);
    }

    public void put(String key, String value) {
        int idx = this.scan(key);
        HashTableNode node = this.elements[idx];

        if (node == null)
            this.elements[idx] = new HashTableNode(key, value);
        else if (node.deleted || node.key.equals(key)) {
            node.key = key;
            node.value = value;
        } else {
            //TODO: restructure table
        }
    }

    public String get(String key) {
        HashTableNode node = this.elements[this.scan(key)];

        if (node != null && !node.deleted)
            return node.value;
        else
            return null;
    }

    public String remove(String key) {
        int idx = this.scan(key);
        HashTableNode node = this.elements[idx];

        if (node != null && node.key.equals(key)) {
            node.deleted = true;
            return node.key;
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");

        for (int i = 0; i < this.capacity; i++) {
            HashTableNode current = this.elements[i];
            builder.append(String.format("\t%d: %s,\n", i, current == null ? "EMPTY" : (current.deleted ? "DELETED" : current.key)));
        }

        builder.append("}");
        return builder.toString();
    }

    @Override
    public Iterator<Optional<HashTableNode>> iterator() {
        return new HashTableIterator();
    }

    private int scan(String key) {
        int start = this.hasher.hash(key, this.capacity);
        int position = start;
        int deletedPosition = this.capacity;
        boolean deletedFound = false;

        for (int i = 0; i < this.capacity; i++) {
            HashTableNode current = this.elements[position];
            if (current == null || current.key.equals(key))
                break;

            if (current.deleted && !deletedFound) {
                deletedFound = true;
                deletedPosition = position;
            }

            position = (start + i * step) % this.capacity;
        }

        if (deletedFound && !this.elements[position].key.equals(key))
            position = deletedPosition;

        return position;
    }

    public class HashTableNode {
        private String key;
        private String value;
        private boolean deleted;

        HashTableNode(String key, String value) {
            this.key = key;
            this.value = value;
            this.deleted = false;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }
    }

    private class HashTableIterator implements Iterator<Optional<HashTableNode>> {
        private int cursor = -1;

        @Override
        public boolean hasNext() {
            return cursor + 1 < HashTable.this.capacity;
        }

        @Override
        public Optional<HashTableNode> next() {
            return Optional.ofNullable(HashTable.this.elements[++this.cursor]);
        }
    }
}

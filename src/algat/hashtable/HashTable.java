package algat.hashtable;

import algat.controller.HashTableDelegate;

import java.util.Iterator;

public class HashTable implements Iterable<HashTable.HashTableNode> {
    private int capacity;
    private HashTableNode[] elements;
    private Hasher hasher;
    private final int step = 1;
    public HashTableDelegate delegate;

    public HashTable(int capacity, Hasher hasher) {
        this.capacity = capacity;
        this.elements = new HashTableNode[capacity];
        this.hasher = hasher;
    }

    public int size() {
        return capacity;
    }

    public boolean contains(String key) {
        ScanTuple result = this.scan(key);
        HashTableNode node = result.node;
        return node != null && !node.deleted && node.key.equals(key);
    }

    public void put(String key, String value) {
        ScanTuple result = this.scan(key);
        HashTableNode node = result.node;

        if (node == null)
            this.elements[result.position] = new HashTableNode(key, value);
        else if (node.deleted || node.key.equals(key)) {
            node.key = key;
            node.value = value;
        } else {
            //TODO: restructure table
        }
    }

    public String get(String key) {
        ScanTuple result = this.scan(key);
        HashTableNode node = result.node;

        if (node != null && !node.deleted)
            return node.value;
        else
            return null;
    }

    public String remove(String key) {
        ScanTuple result = this.scan(key);
        HashTableNode node = result.node;

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
    public Iterator<HashTableNode> iterator() {
        return new HashTableIterator();
    }

    private ScanTuple scan(String key) {
        int start = this.hasher.hash(key, this.capacity);
        int position = start;
        int deletedPosition = this.capacity;
        boolean deletedFound = false;

        boolean hasDelegate = this.delegate != null;
        if (hasDelegate) delegate.onHashComputation(start);

        for (int i = 0; i < this.capacity; i++) {
            HashTableNode current = this.elements[position];
            if (hasDelegate) this.delegate.onNodeInspection(position, current);

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

        ScanTuple result = new ScanTuple(position, this.elements[position]);
        if (hasDelegate) this.delegate.onNodeInspection(result.position, result.node);
        return result;
    }

    public class HashTableNode {
        private String key;
        private String value;
        private boolean deleted = false;

        HashTableNode() {}

        HashTableNode(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String getKey() {
            return this.key;
        }

        public boolean isDeleted() {
            return this.deleted;
        }

        @Override
        public String toString() {
            return this.key + " => " + this.value;
        }
    }

    private class ScanTuple {
        int position;
        HashTableNode node;

        ScanTuple(int position, HashTableNode node) {
            this.position = position;
            this.node = node;
        }
    }

    private class HashTableIterator implements Iterator<HashTableNode> {
        private int cursor = -1;

        @Override
        public boolean hasNext() {
            return cursor + 1 < HashTable.this.capacity;
        }

        @Override
        public HashTableNode next() {
            HashTableNode node = HashTable.this.elements[++this.cursor];
            return node == null ? new HashTableNode() : node;
        }
    }
}

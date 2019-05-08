package algat.hashtable;

public class InternalHashTable implements IHashTable {
    private final int step = 1;
    private int capacity;
    private InternalHashTableNode[] elements;
    private Hasher hasher;

    public InternalHashTable(int capacity, Hasher hasher) {
        this.capacity = capacity;
        this.elements = new InternalHashTableNode[capacity];
        this.hasher = hasher;
    }

    @Override
    public boolean contains(String key) {
        InternalHashTableNode node = this.elements[this.scan(key)];
        return node != null && !node.deleted && node.key.equals(key);
    }

    @Override
    public void put(String key, String value) {
        int idx = this.scan(key);
        InternalHashTableNode node = this.elements[idx];

        if (node == null)
            this.elements[idx] = new InternalHashTableNode(key, value);
        else if (node.deleted || node.key.equals(key)) {
            node.key = key;
            node.value = value;
        } else {
            //TODO: restructure table
        }
    }

    @Override
    public String remove(String key) {
        int idx = this.scan(key);
        InternalHashTableNode node = this.elements[idx];

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
            InternalHashTableNode current = this.elements[i];
            builder.append(String.format("\t%d: %s,\n", i, current == null ? "EMPTY" : (current.deleted ? "DELETED" : current.key)));
        }

        builder.append("}");
        return builder.toString();
    }

    private int scan(String key) {
        int start = this.hasher.hash(key, this.capacity);
        int position = start;
        int deletedPosition = this.capacity;
        boolean deletedFound = false;

        for (int i = 0; i < this.capacity; i++) {
            InternalHashTableNode current = this.elements[position];
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

    private class InternalHashTableNode extends HashTableNode {
        private boolean deleted;

        InternalHashTableNode(String key, String value) {
            super(key, value);
            this.deleted = false;
        }
    }
}

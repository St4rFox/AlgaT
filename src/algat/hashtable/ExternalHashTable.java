package algat.hashtable;

import java.util.LinkedList;

public class ExternalHashTable implements IHashTable {
    private int capacity;
    private LinkedList<HashTableNode>[] lists;
    private Hasher hasher;

    @SuppressWarnings("unchecked")
    public ExternalHashTable(int capacity, Hasher hasher) {
        this.capacity = capacity;
        this.lists = new LinkedList[capacity];

        for (int i = 0; i < capacity; i++)
            this.lists[i] = new LinkedList<>();

        this.hasher = hasher;
    }

    @Override
    public boolean contains(String key) {
        return this.scan(key).node != null;
    }

    @Override
    public void put(String key, String value) {
        ScanTuple st = this.scan(key);

        if (st.node != null)
            st.node.value = value;
        else
            this.lists[st.index].add(new HashTableNode(key, value));
    }

    @Override
    public String remove(String key) {
        ScanTuple st = this.scan(key);

        if (st.node != null) {
            String value = st.node.value;
            this.lists[st.index].remove(st.node);
            return value;
        }

        return null;
    }

    private ScanTuple scan(String key) {
        int idx = this.hasher.hash(key, this.capacity);
        LinkedList<HashTableNode> list = this.lists[idx];

        for (HashTableNode node : list) {
            if (node.key.equals(key))
                return new ScanTuple(idx, node);
        }

        return new ScanTuple(idx, null);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");

        for (int i = 0; i < this.capacity; i++) {
                String key = this.lists[i].isEmpty() ? "EMPTY" : this.lists[i].getFirst().key;
                builder.append(String.format("\t%d: %s => %s,\n", i, key, this.valuesAt(i)));
        }

        builder.append("}");
        return builder.toString();
    }

    private LinkedList<String> valuesAt(int idx) {
        LinkedList<String> values = new LinkedList<>();

        for (HashTableNode node : this.lists[idx]) {
            values.add(node.value);
        }

        return values;
    }

    private class ScanTuple {
        int index;
        HashTableNode node;

        ScanTuple(int index, HashTableNode node) {
            this.index = index;
            this.node = node;
        }
    }
}

package algat.lib.scanmethods;

import algat.lib.hashtable.Hasher;

public class DoubleHashingScanMethod extends ScanMethod {
    private Hasher hasher;
    private String key;

    public DoubleHashingScanMethod(int capacity, Hasher hasher, String key) {
        super(capacity);
        this.hasher = hasher;
        this.key = key;
    }

    @Override
    public int[] getScanSequence(int from) {
        int[] sequence = new int[capacity];

        for (int i = 0; i < capacity; i++)
            sequence[i] = (from + i * hasher.hash(key, capacity)) % capacity;

        return sequence;
    }

    @Override
    public String toString() {
        return "Hashing Doppio";
    }
}

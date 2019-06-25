package algat.lib;

public abstract class ScanMethods {
    public enum Names {
        LINEAR("Scansione Lineare"),
        QUADRATIC("Scansione Quadratica"),
        RANDOM("Scansione Pseudocasuale"),
        DOUBLE_HASHING("Hashing Doppio");

        private String name;
        Names(String name) { this.name = name; }
    }

    public static int[] linear(int from, int capacity, int step) {
        int[] sequence = new int[capacity];

        for (int i = 0; i < capacity; i++)
            sequence[i] = (from + (i * step)) % capacity;

        return sequence;
    }

    public static int[] quadratic(int from, int capacity, int step) {
        int[] sequence = new int[capacity];

        for (int i = 0; i * i < capacity; i++)
            sequence[i] = (from + (i * i * step)) % capacity;

        return sequence;
    }

    public static int[] random(int from, int capacity) {
        int[] sequence = Util.getShuffledRange(capacity);

        for (int i = 0; i < capacity; i++)
            sequence[i] = (from + sequence[i]) % capacity;

        return sequence;
    }

    public static int[] doubleHashing(int firstHash, int capacity, int secondHash) {
        int[] sequence = new int[capacity];

        for (int i = 0; i < capacity; i++)
            sequence[i] = (firstHash + i * secondHash) % capacity;

        return sequence;
    }

}

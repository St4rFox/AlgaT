package algat.lib.scanmethods;

public class QuadraticScanMethod extends ScanMethod {
    private int step;

    public QuadraticScanMethod(int capacity, int step) {
        super(capacity);
        this.step = step;
    }

    @Override
    public int[] getScanSequence(int from) {
        int[] sequence = new int[capacity];

        for (int i = 0; i * i < capacity; i++)
            sequence[i] = (from + (i * i * step)) % capacity;

        return sequence;
    }

    @Override
    public String toString() {
        return "Scansione Quadratica";
    }
}

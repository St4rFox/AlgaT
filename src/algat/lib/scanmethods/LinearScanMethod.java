package algat.lib.scanmethods;

public class LinearScanMethod extends ScanMethod {
    private int step;

    public LinearScanMethod(int capacity, int step) {
        super(capacity);
        this.step = step;
    }

    @Override
    public int[] getScanSequence(int from) {
        int[] sequence = new int[capacity];

        for (int i = 0; i < capacity; i++)
            sequence[i] = (from + (i * step)) % capacity;

        return sequence;
    }

    @Override
    public String toString() {
        return "Scansione Lineare";
    }
}

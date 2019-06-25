package algat.lib.scanmethods;

public abstract class ScanMethod {
    int capacity;

    ScanMethod(int capacity) { this.capacity = capacity; }

    public abstract int[] getScanSequence(int from);
}

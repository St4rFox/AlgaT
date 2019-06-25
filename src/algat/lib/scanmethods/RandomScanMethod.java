package algat.lib.scanmethods;

import algat.lib.Util;

public class RandomScanMethod extends ScanMethod {

    public RandomScanMethod(int capacity) {
        super(capacity);
    }

    @Override
    public int[] getScanSequence(int from) {
        int[] sequence = Util.getShuffledRange(capacity);

        for (int i = 0; i < capacity; i++)
            sequence[i] = (from + sequence[i]) % capacity;

        return sequence;
    }

    @Override
    public String toString() {
        return "Scansione Pseudocasuale";
    }
}

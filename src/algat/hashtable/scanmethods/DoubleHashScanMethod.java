package algat.hashtable.scanmethods;

import algat.hashtable.Hasher;

public class DoubleHashScanMethod implements ScanMethod {

    private String key;
    private int modul;
    private Hasher hasher;

    public DoubleHashScanMethod(Hasher hasher) {
        this.hasher = hasher;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setModul(int modul) {
        this.modul = modul;
    }

    @Override
    public int nextIndex(int i) {
        return this.hasher.hash(this.key, this.modul) * i;
    }

    @Override
    public String toString() {
        return "Hashing Doppio";
    }
}

package algat.hashtable.scanmethods;

public class RandomScanMethod implements ScanMethod {

    @Override
    public int nextIndex(int i) {
        int randomNumber = (int)Math.round(Math.random() * 10);
        return i + randomNumber;
    }

    @Override
    public String toString() {
        return "Scansione Pseudocasuale";
    }
}

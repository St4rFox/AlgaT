package algat.hashtable.scanmethods;

public class LinearScanMethod implements ScanMethod{

    private int step;

    LinearScanMethod(int step){
        this.step = step;
    }

    @Override
    public int nextIndex(int i) {
        return i * this.step;
    }

    @Override
    public String toString() {
        return "Scansione Lineare";
    }
}

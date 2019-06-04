package algat.lib.scanmethods;

public class LinearScanMethod implements ScanMethod{

    private int step;

    public LinearScanMethod(int step){
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

package algat.lib.scanmethods;

public class QuadraticScanMethod implements ScanMethod {

    private int step;

    public QuadraticScanMethod(int step) {
        this.step = step;
    }

    @Override
    public int nextIndex(int i) {
        if(i < 2) return 2;
        else {
            return i * i * step;
        }
    }

    @Override
    public String toString() {
        return "Scansione Quadratica";
    }
}

package algat.lib.scanmethods;

public class LinearScanMethod implements ScanMethod{

    private int step;

<<<<<<< HEAD:src/algat/hashtable/scanmethods/LinearScanMethod.java
    public void setStep(int step){
=======
    public LinearScanMethod(int step){
>>>>>>> d2a669a9f69c738b4ff857283aee80db55797a6c:src/algat/lib/scanmethods/LinearScanMethod.java
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

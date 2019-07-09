package algat.lib;

public enum ScanMethod {
    LINEAR("Scansione Lineare"),
    QUADRATIC("Scansione Quadratica"),
    DOUBLE_HASHING("Hashing Doppio");

    private String name;
    ScanMethod(String name) { this.name = name; }
}

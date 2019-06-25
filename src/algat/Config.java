package algat;

import algat.lib.hashtable.Hasher;
import algat.lib.scanmethods.ScanMethod;

public class Config {
    private static Config configInstance = new Config();
    public static Integer[] DEFAULT_CAPACITIES = {10, 20, 30, 40, 50};

    // Config GETTERS
    public static int getCapacity() {
        return configInstance.capacity;
    }

    public static Hasher getHasher() {
        return configInstance.hasher;
    }

    public static ScanMethod getScanMethod() {
        return configInstance.scanMethod;
    }


    // Config SETTERS
    public static void setCapacity(int capacity) {
        configInstance.capacity = capacity;
    }

    public static void setHasher(Hasher hasher) {
        configInstance.hasher = hasher;
    }

    public static void setScanMethod(ScanMethod scanMethod) {
        configInstance.scanMethod = scanMethod;
    }

    public static void setScanSequence(int[] scanSequence) {
        configInstance.scanSequence = scanSequence;
    }

    private int capacity;
    private Hasher hasher;
    private ScanMethod scanMethod;
    private int[] scanSequence;

    private Config() {
    }
}

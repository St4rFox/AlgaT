package algat;

import algat.lib.hashtable.Hasher;
import algat.lib.scanmethods.ScanMethod;

public class Config {
    private static Config configInstance = new Config();

    public final static Integer[] TABLE_CAPACITIES = {10, 20, 30, 40, 50};

    public static Config get() { return configInstance; }

    public static int getTableCapacity() { return configInstance.tableCapacity; }
    public static Hasher getHashFunction() { return configInstance.hashFunction; }
    public static void setTableCapacity(int tableCapacity) { configInstance.tableCapacity = getNearestCapacity(tableCapacity); }
    public static void setHashFunction(Hasher hashFunction) {
        configInstance.hashFunction = hashFunction;
    }
    private static int getNearestCapacity(int tableCapacity) {
        int tenth = 10;

        while (tenth < tableCapacity)
            tenth += 10;

        return tenth;
    }

    public static void setScanMethod(ScanMethod scanMethod) { configInstance.scanMethod = scanMethod; }

    private int tableCapacity;
    private Hasher hashFunction;
    private ScanMethod scanMethod;

    private Config() {
    }

    @Override
    public String toString() {
        return "{\n\ttableCapacity: " + this.tableCapacity + ",\n\t" + "hashingFunction: " + this.hashFunction + "\n}";
    }
}

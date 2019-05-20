package algat;

import algat.hashtable.Hasher;

public class Config {
    private static Config configInstance = new Config();

    public final static Integer[] TABLE_CAPACITIES = {10, 20, 30, 40, 50};

    public static Config get() { return configInstance; }

    public static void setTableCapacity(int tableCapacity) {
        configInstance.tableCapacity = tableCapacity;
    }

    public static void setHashFunction(Hasher hashFunction) {
        configInstance.hashFunction = hashFunction;
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

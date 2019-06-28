package algat;

import algat.lib.ScanMethod;
import algat.lib.hashtable.Hasher;

import java.util.HashMap;

public class Config {
    private HashMap<Key, Object> storage = new HashMap<>();

    public enum Key {
        CAPACITY,
        HASHER,
        SCAN_METHOD,
        STEP,
        SECOND_HASHER
    }

    public void set(Key key, Object value) {
        if (hasCorrectType(key, value))
            storage.put(key, value);
    }

    public int getInt(Key key) {
        return (int) storage.get(key);
    }

    public Hasher getHasher(Key key) {
        return (Hasher) storage.get(key);
    }

    public ScanMethod getScanMethod(Key key) {
        return (ScanMethod) storage.get(key);
    }

    private boolean hasCorrectType(Key key, Object value) {
        boolean hasCorrectType = false;

        switch (key) {
            case CAPACITY:
            case STEP:
                hasCorrectType = value instanceof Integer;
                break;
            case HASHER:
            case SECOND_HASHER:
                hasCorrectType = value instanceof Hasher;
                break;
            case SCAN_METHOD:
                hasCorrectType = value instanceof ScanMethod;
                break;
        }

        return hasCorrectType;
    }
}

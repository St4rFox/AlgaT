package algat.hashtable;

public interface IHashTable {
    boolean contains(String key);
    void put(String key, String value);
    String remove(String key);
}

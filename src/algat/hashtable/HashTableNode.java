package algat.hashtable;

public class HashTableNode {
    String key;
    String value;
    boolean deleted = false;

    HashTableNode() {}

    HashTableNode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public String getKey() {
        return this.key;
    }

    public boolean isDeleted() {
        return this.deleted;
    }

    @Override
    public String toString() {
        return this.key + " => " + this.value;
    }
}
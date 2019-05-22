package algat.controller;

import algat.hashtable.HashTable;

public interface HashTableDelegate {
    void onHashComputation(int hashValue);
    void onNodeInspection(int index, HashTable.HashTableNode node);
}

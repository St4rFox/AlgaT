package algat.controller;

import algat.hashtable.HashTableNode;

public interface HashTableDelegate {
    void onHashCreated(int hashValue);
    void onScan(int index, HashTableNode node);
    void onFinish(int index, HashTableNode selectedNode);
}

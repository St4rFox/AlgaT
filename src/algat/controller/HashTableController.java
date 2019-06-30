package algat.controller;

import algat.Config;
import algat.lib.NotEnoughSpaceException;
import algat.lib.ScanMethod;
import algat.lib.Util;
import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class HashTableController {
    @FXML private GridPane bucketsContainer;

    private int capacity;
    private Config config;
    private ArrayList<Bucket> buckets = new ArrayList<>();
    private int[] probeSequence;

    void init(Config config, List<Bucket> data) {
        if (data == null) {
            int capacity = config.getInt(Config.Key.CAPACITY);
            for (int i = 0; i < capacity; i++)
                buckets.add(new Bucket());
        } else {
            buckets = new ArrayList<>(data);
        }

        setConfig(config);
    }

    void setConfig(Config config) {
        if (this.config == null || !this.config.equals(config)) {
            this.config = config;
            this.capacity = config.getInt(Config.Key.CAPACITY);
            this.probeSequence = new int[capacity];
            this.buildTable();
        }
    }

    private void buildTable() {
        bucketsContainer.getChildren().clear();

        for (int i = 0; i < capacity; i++) {
            Bucket bucket = buckets.get(i);
            bucketsContainer.addRow(i, new BucketComponent(bucket));
        }
    }

    void insert(String key, String value) {
        int probeIndex = this.probe(key);

        if (probeIndex == capacity)
            throw new NotEnoughSpaceException("Maximum table capacity (=" + capacity + ") was reached");

        System.out.println(probeSequence[probeIndex]);
        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        selectedBucket.setKey(key);
        selectedBucket.setValue(value);

        if (selectedBucket.isDeleted())
            selectedBucket.setDeleted(false);
    }

    void remove(String key) {
        int probeIndex = this.probe(key);
        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        selectedBucket.setDeleted(true);
    }

    boolean hasKey(String key) {
        int probeIndex = this.probe(key);
        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        return !selectedBucket.isEmpty() && !selectedBucket.isDeleted() && selectedBucket.getKey().equals(key);
    }

    private int probe(String key) {
        this.createSequence(key);
        boolean deletedFound = false;
        int bucketIndex = 0;
        int deletedIndex = -1;

        while (bucketIndex < capacity) {
            Bucket probedBucket = buckets.get(probeSequence[bucketIndex]);
            String bucketKey = probedBucket.getKey();

            if (probedBucket.isEmpty() || bucketKey.equals(key))
                break;

            if (probedBucket.isDeleted() && !deletedFound) {
                deletedFound = true;
                deletedIndex = bucketIndex;
            }

            bucketIndex++;
        }

        if (deletedFound && !buckets.get(bucketIndex % capacity).getKey().equals(key))
            bucketIndex = deletedIndex;

        return bucketIndex;
    }

    private void createSequence(String key) {
        Hasher hasher = this.config.getHasher(Config.Key.HASHER);
        ScanMethod scanMethod = this.config.getScanMethod(Config.Key.SCAN_METHOD);
        int hash = hasher.hash(key, capacity);

        switch (scanMethod) {
            case LINEAR:
                this.linear(hash);
                break;
            case QUADRATIC:
                this.quadratic(hash);
                break;
            case RANDOM:
                this.random(hash);
                break;
            case DOUBLE_HASHING:
                this.doubleHashing(hash, key);
                break;
        }
    }

    // =================================
    // === Scan Methods ================
    // =================================
    private void linear(int hash) {
        int step = this.config.getInt(Config.Key.STEP);

        for (int i = 0; i < capacity; i++)
            probeSequence[i] = (hash + (i * step)) % capacity;
    }

    private void quadratic(int hash) {
        int step = this.config.getInt(Config.Key.STEP);

        for (int i = 0; i * i < capacity; i++)
            probeSequence[i] = (hash + (i * i * step)) % capacity;
    }

    private void random(int hash) {
        int[] sequence = Util.getShuffledRange(capacity);

        for (int i = 0; i < capacity; i++)
            probeSequence[i] = (hash + sequence[i]) % capacity;

    }

    private void doubleHashing(int hash, String key) {
        Hasher secondHasher = this.config.getHasher(Config.Key.SECOND_HASHER);
        int step = secondHasher.hash(key, capacity);

        for (int i = 0; i < capacity; i++)
            probeSequence[i] = (hash + i * step) % capacity;
    }
}

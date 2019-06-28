package algat.controller;

import algat.Config;
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
    private int[] scanSequence;

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
        this.config = config;
        this.capacity = config.getInt(Config.Key.CAPACITY);
        this.scanSequence = new int[capacity];
        this.buildTable();
    }

    private void buildTable() {
        bucketsContainer.getChildren().clear();

        for (int i = 0; i < capacity; i++) {
            Bucket bucket = buckets.get(i);
            bucketsContainer.addRow(i, new BucketComponent(bucket));
        }
    }

    void insert(String key, String value) {
        this.createSequence(key);
    }

    void remove(String key) {
        this.createSequence(key);
    }

    void hasKey(String key) {
        this.createSequence(key);
    }

    private void createSequence(String key) {
        Hasher hasher = this.config.getHasher(Config.Key.HASHER);
        ScanMethod scanMethod = this.config.getScanMethod(Config.Key.SCAN_METHOD);
        int hash = hasher.hash(key, capacity);

        switch (scanMethod) {
            case LINEAR:
                this.linear(hash);
            case QUADRATIC:
                this.quadratic(hash);
            case RANDOM:
                this.random(hash);
            case DOUBLE_HASHING:
                this.doubleHashing(hash, key);
        }
    }

    // =================================
    // === Scan Methods ================
    // =================================
    private void linear(int hash) {
        int step = this.config.getInt(Config.Key.STEP);

        for (int i = 0; i < capacity; i++)
            scanSequence[i] = (hash + (i * step)) % capacity;
    }

    private void quadratic(int hash) {
        int step = this.config.getInt(Config.Key.STEP);

        for (int i = 0; i * i < capacity; i++)
            scanSequence[i] = (hash + (i * i * step)) % capacity;
    }

    private void random(int hash) {
        int[] sequence = Util.getShuffledRange(capacity);

        for (int i = 0; i < capacity; i++)
            scanSequence[i] = (hash + sequence[i]) % capacity;

    }

    private void doubleHashing(int hash, String key) {
        Hasher secondHasher = this.config.getHasher(Config.Key.SECOND_HASHER);
        int step = secondHasher.hash(key, capacity);

        for (int i = 0; i < capacity; i++)
            scanSequence[i] = (hash + i * step) % capacity;
    }
}

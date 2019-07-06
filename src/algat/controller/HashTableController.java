package algat.controller;

import algat.Config;
import algat.lib.ErrorCodes;
import algat.lib.ProbeAnimation;
import algat.lib.ScanMethod;
import algat.lib.Util;
import algat.lib.exceptions.NoSuchKeyException;
import algat.lib.hashtable.Hasher;
import algat.model.Bucket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HashTableController {
    @FXML private GridPane bucketsContainer;

    private int capacity;
    private Config config;
    private ArrayList<Bucket> buckets = new ArrayList<>();
    private int[] probeSequence;
    private ObservableList<ErrorCodes> errors = FXCollections.observableArrayList();

    private ProbeAnimation currentAnimation = new ProbeAnimation();
    private boolean animationsEnabled = true;

    public ObservableList<ErrorCodes> getErrors() { return errors; }
    public void clearErrors() { errors.clear(); }

    ProbeAnimation getAnimation() { return currentAnimation; }

    void setAnimationsEnabled(boolean animationsEnabled) {
        this.animationsEnabled = animationsEnabled;
    }

    void init(Config config, List<Bucket> data) {
        if (data != null)
            buckets = new ArrayList<>(data);

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
        this.updateBuckets();
        bucketsContainer.getChildren().clear();

        for (int i = 0; i < capacity; i++) {
            Bucket bucket = buckets.get(i);
            bucketsContainer.addRow(i, new BucketComponent(bucket));
        }
    }

    private void updateBuckets() {
        // Save old buckets data inside an hash map
        HashMap<String, String> oldBucketsData = this.getBucketsData();

        // Create and initialize new buckets list
        buckets = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++)
            buckets.add(new Bucket());

        // If present, insert old buckets data into new buckets list
        if (!oldBucketsData.isEmpty())
            this.insertBucketsData(oldBucketsData);
    }

    private HashMap<String, String> getBucketsData() {
        HashMap<String, String> bucketsData = new HashMap<>();

        for (Bucket bucket : buckets) {
            String key = bucket.getKey();

            if (!key.isEmpty())
                bucketsData.put(key, bucket.getValue());
        }

        return bucketsData;
    }

    private void insertBucketsData(HashMap<String, String> bucketsData) {
        boolean animationsWereEnabled = animationsEnabled;
        animationsEnabled = false;  // Disabilito temporaneamente le animazioni per aggiornare rapidamente la tabella

        for (Map.Entry<String, String> entry : bucketsData.entrySet())
            this.insert(entry.getKey(), entry.getValue());

        animationsEnabled = animationsWereEnabled;
    }

    void insert(String key, String value) {
        int probeIndex = this.probe(key);

        if (probeIndex == capacity) {
            errors.add(ErrorCodes.FULL_TABLE);
            return;
        }

        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        this.animate(probeIndex, event -> {
            selectedBucket.setKey(key);
            selectedBucket.setValue(value);

            if (selectedBucket.isDeleted())
                selectedBucket.setDeleted(false);
        });
    }

    void remove(String key) {
        int probeIndex = this.probe(key);

        if (probeIndex == capacity) {
            errors.add(ErrorCodes.KEY_NOT_FOUND);
            return;
        }

        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        this.animate(probeIndex, event -> {
            if (selectedBucket.getKey().equals(key))
                selectedBucket.setDeleted(true);
            else
                errors.add(ErrorCodes.KEY_NOT_FOUND);
        });
    }

    void hasKey(String key) {
        int probeIndex = this.probe(key);

        if (probeIndex == capacity)
            throw new NoSuchKeyException("Key " + key + " does not exist");

        Bucket selectedBucket = buckets.get(probeSequence[probeIndex]);
        this.animate(probeIndex, event -> {
            System.out.println(!selectedBucket.isEmpty() && !selectedBucket.isDeleted() && selectedBucket.getKey().equals(key));
        });
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

    private void animate(int probeIndex, EventHandler<ActionEvent> onFinishedHandler) {
        if (!animationsEnabled) {
            onFinishedHandler.handle(new ActionEvent());
            return;
        }

        ObservableList<Node> children = bucketsContainer.getChildren();
        ArrayList<BucketComponent> animSequence = new ArrayList<>(capacity);

        for (int i = 0; i <= probeIndex; i++) {
            BucketComponent bucket = (BucketComponent) children.get(probeSequence[i]);
            animSequence.add(bucket);
        }

        currentAnimation.setAnimSequence(animSequence);
        currentAnimation.setOnFinished(onFinishedHandler);
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

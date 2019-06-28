package algat.controller;

import algat.model.Bucket;

import java.util.ArrayList;

@FunctionalInterface
public interface OnConfigCompleted {
    void configCompleted(ArrayList<Bucket> data);
}

package algat.controller;

import algat.model.Record;

import java.util.List;

@FunctionalInterface
public interface OnConfigCompleted {
    void configCompleted(List<Record> data);
}

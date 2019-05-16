package algat.model;

import javafx.beans.property.SimpleStringProperty;

public class Record {
    private final SimpleStringProperty key = new SimpleStringProperty("");
    private final SimpleStringProperty value = new SimpleStringProperty("");

    public Record(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public String getKey() {
        return key.get();
    }

    public void setKey(String key) {
        this.key.set(key);
    }

    public String getValue() {
        return value.get();
    }

    public void setValue(String value) {
        this.value.set(value);
    }

    @Override
    public String toString() {
        return this.key.get() + "=" + this.value.get();
    }
}

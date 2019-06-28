package algat.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Bucket {
    private final SimpleStringProperty key = new SimpleStringProperty();
    private final SimpleStringProperty value = new SimpleStringProperty();
    private final SimpleBooleanProperty deleted = new SimpleBooleanProperty();

    public Bucket(String key, String value) {
        this.setKey(key);
        this.setValue(value);
        this.setDeleted(false);
    }

    public Bucket() {
        this("", "");
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

    public boolean isDeleted() {
        return deleted.get();
    }

    public void setDeleted(boolean deleted) {
        this.deleted.set(deleted);
    }

    // Properties
    public StringProperty keyProperty() {
        return key;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public BooleanProperty deletedProperty() {
        return deleted;
    }

    @Override
    public String toString() {
        return this.key.get() + " = " + this.value.get();
    }
}

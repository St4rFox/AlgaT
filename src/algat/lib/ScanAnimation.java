package algat.lib;

import algat.controller.BucketComponent;
import javafx.animation.Transition;
import javafx.util.Duration;

import java.util.ArrayList;

public class ScanAnimation extends Transition {
    private ArrayList<BucketComponent> scanSequence = new ArrayList<>();
    private Integer cursor = -1;

    public ScanAnimation() {
        this.setCycleDuration(Duration.millis(1000));
    }

    @Override
    protected void interpolate(double v) {
        int nextIndex = (int) Math.floor(this.scanSequence.size() * v);

        if (this.cursor == -1) {
            this.scanSequence.get(nextIndex).getStyleClass().add("inspected");
            this.cursor = nextIndex;
        } else if (this.cursor != nextIndex && nextIndex < this.scanSequence.size()) {
            this.scanSequence.get(nextIndex).getStyleClass().add("inspected");
            this.scanSequence.get(this.cursor).getStyleClass().remove("inspected");
            this.cursor = nextIndex;
        }
    }

    public void addNode(BucketComponent node) {
        this.scanSequence.add(node);
    }

    public void stepForward() {
        if (this.cursor == -1) {
            this.scanSequence.get(this.cursor + 1).getStyleClass().add("inspected");
        } else if (this.cursor + 1 < this.scanSequence.size()) {
            this.scanSequence.get(this.cursor).getStyleClass().remove("inspected");
            this.scanSequence.get(this.cursor + 1).getStyleClass().add("inspected");
        }
    }

    public void stepBackward() {
        if (this.cursor > 0) {
            this.scanSequence.get(this.cursor).getStyleClass().remove("inspected");
            this.scanSequence.get(this.cursor - 1).getStyleClass().add("inspected");
        }
    }
}

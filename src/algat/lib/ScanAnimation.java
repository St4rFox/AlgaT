package algat.lib;

import algat.controller.TableNode;
import javafx.animation.Transition;
import javafx.util.Duration;

import java.util.ArrayList;

public class ScanAnimation extends Transition {
    private ArrayList<TableNode> scanSequence = new ArrayList<>();
    private Integer cursor = null;

    private ScanAnimation() { }

    @Override
    protected void interpolate(double v) {
        int nextIndex = (int) Math.floor(this.scanSequence.size() * v);

        if (this.cursor == null) {
            this.scanSequence.get(nextIndex).getStyleClass().add("inspected");
            this.cursor = nextIndex;
        } else if (this.cursor != nextIndex && nextIndex < this.scanSequence.size()) {
            this.scanSequence.get(nextIndex).getStyleClass().add("inspected");
            this.scanSequence.get(this.cursor).getStyleClass().remove("inspected");
            this.cursor++;
        }
    }

    private static ScanAnimation animation = new ScanAnimation();

    public static ScanAnimation getAnimation() {
        System.out.println(animation.scanSequence);
        return animation;
    }

    public static void addNode(TableNode node) {
        animation.scanSequence.add(node);
    }

    public static void withDuration(Duration duration) {
        animation.setCycleDuration(duration);
    }
}

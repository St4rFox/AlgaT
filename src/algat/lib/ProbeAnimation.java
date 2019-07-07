package algat.lib;

import algat.controller.BucketComponent;
import javafx.animation.Transition;
import javafx.event.ActionEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ProbeAnimation extends Transition {
    private ArrayList<BucketComponent> animSequence = null;
    private int sequenceLength;
    private int cursor;

    public ProbeAnimation() {
        this.setCycleDuration(Duration.millis(1000));
    }

    public void setAnimSequence(List<BucketComponent> animSequence) {
        this.animSequence = new ArrayList<>(animSequence);
        this.sequenceLength = this.animSequence.size();
        this.cursor = -1;
    }

    public boolean isStartable() { return animSequence != null; }

    @Override
    public void play() {
        Duration restartTime = getTotalDuration()
                .divide(animSequence.size())
                .multiply(cursor);

        this.jumpTo(restartTime);
        super.play();
    }

    @Override
    public void stop() {
        animSequence = null;
        super.stop();
    }

    @Override
    protected void interpolate(double v) {
        int nextIndex = (int) Math.floor(this.animSequence.size() * v);

        if (nextIndex != cursor)
            stepForward();
    }

    public void stepForward() {
        if (cursor > -1 && cursor < sequenceLength)
            animSequence.get(cursor).getStyleClass().remove("selected");

        cursor = cursor >= sequenceLength ? cursor : cursor + 1;
        if (cursor < sequenceLength)
            animSequence.get(cursor).getStyleClass().add("selected");
        else
            getOnFinished().handle(new ActionEvent());
    }

    public void stepBackward() {
        if (cursor > -1 && cursor < sequenceLength)
            animSequence.get(cursor).getStyleClass().remove("selected");

        cursor = cursor <= -1 ? cursor : cursor - 1;
        if (cursor > -1)
            animSequence.get(cursor).getStyleClass().add("selected");
    }

    public void rewind() {
        this.setStatus(Status.STOPPED);
        this.jumpTo("start");
        this.restoreState();
    }

    private void restoreState() {
        cursor = -1;

        for (BucketComponent bucket : animSequence)
            bucket.getStyleClass().remove("selected");
    }
}

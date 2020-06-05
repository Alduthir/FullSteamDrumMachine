package org.alduthir.component;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.alduthir.listener.BpmActionListener;
import org.alduthir.model.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * Class BpmSpinner
 * <p>
 * A viewcomponent for a Spinner element containing an integer. This implementation is used to set the BPM of a song,
 * and automatically update it in the database.
 */
public class BpmSpinner extends Spinner<Integer> {
    public static final int MIN_BPM = 20;
    public static final int MAX_BPM = 250;
    public boolean isInitialised = false;

    private List<BpmActionListener> listeners = new ArrayList<>();

    /**
     * Initialize the spinner with a range of possible values. Also binds an onScrollEvent allowing incrementing of the
     * value. Whenever the scrollEvent is triggered the repository is called to update the BPM value in the database.
     *
     * @param song       required to set the initial value of the spinner and to know what to bind the bpm to.
     */
    public void initializeBpmSpinner(Song song) {
        this.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_BPM, MAX_BPM));
        this.getValueFactory().setValue(song.getBpm());
        this.setOnScroll(
                (e) -> {
                    double delta = e.getDeltaY();

                    if (delta < 0) {
                        this.decrement();
                    } else if (delta > 0) {
                        this.increment();
                    }
                }
        );
        this.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
            if (!oldValue.equals(newValue)) {
                for (BpmActionListener listener : listeners) {
                    listener.updateAction(song, Integer.parseInt(newValue));
                }
            }
        });
        isInitialised = true;
    }

    public void addListener(BpmActionListener actionListener) {
        listeners.add(actionListener);
    }
}

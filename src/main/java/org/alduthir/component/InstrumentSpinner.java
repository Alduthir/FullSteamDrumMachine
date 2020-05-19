package org.alduthir.component;

import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

/**
 * Class InstrumentSpinner
 * <p>
 * A viewcomponent for a Spinner element containing an integer. This implementation is used to scroll through available
 * midi keys in the drum midi channel.
 */
public class InstrumentSpinner extends Spinner<Integer> {
    public static final int MIN_NOTE_VALUE = 27;
    public static final int MAX_NOTE_VALUE = 87;

    public InstrumentSpinner() {
        this.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_NOTE_VALUE, MAX_NOTE_VALUE));
        this.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                this.decrement();
            } else if (delta > 0) {
                this.increment();
            }
        });
    }
}

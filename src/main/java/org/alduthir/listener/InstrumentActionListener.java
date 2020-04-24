package org.alduthir.listener;

import org.alduthir.model.Instrument;

/**
 * Interface InstrumentActionListener
 * <p>
 * An interface to define functions Listeners must implement to handle Events raised in the InstrumentCell
 */
public interface InstrumentActionListener {
    /**
     * Call to remove an instrument. Raised whenever the deleteButton on the InstrumentCell is clicked.
     *
     * @param instrument The instrument to be removed.
     */
    void removeAction(Instrument instrument);

    /**
     * Update the beat for given instrument. Called whenever a checkbox is checked/unchecked in the InstrumentCell.
     *
     * @param beatNotes  An encoding string of 16 characters containing 0's and 1's indicating the beat.
     * @param instrument The instrument for which the beat must be saved.
     */
    void updateAction(String beatNotes, Instrument instrument);
}

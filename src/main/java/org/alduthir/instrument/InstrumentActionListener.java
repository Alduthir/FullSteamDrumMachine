package org.alduthir.instrument;

/**
 * Interface InstrumentActionListener
 *
 * An interface to define functions Listeners must implement to handle Events raised in the InstrumentCell
 */
public interface InstrumentActionListener {
    void removeAction(Instrument instrument);
    void updateAction(String beatNotes, Instrument instrument);
}

package org.alduthir.instrument;

public interface InstrumentActionListener {
    void removeAction(Instrument instrument);
    void updateAction(String beatNotes, Instrument instrument);
}

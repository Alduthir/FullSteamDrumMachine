package org.alduthir.instrument;

public interface InstrumentActionListener {
    void removeInstrument(Instrument instrument);
    void updateBeat(String beatNotes, Instrument instrument);
}

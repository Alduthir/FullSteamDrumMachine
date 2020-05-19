package org.alduthir.service;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;

import java.util.List;

public interface InstrumentManageServiceInterface {
    void removeInstrument(Measure measure, Instrument instrument);

    void saveNewInstrument(String name, int midiNumber, Measure measure);

    void reuseInstrument(Instrument instrument, Measure measure);

    void updateBeat(Measure measure, Instrument instrument, String beatNotes);

    void playNote(int midiKey);

    void playMeasure(Measure measure, int bpm);

    List<Instrument> getReuseOptionCollection(Measure measure);

    List<Instrument> getInstrumentCollectionForMeasure(Measure measure);
}

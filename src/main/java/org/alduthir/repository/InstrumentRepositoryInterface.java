package org.alduthir.repository;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;

import java.sql.SQLException;
import java.util.List;

public interface InstrumentRepositoryInterface extends DatabaseInteractionInterface<Instrument> {
    Instrument createInstrument(String name, int midiNumber) throws SQLException;

    List<Instrument> fetchForMeasure(Measure measure) throws SQLException;

    List<Instrument> fetchReuseOptionCollection(Measure measure) throws SQLException;

    void addToMeasure(Instrument instrument, Measure measure) throws SQLException;

    void updateBeat(Measure measure, Instrument instrument, String encodedBeat) throws SQLException;

    void removeFromMeasure(Measure measure, Instrument instrument) throws SQLException;
}

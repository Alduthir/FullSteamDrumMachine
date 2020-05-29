package org.alduthir.repository;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;

import java.util.List;

public interface InstrumentRepositoryInterface extends DatabaseInteractionInterface<Instrument> {
    Instrument createInstrument(String name, int midiNumber) throws DataPersistanceException;

    List<Instrument> fetchForMeasure(Measure measure) throws DataRetrievalException;

    List<Instrument> fetchReuseOptionCollection(Measure measure) throws DataRetrievalException;

    void addToMeasure(Instrument instrument, Measure measure) throws DataPersistanceException;

    void updateBeat(Measure measure, Instrument instrument, String encodedBeat) throws DataPersistanceException;

    void removeFromMeasure(Measure measure, Instrument instrument) throws DataRemovalException;
}

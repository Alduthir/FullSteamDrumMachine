package org.alduthir.repository;

import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;

import java.sql.SQLException;
import java.util.List;

public interface MeasureRepositoryInterface extends DatabaseInteractionInterface<Measure> {
    Measure createMeasure(Measure measure) throws DataPersistanceException;

    List<SongMeasure> fetchForSong(Song song) throws DataRetrievalException;

    void addToSong(Measure measure, Song song, int sequence) throws DataPersistanceException;

    void removeFromSong(SongMeasure songMeasure) throws DataRemovalException;
}

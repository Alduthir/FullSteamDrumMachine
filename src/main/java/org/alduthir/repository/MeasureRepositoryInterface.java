package org.alduthir.repository;

import javafx.collections.ObservableList;
import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;

import java.sql.SQLException;

public interface MeasureRepositoryInterface extends DatabaseInteractionInterface<Measure> {
    Measure createMeasure(Measure measure) throws SQLException;

    ObservableList<SongMeasure> fetchForSong(Song song) throws SQLException;

    void addToSong(Measure measure, Song song, int sequence) throws SQLException;

    void removeFromSong(SongMeasure songMeasure) throws SQLException;
}

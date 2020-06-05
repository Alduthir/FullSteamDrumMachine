package org.alduthir.service;

import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;

import java.util.List;

public interface MeasureManageServiceInterface {
    List<SongMeasure> getSongMeasureCollection(Song song);

    void deleteSongMeasure(SongMeasure songMeasure);

    void playMeasure(Measure measure, int bpm);

    List<Measure> getAllMeasures();

    void addToSong(Song song, Measure measure, int sequence);

    void createForSong(Measure measure, Song song, int sequence);

    boolean isMeasureUsedInMultiplePlaces(Measure measure);

    void deleteMeasure(Measure measure);
}

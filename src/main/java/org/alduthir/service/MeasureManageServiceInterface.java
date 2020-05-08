package org.alduthir.service;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;

public interface MeasureManageServiceInterface {
    void initializeMeasureList(Song song, JFXListView<SongMeasure> measureList);

    void deleteMeasure(Song song, JFXListView<SongMeasure> measureList);

    void addMeasure(Song song, JFXListView<SongMeasure> measureList);

    void playSelectedSongMeasure(Song song, JFXListView<SongMeasure> measureList);

    void playMeasure(Measure measure, int bpm);

    ObservableList<Measure> fetchAll();

    void addToSong(Song song, Measure measure, int sequence);
}

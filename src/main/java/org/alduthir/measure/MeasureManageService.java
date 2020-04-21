package org.alduthir.measure;

import com.jfoenix.controls.JFXListView;
import javafx.scene.control.TextInputDialog;
import org.alduthir.song.Song;
import org.alduthir.util.MidiPlayer;
import org.alduthir.util.StyledTextInputDialog;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.Optional;

public class MeasureManageService {
    private MeasureRepository repository;
    private MidiPlayer midiPlayer;

    public MeasureManageService() {
        midiPlayer = new MidiPlayer();
        try {
            repository = new MeasureRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeMeasureList(Song song, JFXListView<Measure> measureList) {
        try {
            measureList.getItems().setAll(repository.fetchForSong(song));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteMeasure(Song song, JFXListView<Measure> measureList) {
        Measure selectedMeasure = measureList.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            try {
                repository.removeFromSong(selectedMeasure, song);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeMeasureList(song, measureList);
        }
    }

    public void addMeasure(Song song, JFXListView<Measure> measureList) {
        TextInputDialog textInputDialog = new StyledTextInputDialog();
        textInputDialog.setTitle("Create new Measure");
        textInputDialog.setContentText("Enter the name of your new measure.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Measure measure = new Measure(result.get());
            try {
                repository.createMeasure(measure);
                repository.addToSong(measure, song, measureList.getItems().size());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            initializeMeasureList(song, measureList);
        }
    }

    public void playSelectedMeasure(Song song, JFXListView<Measure> measureList) {
        Measure selectedMeasure = measureList.getSelectionModel().getSelectedItem();

        if (selectedMeasure != null) {
            try {
                midiPlayer.playMeasure(song.getBpm(), selectedMeasure);
            } catch (MidiUnavailableException | InvalidMidiDataException | SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

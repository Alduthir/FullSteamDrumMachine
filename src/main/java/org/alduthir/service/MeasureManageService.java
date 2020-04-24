package org.alduthir.service;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import javafx.scene.control.TextInputDialog;
import org.alduthir.model.Measure;
import org.alduthir.model.SongMeasure;
import org.alduthir.model.Song;
import org.alduthir.repository.MeasureRepository;
import org.alduthir.component.StyledTextInputDialog;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Class MeasureManageService
 * <p>
 * A service layer class containing CRUD functionality for Measures.
 */
public class MeasureManageService {
    private MeasureRepository repository;
    private final MidiPlayer midiPlayer;

    /**
     * Create dependencies for MidiPlayer and the database interaction repository.
     */
    public MeasureManageService() {
        midiPlayer = new MidiPlayer();
        try {
            repository = new MeasureRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise a list of SongMeasure models fetched through the repository.
     *
     * @param song        The Song for which to retrieve all SongMeasures.
     * @param measureList the list to which the retrieved models should be added.
     */
    public void initializeMeasureList(Song song, JFXListView<SongMeasure> measureList) {
        try {
            measureList.getItems().setAll(repository.fetchForSong(song));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request the removal of the selected SongMeasure from the database and reload the list of SongMeasures.
     *
     * @param song        The song for which the measureList must be reinitialised.
     * @param measureList The list from which the selectedItem will be deleted. And that will be reinitialised.
     */
    public void deleteMeasure(Song song, JFXListView<SongMeasure> measureList) {
        SongMeasure selectedItem = measureList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            try {
                repository.removeFromSong(selectedItem);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeMeasureList(song, measureList);
        }
    }

    /**
     * Create a dialog requesting a name for the new measure. Once the dialog resolves use the result to create a
     * new measure to add to the song. And reinitialise the measureList so it includes this new measure.
     *
     * @param song        The song to which the new measure will be added.
     * @param measureList The list to be reloaded after the new measure is added.
     */
    public void addMeasure(Song song, JFXListView<SongMeasure> measureList) {
        TextInputDialog textInputDialog = new StyledTextInputDialog();
        textInputDialog.setTitle("Create new Measure");
        textInputDialog.setContentText("Enter the name of your new measure.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Measure measure = new Measure(result.get());
            try {
                measure = repository.createMeasure(measure);
                repository.addToSong(measure, song, measureList.getItems().size());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            initializeMeasureList(song, measureList);
        }
    }

    /**
     * Retrieve the selected Measure from the measureList and request the midiPlayer to
     *
     * @param song        Used to retrieve the bpm at which the measure should be played.
     * @param measureList The list from which the selected Measure is played.
     */
    public void playSelectedMeasure(Song song, JFXListView<SongMeasure> measureList) {
        SongMeasure songMeasure = measureList.getSelectionModel().getSelectedItem();

        if (songMeasure != null) {
            try {
                midiPlayer.playMeasure(song.getBpm(), songMeasure.getMeasure());
            } catch (MidiUnavailableException | InvalidMidiDataException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Request the repository to retrieve a hydrated list of Measures.
     *
     * @return An ObservableList of Measure models.
     */
    public ObservableList<Measure> fetchAll() {
        try {
            return repository.fetchAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Request the repository to add the given measure to the Song.
     *
     * @param song     The song to which the Measure should be added.
     * @param measure  The Measure to be added.
     * @param sequence The position of the new Measure in the Song's order of Measures.
     */
    public void addToSong(Song song, Measure measure, int sequence) {
        try {
            repository.addToSong(measure, song, sequence);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

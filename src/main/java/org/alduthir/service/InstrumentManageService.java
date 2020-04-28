package org.alduthir.service;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.VBox;
import org.alduthir.controller.AddInstrumentOption;
import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.repository.InstrumentRepository;
import org.alduthir.util.NoSelectionModel;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;

/**
 * Class InstrumentManageService
 * <p>
 * A service layer class containing CRUD functionality for Instruments.
 */
public class InstrumentManageService {
    private MidiPlayer midiPlayer;
    private InstrumentRepository repository;

    /**
     * Attempt to construct the manageService with a repository. If no databaseConnection can be established or the
     * jdbc Driver cannot be found, an exception is thrown.
     */
    public InstrumentManageService() {
        try {
            repository = new InstrumentRepository();
            midiPlayer = new MidiPlayer();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all Instruments linked to the current Measure as well their encoded beats.
     *
     * @param measure  required to retrieve the correct MeasureInstrument collection.
     * @param beatList A list of InstrumentObjects to be initialised with the retrieved ObservableList of Instruments.
     */
    public void initializeInstrumentCollection(Measure measure, JFXListView<Instrument> beatList) {
        try {
            beatList.getItems().setAll(repository.fetchForMeasure(measure));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Initialised with NoSelectionModel because selection logic would break the complex InstrumentCell UI.
        beatList.setSelectionModel(new NoSelectionModel<>());
    }

    /**
     * The reuseOptionCollection contains all Instruments that are NOT within the current Measure.
     *
     * @param measure       The measure from which all Instruments should be excluded.
     * @param reuseComboBox The combobox for which the retrieved Instruments should be set as options.s
     */
    public void initializeReuseOptionCollection(Measure measure, JFXComboBox<Instrument> reuseComboBox) {
        try {
            reuseComboBox.getItems().setAll(repository.fetchReuseOptionCollection(measure));
            reuseComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove an instrument from the Measure and update the beatList.
     *
     * @param measure    The measure from which the instrument should be removed.
     * @param instrument The instrument to be removed. Only the link between it and the Measure is removed. It is not
     *                   deleted.
     * @param beatList   The beatlist which must be updated to no longer include the removed Instrument.
     */
    public void removeInstrument(Measure measure, Instrument instrument, JFXListView<Instrument> beatList) {
        try {
            repository.removeFromMeasure(measure, instrument);
            initializeInstrumentCollection(measure, beatList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a new Instrument object and add it to the given Measure.
     *
     * @param name       The given name for the new Instrument.
     * @param midiNumber The key (between 27 and 87) for the midiEvent corresponding to the Instrument.
     * @param measure    The Measure to which the new Instrument is added.
     */
    public void saveNewInstrument(String name, int midiNumber, Measure measure) {
        try {
            Instrument instrument = repository.createInstrument(name, midiNumber);
            repository.addToMeasure(instrument, measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Add an existing Instrument to the Measure.
     *
     * @param instrument The given Instrument to be added.
     * @param measure    The Measure the Instrument should be added to.
     */
    public void reuseInstrument(Instrument instrument, Measure measure) {
        try {
            repository.addToMeasure(instrument, measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises an Integer spinner with a value between 27 and 87 and corresponding scroll events.
     * The value must be between 27 and 87 as those are the keys in the Midi Drum channel mapped to sounds.
     *
     * @param insturmentSpinner The Spinner UI element.
     */
    public void initializeInstrumentSpinner(Spinner<Integer> insturmentSpinner) {
        insturmentSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(27, 87));
        insturmentSpinner.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                insturmentSpinner.decrement();
            } else if (delta > 0) {
                insturmentSpinner.increment();
            }
        });
    }

    /**
     * Initialize the combobox with 2 options NEW or REUSE. Depending on which is selected the corresponding
     * VBox will be hidden or shown.
     */
    public void initiallizeNewOrReuseComboBox(
            JFXComboBox<AddInstrumentOption> newOrReuseSelection,
            VBox reuseBox,
            VBox newBox
    ) {
        ObservableList<AddInstrumentOption> newOrReuseOptionCollection = FXCollections.observableArrayList();
        newOrReuseOptionCollection.add(AddInstrumentOption.NEW);
        newOrReuseOptionCollection.add(AddInstrumentOption.REUSE);
        newOrReuseSelection.getItems().setAll(newOrReuseOptionCollection);
        newOrReuseSelection.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            switch (newValue) {
                case NEW:
                    reuseBox.setVisible(false);
                    newBox.setVisible(true);
                    break;
                case REUSE:
                    newBox.setVisible(false);
                    reuseBox.setVisible(true);
                    break;
            }
        });
        newOrReuseSelection.getSelectionModel().selectFirst();
    }

    /**
     * Update the beat for the Instrument within the given Measure.
     *
     * @param measure    The Measure for which to update the Beat.
     * @param instrument The Instrument for which to update the Beat.
     * @param beatNotes  An encoded string of 0's and 1's determining when the Instrument should be played.
     */
    public void updateBeat(Measure measure, Instrument instrument, String beatNotes) {
        try {
            repository.updateBeat(measure, instrument, beatNotes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the corresponding sound for a given key in the midiChannel.
     *
     * @param midiKey an integer representing which key in the midiChannel should be played.
     */
    public void playNote(int midiKey) {
        try {
            midiPlayer.playNote(midiKey);
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }
}

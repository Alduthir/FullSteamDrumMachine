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
import org.alduthir.repository.InstrumentRepositoryInterface;
import org.alduthir.util.NoSelectionModel;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;

/**
 * Class InstrumentManageService
 * <p>
 * A service layer class containing CRUD functionality for Instruments.
 */
public class InstrumentManageService implements InstrumentManageServiceInterface {
    private MusicPlayerInterface musicPlayerInterface;
    private InstrumentRepositoryInterface instrumentRepositoryInterface;

    /**
     * Attempt to construct the manageService with a repository. If no databaseConnection can be established or the
     * jdbc Driver cannot be found, an exception is thrown.
     */
    public InstrumentManageService(
            InstrumentRepositoryInterface instrumentRepositoryInterface,
            MusicPlayerInterface musicPlayerInterface
    ) {
        this.instrumentRepositoryInterface = instrumentRepositoryInterface;
        this.musicPlayerInterface = musicPlayerInterface;
    }

    /**
     * Retrieve all Instruments linked to the current Measure as well their encoded beats.
     *
     * @param measure  required to retrieve the correct MeasureInstrument collection.
     * @param beatList A list of InstrumentObjects to be initialised with the retrieved ObservableList of Instruments.
     */
    @Override
    public void initializeInstrumentCollection(Measure measure, JFXListView<Instrument> beatList) {
        try {
            beatList.getItems().setAll(instrumentRepositoryInterface.fetchForMeasure(measure));
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
    @Override
    public void initializeReuseOptionCollection(Measure measure, JFXComboBox<Instrument> reuseComboBox) {
        try {
            reuseComboBox.getItems().setAll(instrumentRepositoryInterface.fetchReuseOptionCollection(measure));
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
    @Override
    public void removeInstrument(Measure measure, Instrument instrument, JFXListView<Instrument> beatList) {
        try {
            instrumentRepositoryInterface.removeFromMeasure(measure, instrument);
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
    @Override
    public void saveNewInstrument(String name, int midiNumber, Measure measure) {
        try {
            Instrument instrument = instrumentRepositoryInterface.createInstrument(name, midiNumber);
            instrumentRepositoryInterface.addToMeasure(instrument, measure);
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
    @Override
    public void reuseInstrument(Instrument instrument, Measure measure) {
        try {
            instrumentRepositoryInterface.addToMeasure(instrument, measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises an Integer spinner with a value between 27 and 87 and corresponding scroll events.
     * The value must be between 27 and 87 as those are the keys in the Midi Drum channel mapped to sounds.
     *
     * @param instrumentSpinner The Spinner UI element.
     */
    @Override
    public void initializeInstrumentSpinner(Spinner<Integer> instrumentSpinner) {
        instrumentSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(27, 87));
        instrumentSpinner.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                instrumentSpinner.decrement();
            } else if (delta > 0) {
                instrumentSpinner.increment();
            }
        });
    }

    /**
     * Initialize the combobox with 2 options NEW or REUSE. Depending on which is selected the corresponding
     * VBox will be hidden or shown.
     */
    @Override
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
    @Override
    public void updateBeat(Measure measure, Instrument instrument, String beatNotes) {
        try {
            instrumentRepositoryInterface.updateBeat(measure, instrument, beatNotes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the corresponding sound for a given key in the midiChannel.
     *
     * @param midiKey an integer representing which key in the midiChannel should be played.
     */
    @Override
    public void playNote(int midiKey) {
        try {
            musicPlayerInterface.playNote(midiKey);
        } catch (InvalidMidiDataException | MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play the given measure at the given bpm.
     *
     * @param measure the measure containing multiple Instruments and their timing to be played.
     * @param bpm     The speed at which the measure should be played.
     */
    @Override
    public void playMeasure(Measure measure, int bpm) {
        try {
            musicPlayerInterface.playMeasure(bpm, measure);
        } catch (InvalidMidiDataException | MidiUnavailableException | SQLException e) {
            e.printStackTrace();
        }
    }
}

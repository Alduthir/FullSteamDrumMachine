package org.alduthir.service;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.repository.InstrumentRepositoryInterface;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
     * Retrieve an observableArrayList of instruments found in a measure.
     *
     * @param measure the measure for which to search for instruments.
     * @return An arraylist of instruments.
     */
    @Override
    public List<Instrument> getInstrumentCollectionForMeasure(Measure measure) {
        List<Instrument> instrumentList = new ArrayList<>();
        try {
            return instrumentRepositoryInterface.fetchForMeasure(measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instrumentList;
    }

    /**
     * The reuseOptionCollection contains all Instruments that are NOT within the current Measure.
     *
     * @param measure The measure from which all Instruments should be excluded.
     * @return a list of reuseable instruments for the measure.
     */
    @Override
    public List<Instrument> getReuseOptionCollection(Measure measure) {
        List<Instrument> reuseOptionCollection = new ArrayList<>();
        try {
            return instrumentRepositoryInterface.fetchReuseOptionCollection(measure);
        } catch (SQLException e) {
            e.printStackTrace();
            return reuseOptionCollection;
        }
    }

    /**
     * Remove an instrument from the Measure and update the instrumentList.
     *
     * @param measure    The measure from which the instrument should be removed.
     * @param instrument The instrument to be removed. Only the link between it and the Measure is removed. It is not
     *                   deleted.
     */
    @Override
    public void removeInstrument(Measure measure, Instrument instrument) {
        try {
            instrumentRepositoryInterface.removeFromMeasure(measure, instrument);
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

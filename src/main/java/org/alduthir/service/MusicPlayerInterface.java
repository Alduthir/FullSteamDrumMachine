package org.alduthir.service;

import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.repository.DataRetrievalException;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import java.sql.SQLException;

public interface MusicPlayerInterface {
    Sequencer getSequencer() throws MidiUnavailableException;

    void playSong(Song song) throws MidiUnavailableException, InvalidMidiDataException, DataRetrievalException;

    void playMeasure(int bpm, Measure measure) throws InvalidMidiDataException, MidiUnavailableException, DataRetrievalException;

    void playNote(Integer value) throws InvalidMidiDataException, MidiUnavailableException;
}

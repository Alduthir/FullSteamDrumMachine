package org.alduthir.service;

import org.alduthir.model.Measure;
import org.alduthir.model.Song;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;

public interface MusicPlayerInterface {
    void playSong(Song song) throws SQLException, MidiUnavailableException, InvalidMidiDataException;

    void playMeasure(int bpm, Measure measure) throws InvalidMidiDataException, MidiUnavailableException, SQLException;

    void playNote(Integer value) throws InvalidMidiDataException, MidiUnavailableException;
}

package org.alduthir.service;

import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.InstrumentRepositoryInterface;
import org.alduthir.repository.MeasureRepositoryInterface;
import org.junit.jupiter.api.Test;

import javax.sound.midi.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

/**
 * This Unittest class tests the MidiPlayer implementation of the MusicPlayerInterface.
 */
public class TestMidiPlayer {
    InstrumentRepositoryInterface instrumentRepository = mock(InstrumentRepositoryInterface.class);

    MeasureRepositoryInterface measureRepository = mock(MeasureRepositoryInterface.class);

    /**
     * Tests that to play a note, a single track with a single tick is created in the Sequencer.
     */
    @Test
    protected void testPlayNote() {
        try {
            MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
            Sequencer sequencer = midiPlayer.getSequencer();
            int noteValue = 4;
            midiPlayer.playNote(noteValue);

            Track[] trackList = sequencer.getSequence().getTracks();
            assertEquals(1, trackList.length);
            Track track = trackList[0];
            assertEquals(1, track.ticks());
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    /**
     * Tests that the amount of ticks is equal to the amount of characters in the beat string from the first 1 to the
     * last 1 and everything inbetween. 0 notes may not get a midiEvent but because they are followed by another
     * active note the pause should still registered as a tick.
     */
    @Test
    protected void testPlayMeasure() {
        try {
            MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
            Sequencer sequencer = midiPlayer.getSequencer();

            Measure measure = new Measure("TestMeasure");
            int bpm = 75;

            Instrument hiHat = new Instrument(1, "Hihat", 35, "1010101010101011");
            List<Instrument> instrumentCollection = new ArrayList<>();
            instrumentCollection.add(hiHat);

            // Stub the fetchForMeasure method to return a list of both our instruments.
            when(instrumentRepository.fetchForMeasure(measure)).thenReturn(instrumentCollection);

            midiPlayer.playMeasure(bpm, measure);

            assertEquals(bpm, (int) sequencer.getTempoInBPM());
            Track[] trackList = sequencer.getSequence().getTracks();
            assertEquals(1, trackList.length);
            Track track = trackList[0];
            assertEquals(16, track.ticks());
        } catch (MidiUnavailableException | InvalidMidiDataException | SQLException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    /**
     * Tests that if an Instrument has no active notes in it's beat string, no ticks are added to the track.
     */
    @Test
    protected void testPlayMeasureWithNoActiveNotes() {
        try {
            MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
            Sequencer sequencer = midiPlayer.getSequencer();

            Measure measure = new Measure("EmptyTestMeasure");
            int bpm = 75;

            Instrument hiHat = new Instrument(1, "Hihat", 35, "0000000000000000");
            List<Instrument> instrumentCollection = new ArrayList<>();
            instrumentCollection.add(hiHat);

            // Stub the fetchForMeasure method to return a list of both our instruments.
            when(instrumentRepository.fetchForMeasure(measure)).thenReturn(instrumentCollection);

            midiPlayer.playMeasure(bpm, measure);

            assertEquals(bpm, (int) sequencer.getTempoInBPM());
            Track[] trackList = sequencer.getSequence().getTracks();
            assertEquals(1, trackList.length);
            Track track = trackList[0];
            assertEquals(0, track.ticks());
        } catch (MidiUnavailableException | InvalidMidiDataException | SQLException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }

    /**
     * Tests that the amount of ticks in the track are equal to the sum of both measure's beats.
     */
    @Test
    protected void testPlaySong() {
        try {
            MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
            Sequencer sequencer = midiPlayer.getSequencer();
            Song song = new Song(1, "TestSong", 75);

            Measure firstMeasure = new Measure("FirstMeasure");
            Measure secondMeasure = new Measure("SecondMeasure");

            SongMeasure firstSongMeasure = new SongMeasure(1, song, firstMeasure);
            SongMeasure secondSongMeasure = new SongMeasure(2, song, secondMeasure);

            List<SongMeasure> songMeasureCollection = new ArrayList<>();
            songMeasureCollection.add(firstSongMeasure);
            songMeasureCollection.add(secondSongMeasure);

            Instrument hiHat = new Instrument(1, "Hihat", 35, "1111111111111111");
            List<Instrument> instrumentCollection = new ArrayList<>();
            instrumentCollection.add(hiHat);

            // Stub the fetchForSong method to return the SongMeasureCollection.
            when(measureRepository.fetchForSong(song)).thenReturn(songMeasureCollection);

            // Stub the fetchForMeasure method to return the instrumentCollection for both measures.
            when(instrumentRepository.fetchForMeasure(firstMeasure)).thenReturn(instrumentCollection);
            when(instrumentRepository.fetchForMeasure(secondMeasure)).thenReturn(instrumentCollection);

            midiPlayer.playSong(song);

            // Assert the bpm is set and the track contains enough ticks to account for two measures of 16 notes.
            assertEquals(song.getBpm(), (int) sequencer.getTempoInBPM());
            Track[] trackList = sequencer.getSequence().getTracks();
            assertEquals(1, trackList.length);
            Track track = trackList[0];
            assertEquals(32, track.ticks());
        } catch (MidiUnavailableException | SQLException | InvalidMidiDataException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}
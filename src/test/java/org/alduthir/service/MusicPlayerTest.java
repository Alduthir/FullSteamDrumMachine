package org.alduthir.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;
import org.alduthir.repository.InstrumentRepositoryInterface;
import org.alduthir.repository.MeasureRepositoryInterface;
import org.junit.jupiter.api.Test;

import javax.sound.midi.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class MusicPlayerTest {
    InstrumentRepositoryInterface instrumentRepository = mock(InstrumentRepositoryInterface.class);

    MeasureRepositoryInterface measureRepository = mock(MeasureRepositoryInterface.class);

    /**
     * Tests that to play a note, a single track with a single tick is created in the Sequencer.
     */
    @Test
    void testPlayNote() {
        MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
        Sequencer sequencer = midiPlayer.getSequencer();
        int noteValue = 4;

        try {
            midiPlayer.playNote(noteValue);
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }

        Track[] trackList = sequencer.getSequence().getTracks();
        assertEquals(1, trackList.length);

        Track track = trackList[0];
        assertEquals(1, track.ticks());
    }

    @Test
    void testPlayMeasure() throws SQLException {
        MidiPlayer midiPlayer = new MidiPlayer(instrumentRepository, measureRepository);
        Sequencer sequencer = midiPlayer.getSequencer();

        Measure measure = new Measure("TestMeasure");
        int bpm = 75;

        Instrument hiHat = new Instrument(1, "Hihat", 35, "1010101010101011");
        Instrument bass = new Instrument(1, "Bass", 42, "1000100010001000");
        ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();
        instrumentCollection.add(hiHat);
        instrumentCollection.add(bass);

        // Stub the fetchForMeasure method to return a list of both our instruments.
        when(instrumentRepository.fetchForMeasure(measure)).thenReturn(instrumentCollection);

        try {
            midiPlayer.playMeasure(bpm, measure);
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }

        assertEquals(bpm, (int) sequencer.getTempoInBPM());

        Track[] trackList = sequencer.getSequence().getTracks();
        assertEquals(1, trackList.length);

        Track track = trackList[0];
        // The amount of characters in the beatString. Note that if the last character was 0 for both, this would be 16.
        // As it would not create a tick.
        assertEquals(16, track.ticks());
    }
}

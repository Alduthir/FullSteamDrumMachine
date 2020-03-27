package org.alduthir.util;

import org.alduthir.instrument.Instrument;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

public class MidiPlayer {
    private Sequencer sequencer;

    public Sequencer getSequencer() {
        if (sequencer == null) {
            try {
                sequencer = MidiSystem.getSequencer();
            } catch (MidiUnavailableException e) {
                e.printStackTrace();
            }
        }
        return sequencer;
    }

    public void playSong(Song song) {
    }

    public void playMeasure(Measure measure) {
    }

    public void playInstrument(Instrument instrument) {

    }

    public void stopPlayback() {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
        }
    }
}

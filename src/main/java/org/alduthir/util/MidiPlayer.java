package org.alduthir.util;

import javafx.collections.ObservableList;
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentRepository;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;

import javax.sound.midi.*;
import java.sql.SQLException;

public class MidiPlayer {
    private Sequencer sequencer;
    private InstrumentRepository repository;

    public MidiPlayer() {
        try {
            repository = new InstrumentRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

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

    public void playMeasure(int bpm, Measure measure) throws InvalidMidiDataException, MidiUnavailableException, SQLException {
        sequencer = this.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        ObservableList<Instrument> instrumentCollection = repository.fetchForMeasure(measure);

        for (Instrument instrument : instrumentCollection) {
            int tickIndex = 0;
            for (char shouldPlayOnTick : instrument.getBeat().toCharArray()) {
                if (shouldPlayOnTick == '1') {
                    createNoteOnOff(track, instrument.getMidiNumber(), tickIndex);
                }
                tickIndex++;
            }
        }

        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM((float) bpm);
        sequencer.start();
    }

    public void stopPlayback() {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
        }
    }

    public void playNote(Integer value) throws InvalidMidiDataException, MidiUnavailableException {
        sequencer = this.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        createNoteOnOff(track, value, 0);

        sequencer.setSequence(sequence);
        sequencer.start();
    }

    private void createNoteOnOff(Track track, int key, int tick) {
        track.add(makeEvent(ShortMessage.NOTE_ON, key, tick));
        track.add(makeEvent(ShortMessage.NOTE_OFF, key, tick + 1));
    }

    private MidiEvent makeEvent(int command, int firstByte, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage message = new ShortMessage();
            // 9 is the midi percussion channel, 100 is our default value for volume.
            message.setMessage(command, 9, firstByte, 100);
            event = new MidiEvent(message, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }
}

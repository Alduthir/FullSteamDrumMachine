package org.alduthir.service;

import javafx.collections.ObservableList;
import org.alduthir.model.Instrument;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.InstrumentRepository;
import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.repository.MeasureRepository;

import javax.sound.midi.*;
import java.sql.SQLException;

/**
 * Class MidiPlayer
 * <p>
 * A service for playing Sounds using the javax MidiSystem.
 */
public class MidiPlayer {
    private Sequencer sequencer;
    private InstrumentRepository instrumentRepository;
    private MeasureRepository measureRepository;

    /**
     * Create the InstrumentRepository from which we will be retrieving individual sounds.
     */
    public MidiPlayer() {
        try {
            instrumentRepository = new InstrumentRepository();
            measureRepository = new MeasureRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the sequencer from the MidiSystem
     *
     * @return the default sequencer, connected to a default Receiver
     */
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

    /**
     * Play the entire audio for a song. Creating individual tracks for each measure.
     *
     * @param song The song to play.
     */
    public void playSong(Song song) throws SQLException, MidiUnavailableException, InvalidMidiDataException {
        stopPlayback();
        sequencer = this.getSequencer();
        sequencer.open();
        ObservableList<SongMeasure> songMeasureCollection = measureRepository.fetchForSong(song);
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        int totalTickCount = 0;
        for (SongMeasure songMeasure : songMeasureCollection) {
            Measure measure = songMeasure.getMeasure();
            ObservableList<Instrument> instrumentCollection = instrumentRepository.fetchForMeasure(measure);
            for (Instrument instrument : instrumentCollection) {
                int tickPosition = totalTickCount;
                for (char shouldPlayOnTick : instrument.getBeat().toCharArray()) {
                    if (shouldPlayOnTick == '1') {
                        createNoteOnOff(track, instrument.getMidiNumber(), tickPosition);
                    }
                    tickPosition++;
                }
            }
            totalTickCount += (measure.getBeatUnit() * measure.getBeatsInMeasure());
        }

        sequencer.setSequence(sequence);
        sequencer.setTempoInBPM((float) song.getBpm());
        sequencer.start();
    }

    /**
     * Play all instruments in a measure once, using the given BPM.
     *
     * @param bpm     the speed at which the measure will play.
     * @param measure The measure to play all instrumens from.
     * @throws InvalidMidiDataException Indicates that inappropriate MIDI data
     *                                  was encountered.
     * @throws MidiUnavailableException is thrown when a requested MIDI component
     *                                  cannot be opened or created because it is unavailable.
     * @throws SQLException             If the instrumentCollection cannot be retrieved.
     */
    public void playMeasure(
            int bpm,
            Measure measure
    ) throws InvalidMidiDataException, MidiUnavailableException, SQLException {
        stopPlayback();
        sequencer = this.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        ObservableList<Instrument> instrumentCollection = instrumentRepository.fetchForMeasure(measure);

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

    /**
     * Stop playing audio.
     */
    public void stopPlayback() {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
        }
    }

    /**
     * Play a single note corresponding to the given value in the midi channel.
     *
     * @param value The key for which to play the sound in the midi channel.
     * @throws InvalidMidiDataException Indicates that inappropriate MIDI data
     *                                  was encountered.
     * @throws MidiUnavailableException is thrown when a requested MIDI component
     *                                  cannot be opened or created because it is unavailable.
     */
    public void playNote(Integer value) throws InvalidMidiDataException, MidiUnavailableException {
        stopPlayback();
        sequencer = this.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        createNoteOnOff(track, value, 0);

        sequencer.setSequence(sequence);
        sequencer.start();
    }

    /**
     * Create a midiEvent to play a the sound corresponding to the given midiKey on the given tick.
     * And create an Off event one tick later asking the player to stop the sound then.
     *
     * @param track   The AudioTrack to which the event should be added.
     * @param midiKey The midiKey corresponding to an index in the midi channel.
     * @param tick    The tick within the length of the audioTrack on which the sound should play.
     */
    private void createNoteOnOff(Track track, int midiKey, int tick) {
        track.add(makeEvent(ShortMessage.NOTE_ON, midiKey, tick));
        track.add(makeEvent(ShortMessage.NOTE_OFF, midiKey, tick + 1));
    }

    /**
     * Create a midi Event for the given command
     *
     * @param command The ShortMessage Command to execute in the event.
     * @param midiKey The midiKey corresponding to an index in the midi channel.
     * @param tick    The tick within the length of the audioTrack on which the event should be fired.
     * @return The newly created MidiEvent.
     */
    private MidiEvent makeEvent(int command, int midiKey, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage message = new ShortMessage();
            // 9 is the midi percussion channel, 100 is our default value for volume.
            message.setMessage(command, 9, midiKey, 100);
            event = new MidiEvent(message, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }
}

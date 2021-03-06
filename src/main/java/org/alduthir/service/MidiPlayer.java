package org.alduthir.service;

import org.alduthir.model.Instrument;
import org.alduthir.model.SongMeasure;
import org.alduthir.model.Measure;
import org.alduthir.model.Song;
import org.alduthir.repository.DataRetrievalException;
import org.alduthir.repository.InstrumentRepositoryInterface;
import org.alduthir.repository.MeasureRepositoryInterface;

import javax.sound.midi.*;
import java.util.List;

/**
 * Class MidiPlayer
 * <p>
 * A service for playing Sounds using the javax MidiSystem.
 */
public class MidiPlayer implements MusicPlayerInterface {
    // This is the percussion channel. Obviously we use this channel because this is a drumloop application
    final int MIDI_CHANNEL = 9;
    final int VOLUME = 100;

    private Sequencer sequencer;
    private InstrumentRepositoryInterface instrumentRepository;
    private MeasureRepositoryInterface measureRepository;

    /**
     * Constructor for MidiPlayer
     */
    public MidiPlayer(
            InstrumentRepositoryInterface instrumentRepository,
            MeasureRepositoryInterface measureRepository
    ) throws MidiUnavailableException {
        this.instrumentRepository = instrumentRepository;
        this.measureRepository = measureRepository;
        this.getSequencer();
    }

    /**
     * Retrieve the sequencer from the MidiSystem
     *
     * @return the default sequencer, connected to a default Receiver
     */
    @Override
    public Sequencer getSequencer() throws MidiUnavailableException {
        if (sequencer == null) {
            sequencer = MidiSystem.getSequencer();
        }

        return sequencer;
    }

    /**
     * Play the entire audio for a song. Creating individual tracks for each measure.
     *
     * @param song The song to play.
     */
    @Override
    public void playSong(Song song) throws MidiUnavailableException, InvalidMidiDataException, DataRetrievalException {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
            return;
        }

        sequencer = this.getSequencer();
        sequencer.open();
        List<SongMeasure> songMeasureCollection = measureRepository.fetchForSong(song);
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        int totalTickCount = 0;
        for (SongMeasure songMeasure : songMeasureCollection) {
            Measure measure = songMeasure.getMeasure();
            List<Instrument> instrumentCollection = instrumentRepository.fetchForMeasure(measure);
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
     */
    @Override
    public void playMeasure(
            int bpm,
            Measure measure
    ) throws InvalidMidiDataException, MidiUnavailableException, DataRetrievalException {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
            return;
        }

        sequencer = this.getSequencer();
        sequencer.open();
        Sequence sequence = new Sequence(Sequence.PPQ, 4);
        Track track = sequence.createTrack();

        List<Instrument> instrumentCollection = instrumentRepository.fetchForMeasure(measure);

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
     * Play a single note corresponding to the given value in the midi channel.
     *
     * @param value The key for which to play the sound in the midi channel.
     * @throws InvalidMidiDataException Indicates that inappropriate MIDI data
     *                                  was encountered.
     * @throws MidiUnavailableException is thrown when a requested MIDI component
     *                                  cannot be opened or created because it is unavailable.
     */
    @Override
    public void playNote(Integer value) throws InvalidMidiDataException, MidiUnavailableException {
        if (getSequencer().isRunning()) {
            getSequencer().stop();
            return;
        }

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
            message.setMessage(command, this.MIDI_CHANNEL, midiKey, this.VOLUME);
            event = new MidiEvent(message, tick);
        } catch (InvalidMidiDataException e) {
            e.printStackTrace();
        }
        return event;
    }
}

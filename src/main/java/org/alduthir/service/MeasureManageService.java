package org.alduthir.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.alduthir.model.Measure;
import org.alduthir.model.SongMeasure;
import org.alduthir.model.Song;
import org.alduthir.repository.MeasureRepositoryInterface;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class MeasureManageService
 * <p>
 * A service layer class containing CRUD functionality for Measures.
 */
public class MeasureManageService implements MeasureManageServiceInterface {
    private MeasureRepositoryInterface measureRepository;
    private final MusicPlayerInterface musicPlayer;

    /**
     * Constructor for MeasureManageService
     */
    public MeasureManageService(
            MeasureRepositoryInterface measureRepository,
            MusicPlayerInterface musicPlayer
    ) {
        this.musicPlayer = musicPlayer;
        this.measureRepository = measureRepository;
    }

    /**
     * @param song The song for which all SongMeasures should be fetched.
     * @return Returns a list of SongMeasures for the given Song.
     */
    @Override
    public List<SongMeasure> getSongMeasureCollection(Song song) {
        ObservableList<SongMeasure> songMeasureCollection = FXCollections.observableArrayList();
        try {
            return measureRepository.fetchForSong(song);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songMeasureCollection;
    }

    /**
     * Delete a SongMeasure. Use this instead of removing the Measure, since removing a measure would also delete
     * all it's reused occurences across different songs.
     *
     * @param songMeasure The SongMeasure to be deleted.
     */
    @Override
    public void deleteMeasure(SongMeasure songMeasure) {
        try {
            measureRepository.removeFromSong(songMeasure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the measure object in the db so that it's ID is set. Then use that ID as a foreign key to link it to
     * a song.
     *
     * @param measure  The newly created measure object.
     * @param song     The song to which the measure should be linked
     * @param sequence The position in the list to which the new measure should be added.
     */
    @Override
    public void createForSong(Measure measure, Song song, int sequence) {
        try {
            measure = measureRepository.createMeasure(measure);
            measureRepository.addToSong(measure, song, sequence);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Play an individual measure.
     *
     * @param measure The measure containing multiple instruments to be played
     * @param bpm     The speed at which the measure should be played.
     */
    @Override
    public void playMeasure(Measure measure, int bpm) {
        try {
            musicPlayer.playMeasure(bpm, measure);
        } catch (MidiUnavailableException | InvalidMidiDataException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request the repository to retrieve a hydrated list of Measures.
     *
     * @return An ObservableList of Measure models.
     */
    @Override
    public List<Measure> getAllMeasures() {
        try {
            return measureRepository.fetchAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Request the repository to add the given measure to the Song.
     *
     * @param song     The song to which the Measure should be added.
     * @param measure  The Measure to be added.
     * @param sequence The position of the new Measure in the Song's order of Measures.
     */
    @Override
    public void addToSong(Song song, Measure measure, int sequence) {
        try {
            measureRepository.addToSong(measure, song, sequence);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

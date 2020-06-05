package org.alduthir.service;

import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SongManageService
 * <p>
 * A service layer class containing CRUD functionality for Songs.
 */
public class SongManageService implements SongManageServiceInterface {

    private SongRepositoryInterface songRepository;
    private MeasureRepositoryInterface measureRepository;
    private MusicPlayerInterface musicPlayer;

    /**
     * Create the repository responsible for database communication.
     */
    public SongManageService(
            MeasureRepositoryInterface measureRepository,
            SongRepositoryInterface songRepository,
            MusicPlayerInterface musicPlayer
    ) {
        this.musicPlayer = musicPlayer;
        this.songRepository = songRepository;
        this.measureRepository = measureRepository;
    }

    /**
     * @return An ObservableList containing all Song records in the database.
     */
    @Override
    public List<Song> getSongCollection() {
        try {
            return songRepository.fetchAll();
        } catch (DataRetrievalException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Request the datalayer to create a record for a new song.
     *
     * @param songName the name of the new song. Other values use defaults
     */
    @Override
    public void createSong(String songName) {
        try {
            songRepository.createSong(songName);
        } catch (DataPersistanceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request the repostory to delete the selected item in the songList. Including each SongMeasure it is linked to.
     *
     * @param song The song to be deleted
     */
    @Override
    public void deleteSong(Song song) {
        try {
            List<SongMeasure> songMeasureCollection = measureRepository.fetchForSong(song);

            for (SongMeasure songMeasure : songMeasureCollection) {
                measureRepository.removeFromSong(songMeasure);
            }
            songRepository.deleteById(song.getId());
        } catch (DataRetrievalException | DataRemovalException e) {
            e.printStackTrace();
        }

    }

    /**
     * Use the musicplayer to play the given song.
     *
     * @param song the song to be played.
     */
    @Override
    public Boolean playSong(Song song) {
        try {
            musicPlayer.playSong(song);
        } catch (MidiUnavailableException | InvalidMidiDataException | DataRetrievalException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * @param song the song of which the bpm should be saved.
     */
    @Override
    public void updateBpm(Song song, int bpmValue) {
        try {
            songRepository.updateBpm(song, bpmValue);
        } catch (DataPersistanceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Song findSong(int id) {
        try {
            return songRepository.findById(id);
        } catch (DataRetrievalException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}

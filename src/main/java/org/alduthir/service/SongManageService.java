package org.alduthir.service;

import org.alduthir.model.Song;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class SongManageService
 * <p>
 * A service layer class containing CRUD functionality for Songs.
 */
public class SongManageService implements SongManageServiceInterface {

    private SongRepositoryInterface songRepositoryInterface;
    private MeasureRepositoryInterface measureRepositoryInterface;
    private MusicPlayerInterface musicPlayerInterface;

    /**
     * Create the repository responsible for database communication.
     */
    public SongManageService(
            MeasureRepositoryInterface measureRepositoryInterface,
            SongRepositoryInterface songRepositoryInterface,
            MusicPlayerInterface musicPlayerInterface
    ) {
        this.musicPlayerInterface = musicPlayerInterface;
        this.songRepositoryInterface = songRepositoryInterface;
        this.measureRepositoryInterface = measureRepositoryInterface;
    }

    /**
     * @return An ObservableList containing all Song records in the database.
     */
    public List<Song> getSongCollection() {
        List<Song> songList = new ArrayList<>();
        try {
            return songRepositoryInterface.fetchAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return songList;
    }

    /**
     * Request the datalayer to create a record for a new song.
     *
     * @param songName the name of the new song. Other values use defaults
     */
    public void createSong(String songName) {
        try {
            songRepositoryInterface.createSong(songName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Request the repostory to delete the selected item in the songList. Including each SongMeasure it is linked to.
     *
     * @param song The song to be deleted
     */
    public void deleteSong(Song song) {
        try {
            List<SongMeasure> songMeasureCollection = measureRepositoryInterface.fetchForSong(song);

            for (SongMeasure songMeasure : songMeasureCollection) {
                measureRepositoryInterface.removeFromSong(songMeasure);
            }
            songRepositoryInterface.deleteById(song.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use the musicplayer to play the given song.
     *
     * @param song the song to be played.
     */
    public void playSong(Song song) {
        try {
            musicPlayerInterface.playSong(song);
        } catch (MidiUnavailableException | SQLException | InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param song the song of which the bpm should be saved.
     */
    @Override
    public void updateBpm(Song song) {
        try {
            songRepositoryInterface.updateBpm(song);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

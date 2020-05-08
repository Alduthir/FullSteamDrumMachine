package org.alduthir.service;

import com.jfoenix.controls.JFXListView;
import javafx.collections.ObservableList;
import org.alduthir.model.Song;
import org.alduthir.component.StyledTextInputDialog;
import org.alduthir.model.SongMeasure;
import org.alduthir.repository.MeasureRepository;
import org.alduthir.repository.MeasureRepositoryInterface;
import org.alduthir.repository.SongRepository;
import org.alduthir.repository.SongRepositoryInterface;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;
import java.util.Optional;

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
    public SongManageService() {
        musicPlayerInterface = new MidiPlayer();
        try {
            songRepositoryInterface = new SongRepository();
            measureRepositoryInterface = new MeasureRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Fill the songList with an ObservableList of hydrated Song Objects.
     *
     * @param songList the list to be filled.
     */
    @Override
    public void initializeSongList(JFXListView<Song> songList) {
        try {
            songList.getItems().setAll(songRepositoryInterface.fetchAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a dialog requesting a name for the new song. If the dialog is given a result, creates the new song and
     * re-initialises the list so the new song is included.
     *
     * @param songList The list to be reinitialised including the new song.
     */
    @Override
    public void addSong(JFXListView<Song> songList) {
        var dialog = new StyledTextInputDialog();
        dialog.setTitle("Create new Song");
        dialog.setContentText("Enter the name of your new song.");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                songRepositoryInterface.createSong(result.get());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeSongList(songList);
        }
    }

    /**
     * Request the repostory to delete the selected item in the songList. Then re-initialises the songList so the
     * removed song is no longer in it.
     *
     * @param songList Used to retrieve the selectedItem and to be reloaded.
     */
    @Override
    public void deleteSong(JFXListView<Song> songList) {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            try {
                ObservableList<SongMeasure> songMeasureCollection = measureRepositoryInterface.fetchForSong(selectedSong);

                for (SongMeasure songMeasure : songMeasureCollection) {
                    measureRepositoryInterface.removeFromSong(songMeasure);
                }
                songRepositoryInterface.deleteById(selectedSong.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeSongList(songList);
        }
    }

    /**
     * Play the audio for every measure in the song sequentially.
     *
     * @param songList The songlist from which to play the current selection.
     */
    @Override
    public void playSelectedSong(JFXListView<Song> songList) {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            try {
                musicPlayerInterface.playSong(selectedSong);
            } catch (MidiUnavailableException | SQLException | InvalidMidiDataException e) {
                e.printStackTrace();
            }
        }
    }
}

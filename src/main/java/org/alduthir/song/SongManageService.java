package org.alduthir.song;

import com.jfoenix.controls.JFXListView;
import org.alduthir.util.StyledTextInputDialog;

import java.sql.SQLException;
import java.util.Optional;

public class SongManageService {

    private SongRepository repository;

    public SongManageService() {
        try {
            repository = new SongRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeSongList(JFXListView<Song> songList) {
        try {
            songList.getItems().setAll(repository.fetchAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSong(JFXListView<Song> songList)  {
        var dialog = new StyledTextInputDialog();
        dialog.setTitle("Create new Song");
        dialog.setContentText("Enter the name of your new song.");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            Song song = new Song(result.get());
            try {
                repository.createSong(song);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeSongList(songList);
        }
    }

    public void deleteSong(JFXListView<Song> songList) {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            try {
                repository.deleteById(selectedSong.getId());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            initializeSongList(songList);
        }
    }
}

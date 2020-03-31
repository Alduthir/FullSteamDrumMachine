package org.alduthir.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.song.Song;
import org.alduthir.song.SongCellFactory;
import org.alduthir.song.SongManageService;

public class SongController extends App implements Initializable {

    private final SongManageService songManageService;

    @FXML
    public JFXListView<Song> songList;
    public JFXButton addSongButton;
    public JFXButton playButton;
    public JFXButton editButton;
    public JFXButton exportButton;
    public JFXButton deleteButton;

    public SongController() {
        this.songManageService = new SongManageService();
    }

    @FXML
    public void redirectToMeasureScreen() throws IOException {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if (selectedSong != null) {
            Stage stage = (Stage) songList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/measureScreen.fxml"));
            Parent root = loader.load();
            MeasureController controller = loader.getController();
            controller.initialize(selectedSong);
            stage.setScene(new Scene(root, 800, 400));
            stage.setTitle("Full Steam Drum Machine - " + selectedSong.toString());
            stage.show();
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        try {
            songManageService.initializeSongList(songList);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        songList.setCellFactory(new SongCellFactory());
        songList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    redirectToMeasureScreen();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.consume();
            }
        });
        songList.getSelectionModel().selectFirst();
        songList.requestFocus();
    }

    public void addAction() throws SQLException {
        songManageService.addSong(songList);
    }

    public void deleteAction() throws SQLException {
        songManageService.deleteSong(songList);
    }

    public void playAction() {
        notYetImplemented();
    }

    public void exportAction() {
        notYetImplemented();
    }
}
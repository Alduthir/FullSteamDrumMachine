package org.alduthir.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.model.Song;
import org.alduthir.component.factory.SongCellFactory;
import org.alduthir.service.SongManageService;

/**
 * Class SongController
 * <p>
 * The homescreen of the application. Manages controls for Songs.
 */
public class SongController extends App implements Initializable {

    private final SongManageService songManageService;

    @FXML
    public JFXListView<Song> songList;

    public SongController() {
        this.songManageService = new SongManageService();
    }

    /**
     * Redirect to the Measure management screen for the selected Song.
     *
     * @throws IOException if no resource can be loaded from gui/measureScreen.fxml.
     */
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

    /**
     * Implmentation of Initializable.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        songManageService.initializeSongList(songList);

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

    /**
     * Ask the songManageService to create a dialog for creating a new Song, and add it to the list.
     */
    public void addAction() {
        songManageService.addSong(songList);
    }

    /**
     * Ask the songManageService to delete a Song and remove it from the list.
     */
    public void deleteAction() {
        songManageService.deleteSong(songList);
    }

    /**
     * Ask the songManageService to play the audio for the entire selected Song.
     */
    public void playAction() {
        songManageService.playSelectedSong(songList);
    }

    /**
     * ToDo (COULD HAVE)
     * Export the song to an audio file.
     */
    public void exportAction() {
        notYetImplemented();
    }
}
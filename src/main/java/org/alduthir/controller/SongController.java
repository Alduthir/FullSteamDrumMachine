package org.alduthir.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.component.StyledTextInputDialog;
import org.alduthir.listener.BpmActionListener;
import org.alduthir.model.Song;
import org.alduthir.component.factory.SongCellFactory;
import org.alduthir.service.SongManageService;
import org.alduthir.service.SongManageServiceInterface;

/**
 * Class SongController
 * <p>
 * The homescreen of the application. Manages controls for Songs.
 */
public class SongController extends App implements Initializable, BpmActionListener {

    @FXML
    public JFXListView<Song> songList;

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
        initializeSongList();

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
        var dialog = new StyledTextInputDialog();
        dialog.setTitle("Create new Song");
        dialog.setContentText("Enter the name of your new song.");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            songManageServiceInterface.createSong(result.get());
            initializeSongList();
        }
    }

    /**
     * Ask the songManageService to delete a Song and remove it from the list.
     */
    public void deleteAction() {
        Song toDelete = songList.getSelectionModel().getSelectedItem();
        if (toDelete != null) {
            songManageServiceInterface.deleteSong(toDelete);
            initializeSongList();
        }
    }

    /**
     * Ask the songManageService to play the audio for the entire selected Song.
     */
    public void playAction() {
        Song toBePlayed = songList.getSelectionModel().getSelectedItem();
        if (toBePlayed != null) {
            songManageServiceInterface.playSong(toBePlayed);
        }
    }

    /**
     * Fill the songList with an ObservableList of hydrated Song Objects.
     */
    public void initializeSongList() {
        songList.getItems().setAll(
                FXCollections.observableArrayList(
                        songManageServiceInterface.getSongCollection()
                )
        );
    }

    @Override
    public void updateAction(Song song) {
        songManageServiceInterface.updateBpm(song);
    }
}
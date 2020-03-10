package org.alduthir.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.song.Song;
import org.alduthir.song.SongCellFactory;
import org.alduthir.song.SongRepository;

public class SongController extends App implements Initializable {

    @FXML
    public JFXListView<Song> songList;
    public JFXButton addSongButton;
    public JFXButton playButton;
    public JFXButton editButton;
    public JFXButton exportButton;
    public JFXButton deleteButton;

    private ObservableList<Song> songCollection = FXCollections.observableArrayList();
    private SongRepository repository;

    @FXML
    public void switchToMeasureScreen() throws IOException {
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
        if (repository == null) {
            repository = new SongRepository();
        }

        try {
            songList.getItems().addAll(createSongList());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        songList.setCellFactory(new SongCellFactory());
        songList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    switchToMeasureScreen();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.consume();
            }
        });
        songList.getSelectionModel().selectFirst();
        songList.requestFocus();
    }

    private ObservableList<Song> createSongList() throws SQLException, ClassNotFoundException {
        songCollection = repository.fetchAll();
        return songCollection;
    }

    public void addSong(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        TextInputDialog textInputDialog = new TextInputDialog();
        styleStringDialog(textInputDialog);

        textInputDialog.setTitle("Create new Song");
        textInputDialog.setContentText("Enter the name of your new song.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Song song = new Song(result.get());
            repository.createSong(song);
            songCollection.add(song);
            songList.getItems().setAll(songCollection);
        }
    }

    public void deleteSong(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if(selectedSong != null) {
            repository.deleteById(selectedSong.getId());
            songCollection.remove(selectedSong);
            songList.getItems().setAll(songCollection);
        }
    }
}
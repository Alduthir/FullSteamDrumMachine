package org.alduthir;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import org.alduthir.Song.Song;
import org.alduthir.Song.SongCellFactory;

public class SongController extends App implements Initializable {

    @FXML
    public JFXListView<Song> songList;
    public JFXButton addSongButton;
    public JFXButton playButton;
    public JFXButton editButton;
    public JFXButton exportButton;
    public JFXButton deleteButton;

    public ObservableList<Song> items = FXCollections.observableArrayList();

    @FXML
    public void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        songList.getItems().addAll(createSongList());
        songList.setCellFactory(new SongCellFactory());
        songList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {
            @Override
            public void changed(ObservableValue<? extends Song> observableValue, Song song, Song t1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Wollah");
                alert.setContentText("Je hebt op " + song.toString() + " geklikt");
                alert.showAndWait();
            }
        });
    }

    private ObservableList<Song> createSongList() {
        Song testSong = new Song(1, "Test1");
        items.add(testSong);

        Song testSong2 = new Song(2, "Test2");
        items.add(testSong2);

        return items;
    }
}
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
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
    public void notYetImplemented() throws IOException {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Not yet implemented");
        alert.setContentText("Deze functionaliteit is nog niet geïmplementeerd.");
        alert.showAndWait();
    }

    public void switchToMeasureScreen() throws IOException {
        Song selectedSong = songList.getSelectionModel().getSelectedItem();
        if(selectedSong != null){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("measureScreen"));
            MeasureController controller = loader.getController();
//        controller.setSong(song);
            App.setRoot("measureScreen");
        }
    }

    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        songList.getItems().addAll(createSongList());
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
    }

    private ObservableList<Song> createSongList() {
        Song testSong = new Song(1, "Test1");
        items.add(testSong);

        Song testSong2 = new Song(2, "Test2");
        items.add(testSong2);

        return items;
    }
}
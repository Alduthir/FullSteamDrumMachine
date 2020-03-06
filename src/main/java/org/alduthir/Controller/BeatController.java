package org.alduthir.Controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.Beat.Beat;
import org.alduthir.Beat.BeatCellFactory;
import org.alduthir.Beat.NoSelectionModel;
import org.alduthir.Measure.Measure;
import org.alduthir.Song.Song;

import java.io.IOException;

public class BeatController extends App {

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Beat> beatList;

    private Song song;
    private Measure measure;
    private ObservableList<Beat> beatCollection = FXCollections.observableArrayList();

    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;
        Beat test1 = new Beat("Bass Drum");
        beatCollection.add(test1);

        Beat test2 = new Beat("Hihat");
        beatCollection.add(test2);

        beatList.setSelectionModel(new NoSelectionModel<Beat>());
        beatList.getItems().addAll(beatCollection);
        beatList.setCellFactory(new BeatCellFactory());
    }

    @FXML
    public void switchToMeasure() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/measureScreen.fxml"));
        Parent root = loader.load();
        MeasureController controller = loader.getController();
        controller.initialize(this.song);
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine - " + song.toString());
        stage.show();
    }
}
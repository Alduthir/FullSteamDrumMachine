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
import org.alduthir.Instrument.Instrument;
import org.alduthir.Instrument.InstrumentCellFactory;
import org.alduthir.Instrument.NoSelectionModel;
import org.alduthir.Measure.Measure;
import org.alduthir.Song.Song;

import java.io.IOException;

public class BeatController extends App {

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Instrument> beatList;

    private Song song;
    private Measure measure;
    private ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();

    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;
        Instrument test1 = new Instrument("Bass Drum");
        instrumentCollection.add(test1);

        Instrument test2 = new Instrument("Hihat");
        instrumentCollection.add(test2);

        beatList.setSelectionModel(new NoSelectionModel<Instrument>());
        beatList.getItems().addAll(instrumentCollection);
        beatList.setCellFactory(new InstrumentCellFactory());
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
package org.alduthir.controller;

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
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentCellFactory;
import org.alduthir.instrument.InstrumentRepository;
import org.alduthir.util.NoSelectionModel;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;

import java.io.IOException;
import java.sql.SQLException;

public class BeatController extends App {

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Instrument> beatList;

    private Song song;
    private Measure measure;
    private ObservableList<Instrument> instrumentCollection = FXCollections.observableArrayList();
    private InstrumentRepository repository;

    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;
        getRepository();

        try {
            initializeInstrumentCollection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToMeasureSelection() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/measureScreen.fxml"));
        Parent root = loader.load();
        MeasureController controller = loader.getController();
        controller.initialize(this.song);
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine - " + song.toString());
        stage.show();
    }

    public void addInstrument() {
        notYetImplemented();
    }

    public void removeInstrument(Measure measure, Instrument instrument) throws SQLException {
        getRepository().removeFromMeasure(measure, instrument);
        instrumentCollection.remove(instrument);
        beatList.getItems().setAll(instrumentCollection);
    }

    public void playMeasure() {
        notYetImplemented();
    }

    private void initializeInstrumentCollection() throws SQLException {
        beatList.getItems().setAll(
                getRepository().fetchForMeasure(measure)
        );
        beatList.setSelectionModel(new NoSelectionModel<>());
        beatList.setCellFactory(new InstrumentCellFactory(measure));
    }

    private InstrumentRepository getRepository() {
        if (repository == null) {
            try {
                repository = new InstrumentRepository();
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return repository;
        }
        return repository;
    }
}
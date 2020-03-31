package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentActionListener;
import org.alduthir.instrument.InstrumentCellFactory;
import org.alduthir.instrument.InstrumentManageService;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;

import java.io.IOException;
import java.sql.SQLException;

public class BeatController extends App implements InstrumentActionListener {
    private final InstrumentManageService instrumentManageService;

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Instrument> beatList;

    private Song song;
    private Measure measure;

    public BeatController() {
        this.instrumentManageService = new InstrumentManageService();
    }

    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;

        beatList.setCellFactory(new InstrumentCellFactory(this));

        try {
            instrumentManageService.initializeInstrumentCollection(measure, beatList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void redirectToMeasureSelection() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/measureScreen.fxml"));
        Parent root = loader.load();
        MeasureController controller = loader.getController();
        controller.initialize(this.song);
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine - " + song.toString());
        stage.show();
    }

    public void addAction() {
        notYetImplemented();
    }

    public void playAction() {
        notYetImplemented();
    }

    @Override
    public void removeAction(Instrument instrument) {
        instrumentManageService.removeInstrument(measure, instrument, beatList);
    }

    @Override
    public void updateAction(String beatNotes, Instrument instrument) {
        notYetImplemented();
    }
}
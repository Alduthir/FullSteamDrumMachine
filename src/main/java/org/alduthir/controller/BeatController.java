package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentActionListener;
import org.alduthir.instrument.InstrumentCellFactory;
import org.alduthir.instrument.InstrumentManageService;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;
import org.alduthir.util.MidiPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.IOException;
import java.sql.SQLException;

public class BeatController extends App implements InstrumentActionListener {
    private final InstrumentManageService instrumentManageService;

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Instrument> beatList;

    private MidiPlayer midiPlayer;
    private Song song;
    private Measure measure;

    public BeatController() {
        this.instrumentManageService = new InstrumentManageService();
        this.midiPlayer = new MidiPlayer();
    }

    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;

        beatList.setCellFactory(new InstrumentCellFactory(this));
        instrumentManageService.initializeInstrumentCollection(measure, beatList);
    }

    @FXML
    public void redirectToMeasureSelection(ActionEvent mouseEvent) throws IOException {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/measureScreen.fxml"));
        Parent root = loader.load();
        MeasureController controller = loader.getController();
        controller.initialize(this.song);
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine - " + song.toString());
        stage.show();
    }

    public void addAction(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/addInstrumentDialog.fxml"));
        Parent parent = loader.load();
        AddInstrumentDialogController dialogController = loader.getController();
        dialogController.initialize(measure);

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(parent, 400, 300));
        dialog.setTitle("Add instrument");
        dialog.showAndWait();


        instrumentManageService.initializeInstrumentCollection(measure, beatList);
    }

    public void playAction() {
        try {
            midiPlayer.playMeasure(song.getBpm(), measure);
        } catch (InvalidMidiDataException | MidiUnavailableException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAction(Instrument instrument) {
        instrumentManageService.removeInstrument(measure, instrument, beatList);
    }

    @Override
    public void updateAction(String beatNotes, Instrument instrument) {
        instrumentManageService.updateNotes(measure, instrument, beatNotes);
    }
}
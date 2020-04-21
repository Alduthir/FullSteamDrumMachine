package org.alduthir.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.alduthir.measure.Measure;
import org.alduthir.measure.MeasureManageService;
import org.alduthir.song.Song;
import org.alduthir.util.MidiPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.sql.SQLException;

public class ReuseMeasureDialogController {

    public JFXComboBox<Measure> measureComboBox;
    private Song song;
    private int sequence;
    private MeasureManageService measureManageService;
    private MidiPlayer midiPlayer;

    public void initialize(Song song, int sequence) {
        this.song = song;
        this.sequence = sequence;

        measureManageService = new MeasureManageService();
        midiPlayer = new MidiPlayer();


        ObservableList<Measure> measureObservableList = measureManageService.fetchAll();
        measureComboBox.getItems().setAll(measureObservableList);
        measureComboBox.getSelectionModel().selectFirst();
    }

    private void closeStage(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void previewMeasure() {
        Measure selectedMeasure = measureComboBox.getSelectionModel().getSelectedItem();
        if(selectedMeasure != null){
            try {
                midiPlayer.playMeasure(song.getBpm(), selectedMeasure);
            } catch (InvalidMidiDataException | MidiUnavailableException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveReuse(ActionEvent actionEvent) {
        Measure selectedMeasure = measureComboBox.getSelectionModel().getSelectedItem();
        if(selectedMeasure != null){
            measureManageService.addToSong(song, selectedMeasure, sequence);
        }
        closeStage(actionEvent);
    }
}

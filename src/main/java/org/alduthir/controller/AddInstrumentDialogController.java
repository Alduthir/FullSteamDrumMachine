package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentManageService;
import org.alduthir.measure.Measure;
import org.alduthir.util.AddInstrumentOption;
import org.alduthir.util.MidiPlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.io.File;

public class AddInstrumentDialogController {
    public JFXComboBox<AddInstrumentOption> newOrReuseSelection;
    public JFXComboBox<Instrument> reuseComboBox;
    public JFXButton reuseButton;
    public TextField newNameField;
    public JFXButton newButton;
    public VBox reuseBox;
    public VBox newBox;
    public VBox rootBox;
    public HBox selectionBox;
    public Spinner<Integer> noteSpinner;

    private InstrumentManageService instrumentManageService;
    private MidiPlayer midiPlayer;
    private Measure measure;

    public void initialize(Measure measure) {
        this.measure = measure;
        instrumentManageService = new InstrumentManageService();
        midiPlayer = new MidiPlayer();
        ObservableList<AddInstrumentOption> newOrReuseOptionCollection = FXCollections.observableArrayList();
        newOrReuseOptionCollection.add(AddInstrumentOption.NEW);
        newOrReuseOptionCollection.add(AddInstrumentOption.REUSE);
        newOrReuseSelection.getItems().setAll(newOrReuseOptionCollection);
        newOrReuseSelection.getSelectionModel().selectedItemProperty().addListener((ov, oldValue, newValue) -> {
            switch (newValue) {
                case NEW:
                    reuseBox.setVisible(false);
                    newBox.setVisible(true);
                    break;
                case REUSE:
                    newBox.setVisible(false);
                    reuseBox.setVisible(true);
                    break;
            }
        });
        newOrReuseSelection.getSelectionModel().selectFirst();
        instrumentManageService.initializeReuseOptionCollection(measure, reuseComboBox);
        instrumentManageService.initializeInstrumentSpinner(noteSpinner);

        if (reuseComboBox.getItems().toArray().length == 0) {
            selectionBox.setVisible(false);
        }
    }

    public void saveNewInstrument(ActionEvent actionEvent) {
        if (newNameField.getText().isEmpty()) {
            return;
        }

        instrumentManageService.saveNewInstrument(newNameField.getText(), noteSpinner.getValue(), measure);
        closeStage(actionEvent);
    }

    public void saveReuse(ActionEvent actionEvent) {
        if (reuseComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        instrumentManageService.reuseInstrument(reuseComboBox.getSelectionModel().getSelectedItem(), measure);
        closeStage(actionEvent);
    }

    private void closeStage(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void playMidiNote(ActionEvent actionEvent) {
        try {
            midiPlayer.playNote(noteSpinner.getValue());
        } catch (MidiUnavailableException|InvalidMidiDataException e) {
            e.printStackTrace();
        }
    }
}

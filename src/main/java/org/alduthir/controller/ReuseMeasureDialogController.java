package org.alduthir.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.alduthir.model.Measure;
import org.alduthir.service.MeasureManageService;
import org.alduthir.model.Song;
import org.alduthir.service.MeasureManageServiceInterface;

/**
 * Class ReuseMeasureDialogController
 * <p>
 * A dialog for reusing an existing Measure and adding it to the given Song.
 */
public class ReuseMeasureDialogController {
    private final MeasureManageServiceInterface measureManageServiceInterface;

    @FXML
    public JFXComboBox<Measure> measureComboBox;

    private Song song;
    private int sequence;

    /**
     * Create necessary Service level dependencies on construction.
     */
    public ReuseMeasureDialogController() {
        measureManageServiceInterface = new MeasureManageService();
    }

    /**
     * Initialise the ObservableLists with data and set required fields.
     *
     * @param song     The Song to which the measure will be added.
     * @param sequence The index at which the measure will be added.
     */
    public void initialize(Song song, int sequence) {
        this.song = song;
        this.sequence = sequence;

        ObservableList<Measure> measureObservableList = measureManageServiceInterface.fetchAll();
        measureComboBox.getItems().setAll(measureObservableList);
        measureComboBox.getSelectionModel().selectFirst();
    }

    /**
     * Close the dialog.
     *
     * @param actionEvent required to retrieve the Source UI element.
     */
    private void closeStage(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    /**
     * Preview the audio of the selected Measure.
     */
    public void previewMeasure() {
        Measure selectedMeasure = measureComboBox.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            measureManageServiceInterface.playMeasure(selectedMeasure, song.getBpm());
        }
    }

    /**
     * Finalise the addition of the selected Measure into the Song, and close the dialog.
     *
     * @param actionEvent passed to closeStage to close the dialog.
     */
    public void saveReuse(ActionEvent actionEvent) {
        Measure selectedMeasure = measureComboBox.getSelectionModel().getSelectedItem();
        if (selectedMeasure != null) {
            measureManageServiceInterface.addToSong(song, selectedMeasure, sequence);
        }
        closeStage(actionEvent);
    }
}

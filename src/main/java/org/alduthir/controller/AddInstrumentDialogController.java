package org.alduthir.controller;

import com.jfoenix.controls.JFXComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.component.InstrumentSpinner;
import org.alduthir.model.Instrument;
import org.alduthir.service.InstrumentManageService;
import org.alduthir.model.Measure;
import org.alduthir.service.InstrumentManageServiceInterface;

/**
 * Class AddInstrumentDialogController
 * <p>
 * Contains and controls GUI elements pertaining to adding a new or existing Instrument to a Measure.
 */
public class AddInstrumentDialogController extends App {
    private InstrumentManageServiceInterface instrumentManageServiceInterface;

    @FXML
    public JFXComboBox<AddInstrumentOption> newOrReuseSelection;
    public JFXComboBox<Instrument> reuseComboBox;
    public TextField newNameField;
    public VBox reuseBox;
    public VBox newBox;
    public HBox selectionBox;
    public InstrumentSpinner instrumentSpinner;

    private Measure measure;

    /**
     * Create service level dependency on construction.
     */
    public AddInstrumentDialogController() {
        this.instrumentManageServiceInterface = new InstrumentManageService(
                instrumentRepositoryInterface,
                musicPlayerInterface
        );
    }

    /**
     * Ensures that the right input fields are hidden/shown and initializes the list of reuse options.
     *
     * @param measure the Measure is required because that is what we will add the Instrument to.
     */
    public void initialize(
            Measure measure
    ) {
        this.measure = measure;

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

        reuseComboBox.getItems().setAll(instrumentManageServiceInterface.getReuseOptionCollection(measure));
        reuseComboBox.getSelectionModel().selectFirst();

        if (reuseComboBox.getItems().toArray().length == 0) {
            selectionBox.setVisible(false);
            newBox.setVisible(true);
        }
    }

    /**
     * Calls the InstrumentManageService to save a new Instrument with the input name and midi key.
     *
     * @param actionEvent passed to closeStage in order to close the modal.
     */
    public void saveNewInstrument(ActionEvent actionEvent) {
        if (newNameField.getText().isEmpty()) {
            return;
        }
        instrumentManageServiceInterface.saveNewInstrument(newNameField.getText(), instrumentSpinner.getValue(), measure);

        closeStage(actionEvent);
    }

    /**
     * Reuse an existing Instrument in the current Measure.
     *
     * @param actionEvent passed to closeStage in order to close the modal.
     */
    public void saveReuse(ActionEvent actionEvent) {
        if (reuseComboBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        instrumentManageServiceInterface.reuseInstrument(reuseComboBox.getSelectionModel().getSelectedItem(), measure);
        closeStage(actionEvent);
    }

    /**
     * Ask the midiplayer to play a single note with the midiKey equal to the value of the spinner.
     */
    public void playMidiNote() {
        instrumentManageServiceInterface.playNote(instrumentSpinner.getValue());
    }

    /**
     * Close the modal.
     *
     * @param actionEvent required to retrieve the Node to be closed.
     */
    private void closeStage(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

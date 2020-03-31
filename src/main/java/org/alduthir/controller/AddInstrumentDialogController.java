package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.alduthir.instrument.Instrument;
import org.alduthir.instrument.InstrumentManageService;
import org.alduthir.measure.Measure;
import org.alduthir.util.AddInstrumentOption;

import java.io.File;

public class AddInstrumentDialogController {
    public JFXComboBox<AddInstrumentOption> newOrReuseSelection;
    public JFXComboBox<Instrument> reuseComboBox;
    public JFXButton reuseButton;
    public JFXTextField newNameField;
    public JFXButton chooseFileButton;
    public JFXButton newButton;
    public VBox reuseBox;
    public VBox newBox;

    private FileChooser fileChooser;
    private File selectedFile;
    private InstrumentManageService instrumentManageService;
    private Measure measure;

    public void initialize(Measure measure) {
        this.measure = measure;
        instrumentManageService = new InstrumentManageService();
        newOrReuseSelection.getItems().setAll(AddInstrumentOption.NEW, AddInstrumentOption.REUSE);
        instrumentManageService.initializeReuseOptionCollection(measure, reuseComboBox);
        fileChooser = new FileChooser();
    }

    public void changeVisibility(ActionEvent actionEvent) {
        var selectedOption = newOrReuseSelection.getSelectionModel().getSelectedItem();
        switch (selectedOption) {
            case NEW:
                reuseBox.setVisible(false);
                newBox.setVisible(true);
                break;
            case REUSE:
                newBox.setVisible(false);
                reuseBox.setVisible(true);
                break;
        }
    }

    public void saveNewInstrument(MouseEvent mouseEvent) {
        instrumentManageService.saveNewInstrument(newNameField.getText(), selectedFile, measure);
        closeStage(mouseEvent);
    }

    public void saveReuse(MouseEvent mouseEvent) {
        instrumentManageService.reuseInstrument(reuseComboBox.getSelectionModel().getSelectedItem(), measure);
        closeStage(mouseEvent);
    }

    public void openFileDialog(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
    }

    private void closeStage(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

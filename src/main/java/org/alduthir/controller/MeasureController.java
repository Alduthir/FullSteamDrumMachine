package org.alduthir.controller;

import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Spinner;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.component.BpmSpinner;
import org.alduthir.model.SongMeasure;
import org.alduthir.component.factory.MeasureCellFactory;
import org.alduthir.model.Song;
import org.alduthir.service.MeasureManageService;
import org.alduthir.service.MeasureManageServiceInterface;

import java.io.IOException;

/**
 * Class MeasureController
 * <p>
 * The MeasureController contains a list of all Measures within the Song. And controls UI elements for adding, managing
 * or previewing these Measures.
 */
public class MeasureController extends App {
    private final MeasureManageServiceInterface measureManageServiceInterface;
    private final BpmSpinner bpmSpinner;

    @FXML
    public JFXListView<SongMeasure> measureList;
    public Spinner<Integer> spinner;

    private Song song;

    /**
     * Create necessary service layer dependencies on construction.
     */
    public MeasureController() {
        this.measureManageServiceInterface = new MeasureManageService();
        this.bpmSpinner = new BpmSpinner();
    }

    /**
     * Initialise the ListCell Factories, ListView and BPM Spinner UI elements.
     *
     * @param song This determines which measures must be retrieved and is passed down the chain to other controller
     *             initialize functions.
     */
    public void initialize(Song song) {
        this.song = song;
        measureManageServiceInterface.initializeMeasureList(song, measureList);
        bpmSpinner.initializeBpmSpinner(song, spinner);

        measureList.setCellFactory(new MeasureCellFactory());
        measureList.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                try {
                    redirectToBeatEditor();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                e.consume();
            }
        });
        measureList.getSelectionModel().selectFirst();
        measureList.requestFocus();
    }

    /**
     * Change the Stage to the beatScreen allowing a user to edit a single Measure.
     *
     * @throws IOException if no resource can be loaded from gui/beatScreen.fxml.
     */
    public void redirectToBeatEditor() throws IOException {
        SongMeasure songMeasure = measureList.getSelectionModel().getSelectedItem();
        if (songMeasure != null) {
            Stage stage = (Stage) measureList.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/beatScreen.fxml"));
            Parent root = loader.load();
            BeatController controller = loader.getController();
            controller.initialize(song, songMeasure.getMeasure());
            stage.setScene(new Scene(root, 800, 400));
            stage.setTitle(
                    String.format(
                            "Full Steam Drum Machine - %s - %s",
                            song.toString(),
                            songMeasure.toString()
                    )
            );
            stage.show();
        }
    }

    /**
     * Redirect back to the home screen.
     *
     * @throws IOException if no Resource can be loaded from gui/songScreen.fxml
     */
    public void redirectToSongSelection(ActionEvent mouseEvent) throws IOException {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/songScreen.fxml"));
        Parent root = loader.load();
        stage.setScene(new Scene(root, 800, 400));
        stage.setTitle("Full Steam Drum Machine");
        stage.show();
    }

    /**
     * Ask the ManageService to play the audio for the currently selected measure.
     */
    public void playAction() {
        measureManageServiceInterface.playSelectedSongMeasure(song, measureList);
    }

    /**
     * Ask the ManageService to open a dialog for creating a new measure and Add it to the song.
     */
    public void addAction() {
        measureManageServiceInterface.addMeasure(song, measureList);
    }

    /**
     * Ask the mManageService to remove a Measure from the Song.
     */
    public void deleteAction() {
        measureManageServiceInterface.deleteMeasure(song, measureList);
    }

    /**
     * Open a dialog to reuse a Measure in the Song, the new Measure is added to the end of the ListView.
     *
     * @throws IOException if no resource can be loaded from gui/reuseMeasureDialog.fxml.
     */
    public void reuseAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/reuseMeasureDialog.fxml"));
        Parent parent = loader.load();
        ReuseMeasureDialogController dialogController = loader.getController();
        dialogController.initialize(song, measureList.getItems().size());

        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setScene(new Scene(parent, 400, 300));
        dialog.setTitle("Reuse measure");
        dialog.showAndWait();

        measureManageServiceInterface.initializeMeasureList(song, measureList);
    }

    /**
     * ToDo (COULD HAVE) not yet implemented.
     * Save the sequence of all measures based on their current order in the ListView.
     */
    public void saveSequence() {
        notYetImplemented();
    }
}
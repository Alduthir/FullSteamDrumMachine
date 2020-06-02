package org.alduthir.controller;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.alduthir.App;
import org.alduthir.component.BpmSpinner;
import org.alduthir.component.StyledTextInputDialog;
import org.alduthir.model.Measure;
import org.alduthir.model.SongMeasure;
import org.alduthir.component.factory.MeasureCellFactory;
import org.alduthir.model.Song;

import java.io.IOException;
import java.util.Optional;

/**
 * Class MeasureController
 * <p>
 * The MeasureController contains a list of all Measures within the Song. And controls UI elements for adding, managing
 * or previewing these Measures.
 */
public class MeasureController extends App {

    @FXML
    public JFXListView<SongMeasure> measureList;
    public BpmSpinner bpmSpinner;

    private Song song;

    /**
     * Initialise the ListCell Factories, ListView and BPM Spinner UI elements.
     *
     * @param song This determines which measures must be retrieved and is passed down the chain to other controller
     *             initialize functions.
     */
    public void initialize(Song song) throws IOException {
        this.song = song;
        initializeMeasureList(song, measureList);

        FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/songScreen.fxml"));
        loader.load();
        SongController songController = loader.getController();
        bpmSpinner.addListener(songController);
        bpmSpinner.initializeBpmSpinner(song, bpmSpinner);

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
        SongMeasure songMeasure = measureList.getSelectionModel().getSelectedItem();

        if (songMeasure != null) {
            measureManageServiceInterface.playMeasure(songMeasure.getMeasure(), song.getBpm());
        }
    }

    /**
     * Ask the ManageService to open a dialog for creating a new measure and Add it to the song.
     */
    public void addAction() {
        TextInputDialog textInputDialog = new StyledTextInputDialog();
        textInputDialog.getDialogPane().getStylesheets().add(App.class.getResource("styles.css").toExternalForm());
        textInputDialog.setTitle("Create new Measure");
        textInputDialog.setContentText("Enter the name of your new measure.");

        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            Measure measure = new Measure(result.get());
            measureManageServiceInterface.createForSong(measure, song, measureList.getItems().size());
        }

        initializeMeasureList(song, measureList);
    }

    /**
     * Ask the mManageService to remove a Measure from the Song.
     */
    public void deleteAction() {
        var alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm delete");
        alert.setHeaderText("The selected measure will be removed from the song. It will still be available for later" +
                "reuse.");
        alert.setContentText("Is this okay?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            SongMeasure toDelete = measureList.getSelectionModel().getSelectedItem();
            if (toDelete != null) {
                measureManageServiceInterface.deleteMeasure(toDelete);
                initializeMeasureList(song, measureList);
            }
        }
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

        initializeMeasureList(song, measureList);
    }

    /**
     * Initialise a list of SongMeasure models fetched through the repository.
     *
     * @param song        The Song for which to retrieve all SongMeasures.
     * @param measureList the list to which the retrieved models should be added.
     */
    private void initializeMeasureList(Song song, JFXListView<SongMeasure> measureList) {
        measureList.getItems().setAll(
                FXCollections.observableArrayList(
                        measureManageServiceInterface.getSongMeasureCollection(song)
                )
        );
    }
}
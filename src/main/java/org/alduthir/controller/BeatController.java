package org.alduthir.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
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

/**
 * Class BeatController
 *
 * Controls GUI elements pertaining to managing beats in a Measure.
 */
public class BeatController extends App implements InstrumentActionListener {
    private final InstrumentManageService instrumentManageService;

    public JFXButton backButton;
    public JFXButton addButton;
    public JFXButton playButton;
    public JFXListView<Instrument> beatList;

    private MidiPlayer midiPlayer;
    private Song song;
    private Measure measure;

    /**
     * Create necessary service layer dependencies on construction.
     */
    public BeatController() {
        this.instrumentManageService = new InstrumentManageService();
        this.midiPlayer = new MidiPlayer();
    }

    /**
     * Setup the collection of instruments and couple the CellFactory for individual instruments.
     * @param song Required for redirecting back to MeasureController with the correct list of measures.
     * @param measure Used to determine which Measure to retrieve Instruments for and save them to.
     */
    public void initialize(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;

        beatList.setCellFactory(new InstrumentCellFactory(this));
        instrumentManageService.initializeInstrumentCollection(measure, beatList);
    }

    /**
     * Redirect back to the measure selection steam for the given Song.
     * @param mouseEvent used to retrieve the FX element calling the function and through there retrieve the Stage.
     * @throws IOException if no resource can be loaded from gui/measureScreen.fxml.
     */
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

    /**
     * Open a dialog for Adding a new Instrument to the measure, or reusing a different once from another Measure.
     * @throws IOException if no resource can be loaded from gui/addInstrumentDialog.fxml.
     */
    public void addAction() throws IOException {
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

    /**
     * Preview the current Measure.
     */
    public void playAction() {
        try {
            midiPlayer.playMeasure(song.getBpm(), measure);
        } catch (InvalidMidiDataException | MidiUnavailableException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * An implementation of the InstrumentActionListener, used to remove an Instrument from the Measure.
     * @param instrument the Instrument to be removed.
     */
    @Override
    public void removeAction(Instrument instrument) {
        instrumentManageService.removeInstrument(measure, instrument, beatList);
    }

    /**
     * An implementation of the InstrumentActionListener, used to update the checkboxes forming the beat of a single
     * Instrument within the Measure.
     * @param beatNotes An encoded string consisting of 0's and 1's determining whether the instrument should play on
     *                  a 16th note.
     * @param instrument The Instrument for which the beat should be saved within the current Measure.
     */
    @Override
    public void updateAction(String beatNotes, Instrument instrument) {
        instrumentManageService.UpdateBeat(measure, instrument, beatNotes);
    }
}
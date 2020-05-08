package org.alduthir.service;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.scene.control.Spinner;
import javafx.scene.layout.VBox;
import org.alduthir.controller.AddInstrumentOption;
import org.alduthir.model.Instrument;
import org.alduthir.model.Measure;

public interface InstrumentManageServiceInterface {
    void initializeInstrumentCollection(Measure measure, JFXListView<Instrument> beatList);

    void initializeReuseOptionCollection(Measure measure, JFXComboBox<Instrument> reuseComboBox);

    void removeInstrument(Measure measure, Instrument instrument, JFXListView<Instrument> beatList);

    void saveNewInstrument(String name, int midiNumber, Measure measure);

    void reuseInstrument(Instrument instrument, Measure measure);

    void initializeInstrumentSpinner(Spinner<Integer> insturmentSpinner);

    void initiallizeNewOrReuseComboBox(
            JFXComboBox<AddInstrumentOption> newOrReuseSelection,
            VBox reuseBox,
            VBox newBox
    );

    void updateBeat(Measure measure, Instrument instrument, String beatNotes);

    void playNote(int midiKey);

    void playMeasure(Measure measure, int bpm);
}

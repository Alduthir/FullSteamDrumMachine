package org.alduthir.instrument;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import org.alduthir.measure.Measure;
import org.alduthir.song.Song;
import org.alduthir.util.NoSelectionModel;

import java.io.File;
import java.sql.SQLException;

public class InstrumentManageService {
    private InstrumentRepository repository;

    public InstrumentManageService() {
        try {
            repository = new InstrumentRepository();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initializeInstrumentCollection(Measure measure, JFXListView<Instrument> beatList) {
        try {
            beatList.getItems().setAll(repository.fetchForMeasure(measure));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        beatList.setSelectionModel(new NoSelectionModel<>());
    }

    public void initializeReuseOptionCollection(Measure measure, JFXComboBox<Instrument> reuseComboBox) {
        try {
            reuseComboBox.getItems().setAll(repository.fetchReuseOptionCollection(measure));
            reuseComboBox.getSelectionModel().selectFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void removeInstrument(Measure measure, Instrument instrument, JFXListView<Instrument> beatList) {
        try {
            repository.removeFromMeasure(measure, instrument);
            initializeInstrumentCollection(measure, beatList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveNewInstrument(String text, int midiNumber, Measure measure) {
        Instrument instrument = new Instrument(text, midiNumber);

        try {
            repository.createInstrument(instrument);
            repository.addToMeasure(instrument, measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reuseInstrument(Instrument selectedInstrument, Measure measure) {
        try {
            repository.addToMeasure(selectedInstrument, measure);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void initializeInstrumentSpinner(Spinner<Integer> insturmentSpinner) {
        insturmentSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(27, 87));
        insturmentSpinner.setOnScroll(e -> {
            double delta = e.getDeltaY();
            if (delta < 0) {
                insturmentSpinner.decrement();
            } else if (delta > 0) {
                insturmentSpinner.increment();
            }
        });
    }

    public void updateNotes(Measure measure, Instrument instrument, String beatNotes) {
        try {
            repository.updateBeat(measure, instrument, beatNotes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

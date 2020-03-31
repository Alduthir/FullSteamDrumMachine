package org.alduthir.instrument;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import org.alduthir.measure.Measure;
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

    public void saveNewInstrument(String text, File file, Measure measure) {
        String absolutePath = file.getAbsolutePath();
        Instrument instrument = new Instrument(text, absolutePath);

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
}

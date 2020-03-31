package org.alduthir.instrument;

import com.jfoenix.controls.JFXListView;
import org.alduthir.measure.Measure;
import org.alduthir.util.NoSelectionModel;

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

    public void initializeInstrumentCollection(Measure measure, JFXListView<Instrument> beatList) throws SQLException {
        beatList.getItems().setAll(repository.fetchForMeasure(measure));
        beatList.setSelectionModel(new NoSelectionModel<>());
    }

    public void removeInstrument(Measure measure, Instrument instrument, JFXListView<Instrument> beatList) {
        try {
            repository.removeFromMeasure(measure, instrument);
            initializeInstrumentCollection(measure, beatList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

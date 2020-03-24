package org.alduthir.instrument;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.measure.Measure;

public class InstrumentCellFactory implements Callback<ListView<Instrument>, ListCell<Instrument>>
{
    private Measure measure;
    public InstrumentCellFactory(Measure measure)
    {
        this.measure = measure;
    }
    @Override
    public ListCell<Instrument> call(ListView<Instrument> beatListView) {
        return new InstrumentCell(measure);
    }
}
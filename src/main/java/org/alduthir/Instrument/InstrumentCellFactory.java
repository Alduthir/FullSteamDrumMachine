package org.alduthir.Instrument;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class InstrumentCellFactory implements Callback<ListView<Instrument>, ListCell<Instrument>>
{
    @Override
    public ListCell<Instrument> call(ListView<Instrument> beatListView) {
        return new InstrumentCell();
    }
}
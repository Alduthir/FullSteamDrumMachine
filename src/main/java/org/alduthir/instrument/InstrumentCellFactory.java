package org.alduthir.instrument;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.controller.BeatController;

public class InstrumentCellFactory implements Callback<ListView<Instrument>, ListCell<Instrument>> {
    private BeatController beatController;

    public InstrumentCellFactory(BeatController beatController) {
        this.beatController = beatController;
    }

    @Override
    public ListCell<Instrument> call(ListView<Instrument> beatListView) {
        InstrumentCell cell = new InstrumentCell();
        cell.addListener(beatController);
        return cell;
    }
}
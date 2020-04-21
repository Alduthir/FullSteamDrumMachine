package org.alduthir.instrument;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.controller.BeatController;

public class InstrumentCellFactory implements Callback<ListView<Instrument>, ListCell<Instrument>> {
    private BeatController beatController;

    /**
     * Create the InstrumentCellFactory.
     * @param beatController passed to be used as a listener for actionEvents within the InstrumentCell.
     */
    public InstrumentCellFactory(BeatController beatController) {
        this.beatController = beatController;
    }

    /**
     * Create an InstrumentCell and pass the beatController as a listener.
     * @param beatListView must be passed for the CallBack<ListView<Instrument>>.
     * @return the created ListCell.
     */
    @Override
    public ListCell<Instrument> call(ListView<Instrument> beatListView) {
        InstrumentCell cell = new InstrumentCell();
        cell.addListener(beatController);
        return cell;
    }
}
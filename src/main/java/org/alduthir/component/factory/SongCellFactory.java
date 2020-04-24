package org.alduthir.component.factory;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;
import org.alduthir.component.SongCell;
import org.alduthir.model.Song;

public class SongCellFactory implements Callback<ListView<Song>, ListCell<Song>>
{
    /**
     * @inheritDoc
     */
    @Override
    public ListCell<Song> call(ListView<Song> songListView) {
        return new SongCell();
    }
}
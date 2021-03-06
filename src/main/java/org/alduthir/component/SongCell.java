package org.alduthir.component;

import javafx.scene.control.ListCell;
import org.alduthir.model.Song;

/**
 * Class SongCell
 *
 * A viewcomponent used to display a Song in a ListView. Each individual cell must display it's songs name.
 */
public class SongCell extends ListCell<Song> {

    /**
     * @inheritDoc
     */
    @Override
    public void updateItem(Song song, boolean empty) {
        super.updateItem(song, empty);

        String name = null;

        // Format name
        if (song != null) {
            name = song.toString();
        }
        
        this.setText(name);
        setGraphic(null);
    }
}

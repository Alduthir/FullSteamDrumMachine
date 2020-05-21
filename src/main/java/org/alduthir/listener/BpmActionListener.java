package org.alduthir.listener;

import org.alduthir.model.Song;

/**
 * Interface BpmActionListener
 * <p>
 * An interface to define functions Listeners must implement to handle Events raised in the BpmSpinner
 */
public interface BpmActionListener {

    /**
     * Update the BPM of the passed song.
     * @param song the song for which the BPM should be updated
     */
    void updateAction(Song song, int bpmValue);
}

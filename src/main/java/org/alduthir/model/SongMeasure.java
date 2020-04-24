package org.alduthir.model;

/**
 * Class SongMeasure
 * <p>
 * A model for a SongMeasure database object. Serves as a link between Song and Measure entities.
 */
public class SongMeasure {
    int songMeasureId;
    Song song;
    Measure measure;

    public SongMeasure(int songMeasureId, Song song, Measure measure) {
        this.songMeasureId = songMeasureId;
        this.song = song;
        this.measure = measure;
    }

    public int getSongMeasureId() {
        return songMeasureId;
    }

    public Song getSong() {
        return song;
    }

    public Measure getMeasure() {
        return measure;
    }
}

package org.alduthir.measure;

import org.alduthir.song.Song;

public class SongMeasure {
    int songMeasureId;
    Song song;
    Measure measure;

    public SongMeasure(Song song, Measure measure) {
        this.song = song;
        this.measure = measure;
    }

    public SongMeasure(int songMeasureId, Song song, Measure measure) {
        this.songMeasureId = songMeasureId;
        this.song = song;
        this.measure = measure;
    }

    public int getSongMeasureId() {
        return songMeasureId;
    }

    public void setSongMeasureId(int songMeasureId) {
        this.songMeasureId = songMeasureId;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }
}

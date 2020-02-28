package org.alduthir.Song;

public class Song {

    private int songId;
    private String name;
    private int bpm;

    public Song(int songId, String name, int bpm) {
        this.songId = songId;
        this.name = name;
        this.bpm = bpm;
    }

    public Song(int songId, String name) {
        this.songId = songId;
        this.name = name;
        this.bpm = 75;
    }

    @Override
    public String toString() {
        return name;
    }
}

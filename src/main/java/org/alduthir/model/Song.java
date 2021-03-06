package org.alduthir.model;

/**
 * Class Song
 * <p>
 * A model for a Song database object
 */
public class Song {

    private int id;
    private String name;
    private int bpm;

    public Song(int id, String name, int bpm) {
        this.id = id;
        this.name = name;
        this.bpm = bpm;
    }

    public Song(String name) {
        this.name = name;
        this.bpm = 75;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBpm() {
        return bpm;
    }

    @Override
    public String toString() {
        return name;
    }
}

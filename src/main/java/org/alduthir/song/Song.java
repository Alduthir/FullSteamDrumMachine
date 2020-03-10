package org.alduthir.song;

public class Song {

    private int id;
    private String name;
    private int bpm;

    public Song() {
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBpm() {
        return bpm;
    }

    public void setBpm(int bpm) {
        this.bpm = bpm;
    }

    @Override
    public String toString() {
        return name;
    }
}

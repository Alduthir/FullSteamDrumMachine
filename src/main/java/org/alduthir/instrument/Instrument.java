package org.alduthir.instrument;

public class Instrument {

    private int id;

    private String name;

    private String midiPath;

    public Instrument() {
    }

    public Instrument(String name, String midiPath) {
        this.name = name;
        this.midiPath = midiPath;
    }

    public Instrument(int id, String name, String midiPath) {
        this.id = id;
        this.name = name;
        this.midiPath = midiPath;
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

    public String getMidiPath() {
        return midiPath;
    }

    public void setMidiPath(String midiPath) {
        this.midiPath = midiPath;
    }

    @Override
    public String toString() {
        return name;
    }
}

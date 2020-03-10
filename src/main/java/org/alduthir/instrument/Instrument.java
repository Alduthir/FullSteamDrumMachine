package org.alduthir.instrument;

public class Instrument {

    private int id;

    private String name;

    private String midiPath;

    public Instrument() {
    }

    public Instrument(String name) {
        this.name = name;
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

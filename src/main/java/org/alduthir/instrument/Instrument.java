package org.alduthir.instrument;

/**
 * Class Instrument
 *
 * A model for the Instrument object.
 */
public class Instrument {

    private int id;

    private String name;

    private int midiNumber;

    private String beat;

    public Instrument() {
    }

    public Instrument(String name, int midiNumber) {
        this.name = name;
        this.midiNumber = midiNumber;
    }

    public Instrument(int id, String name, int midiNumber) {
        this.id = id;
        this.name = name;
        this.midiNumber = midiNumber;
    }

    public Instrument(int id, String name, int midiNumber, String beat) {
        this.id = id;
        this.name = name;
        this.midiNumber = midiNumber;
        this.beat = beat;
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

    public int getMidiNumber() {
        return midiNumber;
    }

    public void setMidiNumber(int midiNumber) {
        this.midiNumber = midiNumber;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getBeat() {
        return beat;
    }

    public void setBeat(String beat) {
        this.beat = beat;
    }
}

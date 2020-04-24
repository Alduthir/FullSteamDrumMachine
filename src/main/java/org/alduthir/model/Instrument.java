package org.alduthir.model;

/**
 * Class Instrument
 *
 * A model for the Instrument database object.
 */
public class Instrument {

    private int id;

    private String name;

    private int midiNumber;

    private String beat = "0000000000000000";

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

    public String getName() {
        return name;
    }

    public int getMidiNumber() {
        return midiNumber;
    }

    public String getBeat() {
        return beat;
    }

    @Override
    public String toString() {
        return name;
    }

}

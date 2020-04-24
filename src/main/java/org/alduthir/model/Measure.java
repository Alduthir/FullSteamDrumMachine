package org.alduthir.model;

/**
 * Class Measure
 *
 * A model for a Measure database object.
 */
public class Measure {

    private int id;
    private int beatUnit = 4;
    private int beatsInMeasure = 4;
    private String name;

    public Measure(int id, String name, int beatUnit, int beatsInMeasure){
        this.id = id;
        this.name = name;
        this.beatUnit = beatUnit;
        this.beatsInMeasure = beatsInMeasure;
    }

    public Measure(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getBeatUnit() {
        return beatUnit;
    }

    public int getBeatsInMeasure() {
        return beatsInMeasure;
    }

    @Override
    public String toString() {
        return name;
    }
}

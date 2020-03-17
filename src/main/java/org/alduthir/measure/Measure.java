package org.alduthir.measure;

public class Measure {

    private int id;
    private int beatUnit = 4;
    private int beatsInMeasure = 4;
    private String name;

    public Measure() {
    }

    public Measure(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBeatUnit() {
        return beatUnit;
    }

    public void setBeatUnit(int beatUnit) {
        this.beatUnit = beatUnit;
    }

    public int getBeatsInMeasure() {
        return beatsInMeasure;
    }

    public void setBeatsInMeasure(int beatsInMeasure) {
        this.beatsInMeasure = beatsInMeasure;
    }

    @Override
    public String toString() {
        return name;
    }
}

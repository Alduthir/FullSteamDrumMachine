package org.alduthir.Measure;

public class Measure {

    private int measureId;
    private String name;
    private int sequence;

    public Measure(int measureId, String name, int sequence) {
        this.measureId = measureId;
        this.name = name;
        this.sequence = sequence;
    }

    public Measure(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

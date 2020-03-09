package org.alduthir.Measure;

public class Measure {

    private int id;
    private String name;
    private int sequence;

    public Measure() {
    }

    public Measure(int id, String name, int sequence) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
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

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    @Override
    public String toString() {
        return name;
    }
}

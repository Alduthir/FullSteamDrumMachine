package org.alduthir.Beat;

public class Beat {
    private String name;

    public Beat(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

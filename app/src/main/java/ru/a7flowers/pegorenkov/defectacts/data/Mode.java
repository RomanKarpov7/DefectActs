package ru.a7flowers.pegorenkov.defectacts.data;

public class Mode {
    public static final Mode DEFECTS = new Mode(0);
    public static final Mode DIFFERENCIES = new Mode(1);

    private final int mode;

    public Mode(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}

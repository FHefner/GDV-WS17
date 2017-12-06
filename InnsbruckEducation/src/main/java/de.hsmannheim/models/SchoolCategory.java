package de.hsmannheim.models;

public enum SchoolCategory {
    HIGHER_EDUCATION,
    PRIMARY,
    SPECIAL;

    @Override
    public String toString() {
        String output = "";
        switch (this.name()) {
            case "HIGHER_EDUCATION": {
                output = "Höhere Bildungseinrichtung";
                break;
            }
            case "PRIMARY": {
                output = "Grundschule";
                break;
            }
            case "SPECIAL": {
                output = "Sonderschule";
                break;
            }
        }
        return output;
    }
}

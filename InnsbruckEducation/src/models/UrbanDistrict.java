package models;

import processing.core.PApplet;
import processing.data.Table;
import processing.data.TableRow;

public class UrbanDistrict {

    private final static String NAME_MAPPING_CSV_PATH = "data/zaehlersprengel_filtered.csv";
    public final static String CSV_DATA_PATH = "data/bevoelkerung_2017.csv";
    private PApplet applet;
    private int zaehlerSprengel = -1;
    private String name = "";
    private int amountHabitants6To9;
    private int amountHabitants10To14;
    private int amountHabitants15To19;
    private int amountHabitants20To24;
    private int amountHabitants25To29;
    private int totalAmountHabitants;

    public int getTotalAmountHabitants() {
        return totalAmountHabitants;
    }

    public void setTotalAmountHabitants(int totalAmountHabitants) {
        this.totalAmountHabitants = totalAmountHabitants;
    }

    private void calculateTotalHabitants() {
        this.totalAmountHabitants = this.amountHabitants6To9 + this.amountHabitants10To14
                + this.amountHabitants15To19 + this.amountHabitants20To24 + this.amountHabitants25To29;
    }

    private void getDistrictNameFromZaehlerSprengel() {
        Table mappingTable = applet.loadTable(NAME_MAPPING_CSV_PATH, "header");
        for (TableRow row : mappingTable.rows()) {
            System.out.println("ZLSPR: ." + row.getInt("ZLSPR") + "." + this.zaehlerSprengel);
            if (row.getInt("ZLSPR") == this.zaehlerSprengel) {
                this.name = row.getString("Name");
                return;
            }
        }
    }

    public int getZaehlerSprengel() {
        return zaehlerSprengel;
    }

    public void setZaehlerSprengel(int zaehlerSprengel) {
        this.zaehlerSprengel = zaehlerSprengel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountHabitants6To9() {
        return amountHabitants6To9;
    }

    public void setAmountHabitants6To9(int amountHabitants6To9) {
        this.amountHabitants6To9 = amountHabitants6To9;
    }

    public int getAmountHabitants10To14() {
        return amountHabitants10To14;
    }

    public void setAmountHabitants10To14(int amountHabitants10To14) {
        this.amountHabitants10To14 = amountHabitants10To14;
    }

    public int getAmountHabitants15To19() {
        return amountHabitants15To19;
    }

    public void setAmountHabitants15To19(int amountHabitants15To19) {
        this.amountHabitants15To19 = amountHabitants15To19;
    }

    public int getAmountHabitants20To24() {
        return amountHabitants20To24;
    }

    public void setAmountHabitants20To24(int amountHabitants20To24) {
        this.amountHabitants20To24 = amountHabitants20To24;
    }

    public int getAmountHabitants25To29() {
        return amountHabitants25To29;
    }

    public void setAmountHabitants25To29(int amountHabitants25To29) {
        this.amountHabitants25To29 = amountHabitants25To29;
    }

    public UrbanDistrict(PApplet applet, int zaehlerSprengel, int amountHabitants6To9, int amountHabitants10To14, int amountHabitants15To19, int amountHabitants20To24, int amountHabitants25To29) {
        this.applet = applet;
        this.zaehlerSprengel = zaehlerSprengel;
        this.amountHabitants6To9 = amountHabitants6To9;
        this.amountHabitants10To14 = amountHabitants10To14;
        this.amountHabitants15To19 = amountHabitants15To19;
        this.amountHabitants20To24 = amountHabitants20To24;
        this.amountHabitants25To29 = amountHabitants25To29;
        getDistrictNameFromZaehlerSprengel();
        calculateTotalHabitants();
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(this.zaehlerSprengel);
        output.append(" ").append(this.name).append("\n");
        output.append("Einwohner 6-9: ").append(String.valueOf(this.amountHabitants6To9)).append("\n");
        output.append("Einwohner 10-14: ").append(String.valueOf(this.amountHabitants10To14)).append("\n");
        output.append("Einwohner 15-19: ").append(String.valueOf(this.amountHabitants15To19)).append("\n");
        output.append("Einwohner 20-24: ").append(String.valueOf(this.amountHabitants20To24)).append("\n");
        output.append("Einwohner 25-29: ").append(String.valueOf(this.amountHabitants25To29)).append("\n");
        output.append("Gesamteinwohner: ").append(String.valueOf(this.totalAmountHabitants)).append("\n");
        return output.toString();
    }
}

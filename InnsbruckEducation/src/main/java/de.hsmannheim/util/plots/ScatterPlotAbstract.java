package de.hsmannheim.util.plots;

import de.hsmannheim.models.UrbanDistrict;
import processing.core.PVector;

import java.util.*;

public abstract class ScatterPlotAbstract {
    private String educationType;
    private String ageGroup;
    protected List<PVector> colorDataList;
    protected HashMap<String, Float> inhabitantsForColorWithName= new HashMap<>();
    protected List<Float> inhabitantsForColor= new ArrayList<>();

    public float[] getInhabitantsForColor() {
        return createPrimitiveFloatArray();
    }

    private float[] createPrimitiveFloatArray(){
        float[] colorData=new float[inhabitantsForColor.size()];
        int i=0;
        for (Float f : inhabitantsForColor)
            colorData[i++] = f;
        return colorData;
    }

    public String getAgeBand(){
        return ageGroup;
    }

    public String getEducationType(){
        return educationType;
    }

    public ScatterPlotAbstract(String educationType, String ageGroup){
        this.educationType=educationType;
        this.ageGroup=ageGroup;
    }

    public List<PVector> createDataset(List<UrbanDistrict> allDistrictsList, int year) {
        colorDataList = new ArrayList<>();
        for(UrbanDistrict district: allDistrictsList){
          addDistrictToPlot(district, year);
        }
        return colorDataList;
    }

    public float[] createDataSetWithHighlightedDistrict(UrbanDistrict district){
        float colorOfHighlightedDistrict= inhabitantsForColorWithName.get(district.getName());
        float[] colorData=new float[inhabitantsForColor.size()];
        int i=0;
        for (Float f : inhabitantsForColor){
            if(f!=colorOfHighlightedDistrict){
                colorData[i] = f;
            }else {
                //to make the value sorted by the ColourTable to the special index -1 for marked points in ScatterPlotUtil
                colorData[i]= f-99;
            }
            i++;
        }

        return colorData;
        }

   protected abstract void addToColorDataList(int totalAmountInhabitants, UrbanDistrict district);

    protected abstract void addDistrictToPlot(UrbanDistrict district, Integer year);

}

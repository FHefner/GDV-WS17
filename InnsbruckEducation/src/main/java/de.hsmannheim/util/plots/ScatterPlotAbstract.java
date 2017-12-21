package de.hsmannheim.util.plots;

import de.hsmannheim.models.UrbanDistrict;
import processing.core.PVector;

import java.util.*;

public abstract class ScatterPlotAbstract {
    private String educationType;
    private String ageGroup;
    protected List<PVector> colorDataList;
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
   //     this.educationalInstitutions=educationalInstitutions;
    }

    public List<PVector> createDataset(List<UrbanDistrict> allDistrictsList) {
        colorDataList = new ArrayList<>();
        for(UrbanDistrict district: allDistrictsList){
          addDistrictToPlot(district);
        }
        return colorDataList;
    }

   //To calculate color divide the district with the most Inhabitants by 20 this result is taken
    //to divide ALL totalAmountInhabitants of the different districts to get a color rating of 10 different colors.
    //Example: District with the most Inhabitants (University) 1000 --> 1000/20= 50 --> district.amountTotalInhabitants/50
    protected abstract void addToColorDataList(int totalAmountInhabitants);

    protected abstract void addDistrictToPlot(UrbanDistrict district);

}

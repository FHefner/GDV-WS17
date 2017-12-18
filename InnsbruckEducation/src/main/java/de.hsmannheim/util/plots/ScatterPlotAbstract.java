package de.hsmannheim.util.plots;

import de.hsmannheim.models.UrbanDistrict;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;

public abstract class ScatterPlotAbstract {
    private String educationType;
    private String ageGroup;
    protected XYSeries series;

    public String getAgeBand() {
        return ageGroup;
    }

    public String getEducationType(){
        return educationType;

    }

    public ScatterPlotAbstract(String educationType, String ageGroup){
        this.educationType=educationType;
        this.ageGroup=ageGroup;
        this.series= new XYSeries("Stadtteile");
    }

    public XYSeriesCollection createDataset(List<UrbanDistrict> allDistrictsList) {
        double educationalInstitutions=0;
        XYSeriesCollection result = new XYSeriesCollection();
        this.series = new XYSeries("Stadtteile");
        for(UrbanDistrict district: allDistrictsList){
          addDistrictToPlot(district);
        }
        result.addSeries(series);
        return result;
    }

    public abstract void addDistrictToPlot(UrbanDistrict district);

}

package de.hsmannheim.util.plots;

import de.hsmannheim.models.UrbanDistrict;
import de.hsmannheim.util.district.DistrictUtil;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.util.List;

public abstract class ScatterPlotAbstract {
    private String educationType;
    private String ageGroup;
    protected XYSeries series;

    public ScatterPlotAbstract(String educationType, String ageGroup){
        this.educationType=educationType;
        this.ageGroup=ageGroup;
        this.series= new XYSeries("Stadteile");
    }

    public XYSeriesCollection createDataset(List<UrbanDistrict> allDistrictsList) {
        double educationalInstitutions=0;
        XYSeriesCollection result = new XYSeriesCollection();
        this.series = new XYSeries("Stadteile");
        for(UrbanDistrict district: allDistrictsList){
          addDistrcitToPlot(district);
        }
        result.addSeries(series);
        return result;
    }

    public abstract void addDistrcitToPlot(UrbanDistrict district);

    public String getAgeBand() {
        return ageGroup;
    }

    public String getEducationType(){
        return educationType;

    }


}

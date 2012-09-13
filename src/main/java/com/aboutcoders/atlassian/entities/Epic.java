package com.aboutcoders.atlassian.entities;

import com.atlassian.jira.charts.Chart;
import com.aboutcoders.atlassian.helpers.ChartGenerator;

import java.util.Map;

public class Epic
{
    protected String _summary;
    protected String _key;
    protected double _totalStoryPoints;
    protected double _burnedStoryPoints;
    protected Map<String, Object> chartParams = null;

    public String getSummary() {
        return _summary;
    }

    public void setSummary(String _summary) {
        this._summary = _summary;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String _key) {
        this._key = _key;
    }

    public double getTotalStoryPoints() {
        return _totalStoryPoints;
    }

    public void setTotalStoryPoints(Double _totalStoryPoints) {
        this._totalStoryPoints = _totalStoryPoints;
    }

    public double getBurnedStoryPoints() {
        return _burnedStoryPoints;
    }

    public void setBurnedStoryPoints(Double _burnedStoryPoints) {
        this._burnedStoryPoints = _burnedStoryPoints;
    }

    public int getPercentComplete() {
        return (int) (( this._burnedStoryPoints / this._totalStoryPoints ) * 100);
    }

    public void generateChart( double totalSp, double burnedSp )
    {
        final Chart chart = ChartGenerator.getChart( totalSp, burnedSp );
        this.chartParams = chart.getParameters();
    }

    public String getImageMapName()
    {
        String im = null;
        if ( this.chartParams != null && this.chartParams.get("imagemapName") != null )
        {
            im = (String ) this.chartParams.get("imagemapName");
        }
        return im;
    }

    public String getImageMap()
    {
        String im = null;
        if ( this.chartParams != null && this.chartParams.get("imagemap") != null )
        {
            im = (String ) this.chartParams.get("imagemap");
        }
        return im;
    }

    public String getChartName()
    {
        String im = null;
        if ( this.chartParams != null && this.chartParams.get("chart") != null )
        {
            im = (String ) this.chartParams.get("chart");
        }
        return im;
    }
}

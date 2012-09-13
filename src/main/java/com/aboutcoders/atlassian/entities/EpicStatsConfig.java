package com.aboutcoders.atlassian.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public final class EpicStatsConfig
{
    @XmlElement
    private String epicIssueType;

    @XmlElement
    private String storyIssueType;

    @XmlElement
    private String storyPointsField;

    @XmlElement
    private String epicField;

    @XmlElement
    private String doneStatus;

    @XmlElement
    private String filterName1;

    @XmlElement
    private String filterJql1;

    @XmlElement
    private String filterName2;

    @XmlElement
    private String filterJql2;

    @XmlElement
    private String filterName3;

    @XmlElement
    private String filterJql3;

    @XmlElement
    private String filterName4;

    @XmlElement
    private String filterJql4;

    @XmlElement
    private String filterName5;

    @XmlElement
    private String filterJql5;

    public String getEpicIssueType() {
        return epicIssueType;
    }

    public void setEpicIssueType(String epicIssueType) {
        this.epicIssueType = epicIssueType;
    }

    public String getStoryIssueType() {
        return storyIssueType;
    }

    public void setStoryIssueType(String storyIssueType) {
        this.storyIssueType = storyIssueType;
    }

    public String getStoryPointsField() {
        return storyPointsField;
    }

    public void setStoryPointsField(String storyPointsField) {
        this.storyPointsField = storyPointsField;
    }

    public String getEpicField() {
        return epicField;
    }

    public void setEpicField(String epicField) {
        this.epicField = epicField;
    }

    public String getDoneStatus() {
        return doneStatus;
    }

    public void setDoneStatus(String doneStatus) {
        this.doneStatus = doneStatus;
    }

    public String getFilterName1() {
        return filterName1;
    }

    public void setFilterName1(String filterName1) {
        this.filterName1 = filterName1;
    }

    public String getFilterJql1() {
        return filterJql1;
    }

    public void setFilterJql1(String filterJql1) {
        this.filterJql1 = filterJql1;
    }

    public String getFilterName2() {
        return filterName2;
    }

    public void setFilterName2(String filterName2) {
        this.filterName2 = filterName2;
    }

    public String getFilterJql2() {
        return filterJql2;
    }

    public void setFilterJql2(String filterJql2) {
        this.filterJql2 = filterJql2;
    }

    public String getFilterName3() {
        return filterName3;
    }

    public void setFilterName3(String filterName3) {
        this.filterName3 = filterName3;
    }

    public String getFilterJql3() {
        return filterJql3;
    }

    public void setFilterJql3(String filterJql3) {
        this.filterJql3 = filterJql3;
    }

    public String getFilterName4() {
        return filterName4;
    }

    public void setFilterName4(String filterName4) {
        this.filterName4 = filterName4;
    }

    public String getFilterJql4() {
        return filterJql4;
    }

    public void setFilterJql4(String filterJql4) {
        this.filterJql4 = filterJql4;
    }

    public String getFilterName5() {
        return filterName5;
    }

    public void setFilterName5(String filterName5) {
        this.filterName5 = filterName5;
    }

    public String getFilterJql5() {
        return filterJql5;
    }

    public void setFilterJql5(String filterJql5) {
        this.filterJql5 = filterJql5;
    }
}

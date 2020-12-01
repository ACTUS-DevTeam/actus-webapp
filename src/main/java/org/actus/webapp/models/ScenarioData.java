package org.actus.webapp.models;

import java.util.*;

public class ScenarioData {

    private String scenarioId;
    private List<ObservedData> timeSeriesData;
    private List<TermStructureData> termStructureData;
    private List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData;

    public ScenarioData() {
    }

    public ScenarioData(String scenarioId, List<ObservedData> timeSeriesData, List<TermStructureData> termStructureData, List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData) {
        this.scenarioId = scenarioId;
        this.timeSeriesData = timeSeriesData;
        this.termStructureData = termStructureData;
        this.twoDimensionalPrepaymentModelData = twoDimensionalPrepaymentModelData;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public List<ObservedData> getTimeSeriesData() {
        return timeSeriesData;
    }

    public void setTimeSeriesData(List<ObservedData> timeSeriesData) {
        this.timeSeriesData = timeSeriesData;
    }

    public List<TermStructureData> getTermStructureData() {
        return termStructureData;
    }

    public void setTermStructureData(List<TermStructureData> termStructureData) {
        this.termStructureData = termStructureData;
    }

    public List<TwoDimensionalPrepaymentModelData> getTwoDimensionalPrepaymentModelData() {
        return twoDimensionalPrepaymentModelData;
    }

    public void setTwoDimensionalPrepaymentModelData(List<TwoDimensionalPrepaymentModelData> twoDimensionalPrepaymentModelData) {
        this.twoDimensionalPrepaymentModelData = twoDimensionalPrepaymentModelData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioData{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append(", timeSeriesData='").append(timeSeriesData).append('\'');
        sb.append(", termStructureData='").append(termStructureData).append('\'');
        sb.append(", twoDimensionalPrepaymentModelData='").append(twoDimensionalPrepaymentModelData).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
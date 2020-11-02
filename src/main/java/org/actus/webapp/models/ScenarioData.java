package org.actus.webapp.models;

import java.util.*;

public class ScenarioData {

    private String scenarioId;
    private List<ObservedData> data;

    public ScenarioData() {
    }

    public ScenarioData(String scenarioId, List<ObservedData> data) {
        this.scenarioId = scenarioId;
        this.data = data;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public List<ObservedData> getData() {
        return data;
    }

    public void setData(List<ObservedData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioData{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
package org.actus.webapp.models;

import java.util.*;

public class ScenarioSimulationInput {

    private String scenarioId;
    private List<Map<String,Object>> contracts;

    public ScenarioSimulationInput() {
    }

    public ScenarioSimulationInput(String scenarioId, List<Map<String,Object>> contracts) {
        this.scenarioId = scenarioId;
        this.contracts = contracts;
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public List<Map<String,Object>> getContracts() {
        return contracts;
    }

    public void setContracts(List<Map<String,Object>> contracts) {
        this.contracts = contracts;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScenarioSimulationInput{");
        sb.append("scenarioId='").append(scenarioId).append('\'');
        sb.append(", contracts='").append(contracts).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
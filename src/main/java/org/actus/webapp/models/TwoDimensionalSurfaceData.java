package org.actus.webapp.models;

import java.util.List;

public class TwoDimensionalSurfaceData {
    private String riskFactorId;
    private String interpolationMethod; // e.g. "linear"
    private String extrapolationMethod; // e.g. "constant"
    private List<Margin> margins;
    private List<List<Double>> data;

    public TwoDimensionalSurfaceData() {
    }

    public TwoDimensionalSurfaceData(String riskFactorId, String interpolationMethod, String extrapolationMethod, List<Margin> margins, List<List<Double>> data) {
        this.riskFactorId = riskFactorId;
        this.interpolationMethod = interpolationMethod;
        this.extrapolationMethod = extrapolationMethod;
        this.margins = margins;
        this.data = data;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public String getInterpolationMethod() {
        return this.interpolationMethod;
    }

    public void setInterpolationMethod(String interpolationMethod) {
        this.interpolationMethod = interpolationMethod;
    }

    public String getExtrapolationMethod() {
        return this.extrapolationMethod;
    }

    public void setExtrapolationMethod(String extrapolationMethod) {
        this.extrapolationMethod = extrapolationMethod;
    }

    public List<Margin> getMargins() {
        return this.margins;
    }

    public void setMargins(List<Margin> margins) {
        this.margins = margins;
    }

    public List<List<Double>> getData() {
        return this.data;
    }

    public void setData(List<List<Double>> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalSurfaveData{");
        sb.append("riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", interpolationMethod='").append(interpolationMethod).append('\'');
        sb.append(", extrapolationMethod='").append(extrapolationMethod).append('\'');
        sb.append(", margins='").append(margins).append('\'');
        sb.append(", data='").append(data).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
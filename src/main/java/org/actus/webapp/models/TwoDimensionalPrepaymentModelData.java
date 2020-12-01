package org.actus.webapp.models;

import java.util.List;

public class TwoDimensionalPrepaymentModelData {
    private String riskFactorId;
    private String referenceRateId;
    private TwoDimensionalSurfaceData surface;

    public TwoDimensionalPrepaymentModelData() {
    }

    public TwoDimensionalPrepaymentModelData(String riskFactorId, String referenceRateId, TwoDimensionalSurfaceData surface) {
        this.riskFactorId = riskFactorId;
        this.referenceRateId = referenceRateId;
        this.surface = surface;
    }

    public String getRiskFactorId() {
        return this.riskFactorId;
    }

    public void setRiskFactorId(String riskFactorId) {
        this.riskFactorId = riskFactorId;
    }

    public String getReferenceRateId() {
        return this.referenceRateId;
    }

    public void setReferenceRateId(String referenceRateId) {
        this.referenceRateId = referenceRateId;
    }

    public TwoDimensionalSurfaceData getSurface() {
        return this.surface;
    }

    public void setSurface(TwoDimensionalSurfaceData surface) {
        this.surface = surface;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TwoDimensionalPrepaymentModelData{");
        sb.append(", riskFactorId='").append(riskFactorId).append('\'');
        sb.append(", referenceRateId='").append(referenceRateId).append('\'');
        sb.append(", surface='").append(surface).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
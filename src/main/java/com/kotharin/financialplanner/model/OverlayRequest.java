package com.kotharin.financialplanner.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OverlayRequest {
    private Double callMoneynessPct;
    private Integer dteDays;
    private Integer rollFrequencyDays;
    private Double coveragePct;
    private Boolean enableTaxLossHarvest;
    private Double positionReductionPctPerQuarter;
    private Double reductionThresholdPct;
    private Double shareReductionTriggerPct;
    private BigDecimal costBasis;
    private Integer targetDteDays;
    private Double targetDelta;
    private Double profitCapturePct;
    private Double stopLossMultiple;
    private Double extrinsicThresholdPct;
    private Double totalRealizedOptionLoss;
    private Integer totalSharesSoldOnCallLoss;
    private Double totalRealizedOptionPnl;
    private List<String> auditLog;
    private String cashReturnMode;

    public OverlayRequest() {
    }

    private OverlayRequest(Builder builder) {
        this.callMoneynessPct = builder.callMoneynessPct;
        this.dteDays = builder.dteDays;
        this.rollFrequencyDays = builder.rollFrequencyDays;
        this.coveragePct = builder.coveragePct;
        this.enableTaxLossHarvest = builder.enableTaxLossHarvest;
        this.positionReductionPctPerQuarter = builder.positionReductionPctPerQuarter;
        this.reductionThresholdPct = builder.reductionThresholdPct;
        this.shareReductionTriggerPct = builder.shareReductionTriggerPct;
        this.costBasis = builder.costBasis;
        this.targetDteDays = builder.targetDteDays;
        this.targetDelta = builder.targetDelta;
        this.profitCapturePct = builder.profitCapturePct;
        this.stopLossMultiple = builder.stopLossMultiple;
        this.extrinsicThresholdPct = builder.extrinsicThresholdPct;
        this.totalRealizedOptionLoss = builder.totalRealizedOptionLoss;
        this.totalSharesSoldOnCallLoss = builder.totalSharesSoldOnCallLoss;
        this.totalRealizedOptionPnl = builder.totalRealizedOptionPnl;
        this.auditLog = builder.auditLog;
        this.cashReturnMode = builder.cashReturnMode;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters and Setters
    public Double getCallMoneynessPct() {
        return callMoneynessPct;
    }

    public void setCallMoneynessPct(Double callMoneynessPct) {
        this.callMoneynessPct = callMoneynessPct;
    }

    public Integer getDteDays() {
        return dteDays;
    }

    public void setDteDays(Integer dteDays) {
        this.dteDays = dteDays;
    }

    public Integer getRollFrequencyDays() {
        return rollFrequencyDays;
    }

    public void setRollFrequencyDays(Integer rollFrequencyDays) {
        this.rollFrequencyDays = rollFrequencyDays;
    }

    public Double getCoveragePct() {
        return coveragePct;
    }

    public void setCoveragePct(Double coveragePct) {
        this.coveragePct = coveragePct;
    }

    public Boolean getEnableTaxLossHarvest() {
        return enableTaxLossHarvest;
    }

    public void setEnableTaxLossHarvest(Boolean enableTaxLossHarvest) {
        this.enableTaxLossHarvest = enableTaxLossHarvest;
    }

    public Double getPositionReductionPctPerQuarter() {
        return positionReductionPctPerQuarter;
    }

    public void setPositionReductionPctPerQuarter(Double positionReductionPctPerQuarter) {
        this.positionReductionPctPerQuarter = positionReductionPctPerQuarter;
    }

    public Double getReductionThresholdPct() {
        return reductionThresholdPct;
    }

    public void setReductionThresholdPct(Double reductionThresholdPct) {
        this.reductionThresholdPct = reductionThresholdPct;
    }

    public Double getShareReductionTriggerPct() {
        return shareReductionTriggerPct;
    }

    public void setShareReductionTriggerPct(Double shareReductionTriggerPct) {
        this.shareReductionTriggerPct = shareReductionTriggerPct;
    }

    public BigDecimal getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(BigDecimal costBasis) {
        this.costBasis = costBasis;
    }

    public Integer getTargetDteDays() {
        return targetDteDays;
    }

    public void setTargetDteDays(Integer targetDteDays) {
        this.targetDteDays = targetDteDays;
    }

    public Double getTargetDelta() {
        return targetDelta;
    }

    public void setTargetDelta(Double targetDelta) {
        this.targetDelta = targetDelta;
    }

    public Double getProfitCapturePct() {
        return profitCapturePct;
    }

    public void setProfitCapturePct(Double profitCapturePct) {
        this.profitCapturePct = profitCapturePct;
    }

    public Double getStopLossMultiple() {
        return stopLossMultiple;
    }

    public void setStopLossMultiple(Double stopLossMultiple) {
        this.stopLossMultiple = stopLossMultiple;
    }

    public Double getExtrinsicThresholdPct() {
        return extrinsicThresholdPct;
    }

    public void setExtrinsicThresholdPct(Double extrinsicThresholdPct) {
        this.extrinsicThresholdPct = extrinsicThresholdPct;
    }

    public Double getTotalRealizedOptionLoss() {
        return totalRealizedOptionLoss;
    }

    public void setTotalRealizedOptionLoss(Double totalRealizedOptionLoss) {
        this.totalRealizedOptionLoss = totalRealizedOptionLoss;
    }

    public Integer getTotalSharesSoldOnCallLoss() {
        return totalSharesSoldOnCallLoss;
    }

    public void setTotalSharesSoldOnCallLoss(Integer totalSharesSoldOnCallLoss) {
        this.totalSharesSoldOnCallLoss = totalSharesSoldOnCallLoss;
    }

    public Double getTotalRealizedOptionPnl() {
        return totalRealizedOptionPnl;
    }

    public void setTotalRealizedOptionPnl(Double totalRealizedOptionPnl) {
        this.totalRealizedOptionPnl = totalRealizedOptionPnl;
    }

    public List<String> getAuditLog() {
        return auditLog;
    }

    public void setAuditLog(List<String> auditLog) {
        this.auditLog = auditLog;
    }

    public String getCashReturnMode() {
        return cashReturnMode;
    }

    public void setCashReturnMode(String cashReturnMode) {
        this.cashReturnMode = cashReturnMode;
    }

    public static class Builder {
        private Double callMoneynessPct = 5.0;
        private Integer dteDays = 45;
        private Integer rollFrequencyDays = 30;
        private Double coveragePct = 50.0;
        private Boolean enableTaxLossHarvest = true;
        private Double positionReductionPctPerQuarter = 0.0;
        private Double reductionThresholdPct = null;
        private Double shareReductionTriggerPct = 0.0;
        private BigDecimal costBasis = BigDecimal.ZERO;
        private Integer targetDteDays = 30;
        private Double targetDelta = 0.20;
        private Double profitCapturePct = 0.50;
        private Double stopLossMultiple = 1.00;
        private Double extrinsicThresholdPct = 0.05;
        private Double totalRealizedOptionLoss = 0.0;
        private Integer totalSharesSoldOnCallLoss = 0;
        private Double totalRealizedOptionPnl = 0.0;
        private List<String> auditLog = new ArrayList<>();
        private String cashReturnMode = "underlying";

        public Builder callMoneynessPct(Double callMoneynessPct) {
            this.callMoneynessPct = callMoneynessPct;
            return this;
        }

        public Builder dteDays(Integer dteDays) {
            this.dteDays = dteDays;
            return this;
        }

        public Builder rollFrequencyDays(Integer rollFrequencyDays) {
            this.rollFrequencyDays = rollFrequencyDays;
            return this;
        }

        public Builder coveragePct(Double coveragePct) {
            this.coveragePct = coveragePct;
            return this;
        }

        public Builder enableTaxLossHarvest(Boolean enableTaxLossHarvest) {
            this.enableTaxLossHarvest = enableTaxLossHarvest;
            return this;
        }

        public Builder positionReductionPctPerQuarter(Double positionReductionPctPerQuarter) {
            this.positionReductionPctPerQuarter = positionReductionPctPerQuarter;
            return this;
        }

        public Builder reductionThresholdPct(Double reductionThresholdPct) {
            this.reductionThresholdPct = reductionThresholdPct;
            return this;
        }

        public Builder shareReductionTriggerPct(Double shareReductionTriggerPct) {
            this.shareReductionTriggerPct = shareReductionTriggerPct;
            return this;
        }

        public Builder costBasis(BigDecimal costBasis) {
            this.costBasis = costBasis;
            return this;
        }

        public Builder targetDteDays(Integer targetDteDays) {
            this.targetDteDays = targetDteDays;
            return this;
        }

        public Builder targetDelta(Double targetDelta) {
            this.targetDelta = targetDelta;
            return this;
        }

        public Builder profitCapturePct(Double profitCapturePct) {
            this.profitCapturePct = profitCapturePct;
            return this;
        }

        public Builder stopLossMultiple(Double stopLossMultiple) {
            this.stopLossMultiple = stopLossMultiple;
            return this;
        }

        public Builder extrinsicThresholdPct(Double extrinsicThresholdPct) {
            this.extrinsicThresholdPct = extrinsicThresholdPct;
            return this;
        }

        public Builder totalRealizedOptionLoss(Double totalRealizedOptionLoss) {
            this.totalRealizedOptionLoss = totalRealizedOptionLoss;
            return this;
        }

        public Builder totalSharesSoldOnCallLoss(Integer totalSharesSoldOnCallLoss) {
            this.totalSharesSoldOnCallLoss = totalSharesSoldOnCallLoss;
            return this;
        }

        public Builder totalRealizedOptionPnl(Double totalRealizedOptionPnl) {
            this.totalRealizedOptionPnl = totalRealizedOptionPnl;
            return this;
        }

        public Builder auditLog(List<String> auditLog) {
            this.auditLog = auditLog;
            return this;
        }

        public Builder cashReturnMode(String cashReturnMode) {
            this.cashReturnMode = cashReturnMode;
            return this;
        }

        public OverlayRequest build() {
            return new OverlayRequest(this);
        }
    }
}

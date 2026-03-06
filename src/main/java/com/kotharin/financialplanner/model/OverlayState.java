package com.kotharin.financialplanner.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

public class OverlayState {
    private Double shares;
    private BigDecimal costBasis;
    private Double cash;
    private OptionPos openOption;
    private Double cumulativeTaxes;
    private Double cumulativeTlh;
    private Double realizedOptionPnl;
    private Double realizedStockGain;
    private LocalDate lastCloseDate;
    private Boolean openNextDay;

    public OverlayState() {
    }

    private OverlayState(Builder builder) {
        this.shares = builder.shares;
        this.costBasis = builder.costBasis;
        this.cash = builder.cash;
        this.openOption = builder.openOption;
        this.cumulativeTaxes = builder.cumulativeTaxes;
        this.cumulativeTlh = builder.cumulativeTlh;
        this.realizedOptionPnl = builder.realizedOptionPnl;
        this.realizedStockGain = builder.realizedStockGain;
        this.lastCloseDate = builder.lastCloseDate;
        this.openNextDay = builder.openNextDay;
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public Double getShares() {
        return shares;
    }

    public void setShares(Double shares) {
        this.shares = shares;
    }

    public BigDecimal getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(BigDecimal costBasis) {
        this.costBasis = costBasis;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public OptionPos getOpenOption() {
        return openOption;
    }

    public void setOpenOption(OptionPos openOption) {
        this.openOption = openOption;
    }

    public Double getCumulativeTaxes() {
        return cumulativeTaxes;
    }

    public void setCumulativeTaxes(Double cumulativeTaxes) {
        this.cumulativeTaxes = cumulativeTaxes;
    }

    public Double getCumulativeTlh() {
        return cumulativeTlh;
    }

    public void setCumulativeTlh(Double cumulativeTlh) {
        this.cumulativeTlh = cumulativeTlh;
    }

    public Double getRealizedOptionPnl() {
        return realizedOptionPnl;
    }

    public void setRealizedOptionPnl(Double realizedOptionPnl) {
        this.realizedOptionPnl = realizedOptionPnl;
    }

    public Double getRealizedStockGain() {
        return realizedStockGain;
    }

    public void setRealizedStockGain(Double realizedStockGain) {
        this.realizedStockGain = realizedStockGain;
    }

    public LocalDate getLastCloseDate() {
        return lastCloseDate;
    }

    public void setLastCloseDate(LocalDate lastCloseDate) {
        this.lastCloseDate = lastCloseDate;
    }

    public Boolean getOpenNextDay() {
        return openNextDay;
    }

    public void setOpenNextDay(Boolean openNextDay) {
        this.openNextDay = openNextDay;
    }

    public static class Builder {
        private Double shares;
        private BigDecimal costBasis = BigDecimal.ZERO;
        private Double cash = 0.0;
        private OptionPos openOption = null;
        private Double cumulativeTaxes = 0.0;
        private Double cumulativeTlh = 0.0;
        private Double realizedOptionPnl = 0.0;
        private Double realizedStockGain = 0.0;
        private LocalDate lastCloseDate = null;
        private Boolean openNextDay = true;

        public Builder shares(Double shares) {
            this.shares = shares;
            return this;
        }

        public Builder costBasis(BigDecimal costBasis) {
            this.costBasis = costBasis;
            return this;
        }

        public Builder cash(Double cash) {
            this.cash = cash;
            return this;
        }

        public Builder openOption(OptionPos openOption) {
            this.openOption = openOption;
            return this;
        }

        public Builder cumulativeTaxes(Double cumulativeTaxes) {
            this.cumulativeTaxes = cumulativeTaxes;
            return this;
        }

        public Builder cumulativeTlh(Double cumulativeTlh) {
            this.cumulativeTlh = cumulativeTlh;
            return this;
        }

        public Builder realizedOptionPnl(Double realizedOptionPnl) {
            this.realizedOptionPnl = realizedOptionPnl;
            return this;
        }

        public Builder realizedStockGain(Double realizedStockGain) {
            this.realizedStockGain = realizedStockGain;
            return this;
        }

        public Builder lastCloseDate(LocalDate lastCloseDate) {
            this.lastCloseDate = lastCloseDate;
            return this;
        }

        public Builder openNextDay(Boolean openNextDay) {
            this.openNextDay = openNextDay;
            return this;
        }

        public OverlayState build() {
            return new OverlayState(this);
        }
    }
}

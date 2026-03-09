package com.kotharin.financialplanner.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

public class BaselineResult {
    private Map<LocalDate, BigDecimal> historicalPrices;
    private int initialShares;
    private int finalShares;
    private Map<LocalDate, BigDecimal> stockValues;
    private BigDecimal initialValue;
    private BigDecimal finalValue;
    private BigDecimal percentReturn;
    private BigDecimal stockPnL;
    private BigDecimal optionPnL;
    private BigDecimal totalPnL;
    private BigDecimal totalTaxes;

    public BaselineResult() {
    }

    public BaselineResult(Map<LocalDate, BigDecimal> historicalPrices,
            int initialShares) {
        this.initialShares = initialShares;
        this.finalShares = initialShares;
        this.historicalPrices = historicalPrices;
        this.stockValues = historicalPrices.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        e -> e.getValue().multiply(BigDecimal.valueOf(initialShares))));
        this.initialValue = historicalPrices.values().stream().findFirst().orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(initialShares));
        this.finalValue = historicalPrices.values().stream().reduce((a, b) -> b).orElse(BigDecimal.ZERO)
                .multiply(BigDecimal.valueOf(finalShares));
        this.percentReturn = this.finalValue.divide(this.initialValue, 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100));
        this.stockPnL = this.finalValue.subtract(this.initialValue);
        this.optionPnL = BigDecimal.ZERO;
        this.totalPnL = this.stockPnL.add(this.optionPnL);
        this.totalTaxes = this.totalPnL.multiply(BigDecimal.valueOf(0.2));
    }

    public BigDecimal getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(BigDecimal initialValue) {
        this.initialValue = initialValue;
    }

    public BigDecimal getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(BigDecimal finalValue) {
        this.finalValue = finalValue;
    }

    public BigDecimal getPercentReturn() {
        return percentReturn;
    }

    public void setPercentReturn(BigDecimal percentReturn) {
        this.percentReturn = percentReturn;
    }

    public BigDecimal getStockPnL() {
        return stockPnL;
    }

    public void setStockPnL(BigDecimal stockPnL) {
        this.stockPnL = stockPnL;
    }

    public BigDecimal getOptionPnL() {
        return optionPnL;
    }

    public void setOptionPnL(BigDecimal optionPnL) {
        this.optionPnL = optionPnL;
    }

    public BigDecimal getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(BigDecimal totalPnL) {
        this.totalPnL = totalPnL;
    }

    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    public Map<LocalDate, BigDecimal> getHistoricalPrices() {
        return historicalPrices;
    }

    public void setHistoricalPrices(Map<LocalDate, BigDecimal> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }

    @Override
    public String toString() {
        return "{" +
                "\"historicalPrices\":\"" + historicalPrices + "\"" +
                ", \"initialShares\":" + initialShares +
                ", \"finalShares\":" + finalShares +
                ", \"stockValues\":\"" + stockValues + "\"" +
                ", \"initialValue\":" + initialValue +
                ", \"finalValue\":" + finalValue +
                ", \"percentReturn\":" + percentReturn +
                ", \"stockPnL\":" + stockPnL +
                ", \"optionPnL\":" + optionPnL +
                ", \"totalPnL\":" + totalPnL +
                ", \"totalTaxes\":" + totalTaxes +
                '}';
    }

}

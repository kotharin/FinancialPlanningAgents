package com.kotharin.financialplanner.model;

import java.math.BigDecimal;
import java.util.Map;

public class ConcentratedPositionAnalysis {
    private String analysis;
    private Map<String, BigDecimal> historicalPrices;

    public ConcentratedPositionAnalysis() {
    }

    public ConcentratedPositionAnalysis(String analysis, Map<String, BigDecimal> historicalPrices) {
        this.analysis = analysis;
        this.historicalPrices = historicalPrices;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Map<String, BigDecimal> getHistoricalPrices() {
        return historicalPrices;
    }

    public void setHistoricalPrices(Map<String, BigDecimal> historicalPrices) {
        this.historicalPrices = historicalPrices;
    }

    @Override
    public String toString() {
        return "ConcentratedPositionAnalysis{" +
                "analysis='" + analysis + '\'' +
                ", historicalPrices=" + historicalPrices +
                '}';
    }
}

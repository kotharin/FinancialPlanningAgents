package com.kotharin.financialplanner.model;

public class ConcentratedPositionAnalysis {
    private String symbol;
    private BaselineResult baselineResult;
    private CoveredCallSummary coveredCallSummary;

    public ConcentratedPositionAnalysis(String symbol, BaselineResult baselineResult,
            CoveredCallSummary coveredCallSummary) {
        this.symbol = symbol;
        this.baselineResult = baselineResult;
        this.coveredCallSummary = coveredCallSummary;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BaselineResult getBaselineResult() {
        return baselineResult;
    }

    public CoveredCallSummary getCoveredCallSummary() {
        return coveredCallSummary;
    }

    @Override
    public String toString() {
        return "ConcentratedPositionAnalysis {" +
                "symbol='" + symbol + '\'' +
                ", baselineResult=" + baselineResult +
                ", coveredCallSummary=" + coveredCallSummary +
                '}';
    }
}

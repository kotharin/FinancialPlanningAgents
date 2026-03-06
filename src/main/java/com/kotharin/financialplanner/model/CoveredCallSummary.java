package com.kotharin.financialplanner.model;

import java.time.LocalDate;
import java.util.TreeMap;

public class CoveredCallSummary {
    private TreeMap<LocalDate, CoveredCallResult> ccResults;
    private double initialValue;
    private double finalStockValue;
    private double finalCash;
    private double finalPortfolioValue;
    private double totalPnL;
    private int finalShares;

    public CoveredCallSummary() {
    }

    // Getters and Setters
    public TreeMap<LocalDate, CoveredCallResult> getCcResults() {
        return ccResults;
    }

    public void setCcResults(TreeMap<LocalDate, CoveredCallResult> ccResults) {
        this.ccResults = ccResults;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getFinalStockValue() {
        return finalStockValue;
    }

    public void setFinalStockValue(double finalStockValue) {
        this.finalStockValue = finalStockValue;
    }

    public double getFinalCash() {
        return finalCash;
    }

    public void setFinalCash(double finalCash) {
        this.finalCash = finalCash;
    }

    public double getFinalPortfolioValue() {
        return finalPortfolioValue;
    }

    public void setFinalPortfolioValue(double finalPortfolioValue) {
        this.finalPortfolioValue = finalPortfolioValue;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(double totalPnL) {
        this.totalPnL = totalPnL;
    }

    public int getFinalShares() {
        return finalShares;
    }

    public void setFinalShares(int finalShares) {
        this.finalShares = finalShares;
    }

    public CoveredCallSummary(TreeMap<LocalDate, CoveredCallResult> ccResults, double initialValue,
            double finalStockValue, double finalCash, double finalPortfolioValue, double totalPnL,
            int finalShares) {
        this.ccResults = ccResults;
        this.initialValue = initialValue;
        this.finalStockValue = finalStockValue;
        this.finalCash = finalCash;
        this.finalPortfolioValue = finalPortfolioValue;
        this.totalPnL = totalPnL;
        this.finalShares = finalShares;
    }

    public String toString() {
        return "CoveredCallSummary {" +
                "ccResults=" + ccResults +
                ", initialValue=" + initialValue +
                ", finalStockValue=" + finalStockValue +
                ", finalCash=" + finalCash +
                ", finalPortfolioValue=" + finalPortfolioValue +
                ", totalPnL=" + totalPnL +
                ", finalShares=" + finalShares +
                '}';
    }

}

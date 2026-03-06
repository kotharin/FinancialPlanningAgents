package com.kotharin.financialplanner.model;

import java.time.LocalDate;

public class OptionPos {
    private LocalDate openDate;
    private int dteOpen;
    private double strike;
    private int coveredShares;
    private double premiumOpenPerShare;
    private double premiumOpenTotal;

    public OptionPos() {
    }

    public OptionPos(LocalDate openDate, int dteOpen, double strike, int coveredShares, double premiumOpenPerShare,
            double premiumOpenTotal) {
        this.openDate = openDate;
        this.dteOpen = dteOpen;
        this.strike = strike;
        this.coveredShares = coveredShares;
        this.premiumOpenPerShare = premiumOpenPerShare;
        this.premiumOpenTotal = premiumOpenTotal;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    }

    public int getDteOpen() {
        return dteOpen;
    }

    public void setDteOpen(int dteOpen) {
        this.dteOpen = dteOpen;
    }

    public double getStrike() {
        return strike;
    }

    public void setStrike(double strike) {
        this.strike = strike;
    }

    public int getCoveredShares() {
        return coveredShares;
    }

    public void setCoveredShares(int coveredShares) {
        this.coveredShares = coveredShares;
    }

    public double getPremiumOpenPerShare() {
        return premiumOpenPerShare;
    }

    public void setPremiumOpenPerShare(double premiumOpenPerShare) {
        this.premiumOpenPerShare = premiumOpenPerShare;
    }

    public double getPremiumOpenTotal() {
        return premiumOpenTotal;
    }

    public void setPremiumOpenTotal(double premiumOpenTotal) {
        this.premiumOpenTotal = premiumOpenTotal;
    }
}

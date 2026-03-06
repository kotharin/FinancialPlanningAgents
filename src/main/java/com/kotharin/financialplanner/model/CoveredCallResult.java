package com.kotharin.financialplanner.model;

public class CoveredCallResult {
    private double shares;
    private double coveredShares;
    private double strikePrice;
    private double optionPremium;
    private double stockValue;
    private double stockPnL;
    private double optionPnL;
    private double cumulativeTaxes;
    private double cumulativeTLH;
    private double realizedStockGain;
    private double cash;
    private double portfolioValue;
    private double totalPnL;
    private double optionMark;
    private double intrinsic;
    private double extrinsic;
    private String exitReason;

    public CoveredCallResult() {
    }

    public CoveredCallResult(double shares, double coveredShares, double strikePrice, double optionPremium,
            double stockValue, double stockPnL, double optionPnL, double cumulativeTaxes, double cumulativeTLH,
            double realizedStockGain, double cash, double portfolioValue, double totalPnL, double optionMark,
            double intrinsic, double extrinsic, String exitReason) {
        this.shares = shares;
        this.coveredShares = coveredShares;
        this.strikePrice = strikePrice;
        this.optionPremium = optionPremium;
        this.stockValue = stockValue;
        this.stockPnL = stockPnL;
        this.optionPnL = optionPnL;
        this.cumulativeTaxes = cumulativeTaxes;
        this.cumulativeTLH = cumulativeTLH;
        this.realizedStockGain = realizedStockGain;
        this.cash = cash;
        this.portfolioValue = portfolioValue;
        this.totalPnL = totalPnL;
        this.optionMark = optionMark;
        this.intrinsic = intrinsic;
        this.extrinsic = extrinsic;
        this.exitReason = exitReason;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getCoveredShares() {
        return coveredShares;
    }

    public void setCoveredShares(double coveredShares) {
        this.coveredShares = coveredShares;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public double getOptionPremium() {
        return optionPremium;
    }

    public void setOptionPremium(double optionPremium) {
        this.optionPremium = optionPremium;
    }

    public double getStockValue() {
        return stockValue;
    }

    public void setStockValue(double stockValue) {
        this.stockValue = stockValue;
    }

    public double getStockPnL() {
        return stockPnL;
    }

    public void setStockPnL(double stockPnL) {
        this.stockPnL = stockPnL;
    }

    public double getOptionPnL() {
        return optionPnL;
    }

    public void setOptionPnL(double optionPnL) {
        this.optionPnL = optionPnL;
    }

    public double getCumulativeTaxes() {
        return cumulativeTaxes;
    }

    public void setCumulativeTaxes(double cumulativeTaxes) {
        this.cumulativeTaxes = cumulativeTaxes;
    }

    public double getCumulativeTLH() {
        return cumulativeTLH;
    }

    public void setCumulativeTLH(double cumulativeTLH) {
        this.cumulativeTLH = cumulativeTLH;
    }

    public double getRealizedStockGain() {
        return realizedStockGain;
    }

    public void setRealizedStockGain(double realizedStockGain) {
        this.realizedStockGain = realizedStockGain;
    }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getPortfolioValue() {
        return portfolioValue;
    }

    public void setPortfolioValue(double portfolioValue) {
        this.portfolioValue = portfolioValue;
    }

    public double getTotalPnL() {
        return totalPnL;
    }

    public void setTotalPnL(double totalPnL) {
        this.totalPnL = totalPnL;
    }

    public double getOptionMark() {
        return optionMark;
    }

    public void setOptionMark(double optionMark) {
        this.optionMark = optionMark;
    }

    public double getIntrinsic() {
        return intrinsic;
    }

    public void setIntrinsic(double intrinsic) {
        this.intrinsic = intrinsic;
    }

    public double getExtrinsic() {
        return extrinsic;
    }

    public void setExtrinsic(double extrinsic) {
        this.extrinsic = extrinsic;
    }

    public String getExitReason() {
        return exitReason;
    }

    public void setExitReason(String exitReason) {
        this.exitReason = exitReason;
    }
}

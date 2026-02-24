package com.kotharin.financialplanner.model;

public class Portfolio {
    private int riskScore;

    private String portfolio;
    private Double expectedReturn;
    private Double volatility;
    private String headers = "SPY,IJH,IWM,VWO,VEA,IEF,SHY,TLT,LEMB,HYG,VCLT,IAU,SCHH,PGX,BTCUSD,BIL,ExpectedReturn,Volatility";

    public Portfolio(int riskScore, String portfolio, Double expectedReturn, Double volatility) {
        this.riskScore = riskScore;
        this.portfolio = portfolio;
        this.expectedReturn = expectedReturn;
        this.volatility = volatility;
    }

    public int getRiskScore() {
        return riskScore;
    }

    public String getPortfolio() {
        return portfolio;
    }

    public Double getExpectedReturn() {
        return expectedReturn;
    }

    public Double getVolatility() {
        return volatility;
    }

    public String getHeaders() {
        return headers;
    }

}

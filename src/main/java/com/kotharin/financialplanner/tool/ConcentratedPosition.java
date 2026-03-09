package com.kotharin.financialplanner.tool;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import com.kotharin.financialplanner.model.BaselineResult;
import com.kotharin.financialplanner.model.ConcentratedPositionAnalysis;
import com.kotharin.financialplanner.model.CoveredCallResult;
import com.kotharin.financialplanner.model.CoveredCallSummary;
import com.kotharin.financialplanner.model.OverlayRequest;
import com.kotharin.financialplanner.model.OverlayState;
import com.kotharin.financialplanner.model.OptionPos;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.ToolContext;

import dev.langchain4j.agent.tool.Tool;
import net.jacobpeterson.alpaca.AlpacaAPI;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.common.historical.bar.enums.BarTimePeriod;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBar;
import net.jacobpeterson.alpaca.model.endpoint.marketdata.stock.historical.bar.StockBarsResponse;
import net.jacobpeterson.alpaca.model.properties.DataAPIType;
import net.jacobpeterson.alpaca.model.properties.EndpointAPIType;

public class ConcentratedPosition {
    private static final String ALPACA_KEY_ID = System.getenv("ALPACA_KEY_ID");
    private static final String ALPACA_SECRET_KEY = System.getenv("ALPACA_SECRET_KEY");

    private static TreeMap<LocalDate, BigDecimal> getHistoricalPrices(String symbol, String dateAcquiredStr) {
        TreeMap<LocalDate, BigDecimal> historicalPrices = new TreeMap<>();
        try {
            LocalDate acquiredDate = LocalDate.parse(dateAcquiredStr);
            ZonedDateTime start = acquiredDate.atStartOfDay(ZoneId.of("America/New_York"));
            ZonedDateTime end = ZonedDateTime.now(ZoneId.of("America/New_York")).minusMinutes(30);

            AlpacaAPI alpacaAPI = new AlpacaAPI(ALPACA_KEY_ID, ALPACA_SECRET_KEY, EndpointAPIType.PAPER,
                    DataAPIType.IEX);

            // Fetch monthly bars using Alpaca API
            BarTimePeriod timePeriod = BarTimePeriod.MONTH;

            StockBarsResponse response = alpacaAPI
                    .stockMarketData().getBars(symbol, start, end, null, null, 1, timePeriod, null, null);

            if (response != null && response.getBars() != null) {
                for (StockBar bar : response.getBars()) {
                    Date date = Date.from(bar.getTimestamp().toInstant());
                    LocalDate localDate = date.toInstant().atZone(ZoneId.of("America/New_York")).toLocalDate();
                    historicalPrices.put(localDate, BigDecimal.valueOf(bar.getClose()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching historical prices for " + symbol + " using Alpaca: " + e.getMessage());
        }
        return historicalPrices;
    }

    private static Double estimateVolatility(String symbol, Map<LocalDate, BigDecimal> historicalPrices,
            int lookBackDays) {

        if (historicalPrices.size() < 3) {
            return 0.30;
        }

        // Get prices chronologically (TreeMap guarantees order)
        List<BigDecimal> prices = new ArrayList<>(historicalPrices.values());

        // Window slicing (last N prices)
        int numPrices = prices.size();
        int startIndex = Math.max(0, numPrices - lookBackDays);
        List<BigDecimal> window = prices.subList(startIndex, numPrices);

        if (window.size() < 3) {
            return 0.30;
        }

        // Calculate log returns
        List<Double> logReturns = new ArrayList<>();
        for (int i = 1; i < window.size(); i++) {
            double priceToday = window.get(i).doubleValue();
            double priceYesterday = window.get(i - 1).doubleValue();

            // Log Return = ln(Price_Today / Price_Yesterday)
            double logReturn = Math.log(priceToday / priceYesterday);
            logReturns.add(logReturn);
        }

        if (logReturns.size() < 2) {
            return 0.30;
        }

        // Calculate Standard Deviation of log returns
        double sum = 0.0;
        for (Double ret : logReturns) {
            sum += ret;
        }
        double mean = sum / logReturns.size();

        double sumSquaredDiffs = 0.0;
        for (Double ret : logReturns) {
            sumSquaredDiffs += Math.pow(ret - mean, 2);
        }

        // Sample standard deviation (divided by N-1)
        double variance = sumSquaredDiffs / (logReturns.size() - 1);
        double stdDev = Math.sqrt(variance);

        // Annualize the daily volatility (assuming 252 trading days)
        double annualizedVolatility = stdDev * Math.sqrt(252);

        return annualizedVolatility;
    }

    private static CoveredCallSummary runCoveredCallOverlay(String symbol,
            OverlayRequest overlayRequest,
            TreeMap<LocalDate, BigDecimal> historicalPrices, String dateAcquired, int initialShares) {

        final TreeMap<LocalDate, CoveredCallResult> results = new TreeMap<>();

        LocalDate acquiredDate = LocalDate.parse(dateAcquired);
        BigDecimal initialPrice = historicalPrices.get(acquiredDate);
        BigDecimal initialValue = initialPrice.multiply(new BigDecimal(initialShares));

        Double volatility = estimateVolatility(symbol, historicalPrices, 60);

        BigDecimal basis = overlayRequest.getCostBasis();

        LocalDate lastReductionDate = historicalPrices.firstKey();
        boolean reductionTriggered = false;
        Double prevPrice = null;

        // Overlay State
        OverlayState state = OverlayState.builder()
                .shares(Double.valueOf(initialShares))
                .costBasis(basis)
                .build();

        // Reporting counters
        double totalRealizedOptionLoss = 0.0;
        int totalSharesSoldOnCallLoss = 0;
        double totalRealizedOptionPnl = 0.0;

        double riskFreeRate = 0.05; // 5% risk free rate estimate

        for (Map.Entry<LocalDate, BigDecimal> entry : historicalPrices.entrySet()) {
            LocalDate date = entry.getKey();
            double currentPrice = entry.getValue().doubleValue();
            String exitReason = "";

            // 1. Cash accrual at underlying return
            if ("underlying".equals(overlayRequest.getCashReturnMode()) && prevPrice != null && prevPrice > 0) {
                state.setCash(state.getCash() * (currentPrice / prevPrice));
            }

            // Optional scheduled reductions
            if (overlayRequest.getPositionReductionPctPerQuarter() > 0) {
                long daysSince = java.time.temporal.ChronoUnit.DAYS.between(lastReductionDate, date);
                if (daysSince >= 90) {
                    int reductionAmount = (int) (state.getShares()
                            * (overlayRequest.getPositionReductionPctPerQuarter() / 100.0));
                    reductionAmount = Math.min(reductionAmount, state.getShares().intValue());
                    if (reductionAmount > 0) {
                        state.setCash(state.getCash() + (reductionAmount * currentPrice));
                        state.setShares(Math.max(0.0, state.getShares() - reductionAmount));
                        lastReductionDate = date;
                    }
                }
            }

            if (overlayRequest.getReductionThresholdPct() != null && !reductionTriggered) {
                double gainPct = (currentPrice / initialPrice.doubleValue() - 1.0) * 100.0;
                if (gainPct >= overlayRequest.getReductionThresholdPct()) {
                    int reductionAmount = (int) (state.getShares() * 0.25);
                    reductionAmount = Math.min(reductionAmount, state.getShares().intValue());
                    if (reductionAmount > 0) {
                        state.setCash(state.getCash() + (reductionAmount * currentPrice));
                        state.setShares(Math.max(0.0, state.getShares() - reductionAmount));
                        reductionTriggered = true;
                    }
                }
            }

            // 2/3. Evaluate exit rules if option is open
            double optionMark = 0.0;
            double intrinsic = 0.0;
            double extrinsic = 0.0;

            if (state.getOpenOption() != null && state.getOpenOption().getCoveredShares() > 0) {
                long daysOpen = java.time.temporal.ChronoUnit.DAYS.between(state.getOpenOption().getOpenDate(), date);
                int dteRemaining = overlayRequest.getTargetDteDays() - (int) daysOpen;
                double t = Math.max(dteRemaining, 0) / 365.0;

                optionMark = blackScholesCall(currentPrice, state.getOpenOption().getStrike(), t, riskFreeRate,
                        volatility);
                intrinsic = Math.max(currentPrice - state.getOpenOption().getStrike(), 0.0);
                extrinsic = Math.max(optionMark - intrinsic, 0.0);

                exitReason = evaluateExitReason(
                        intrinsic, extrinsic, state.getOpenOption().getPremiumOpenPerShare(), optionMark,
                        dteRemaining, overlayRequest.getProfitCapturePct(), overlayRequest.getStopLossMultiple(),
                        overlayRequest.getExtrinsicThresholdPct());

                if (!exitReason.isEmpty()) {
                    // Expiration uses intrinsic as settlement
                    double closePerShare = "EXPIRE".equals(exitReason) ? intrinsic : optionMark;
                    double closeCostTotal = closePerShare * state.getOpenOption().getCoveredShares();

                    // Cash accounting
                    state.setCash(state.getCash() - closeCostTotal);

                    // Realized option PnL for this trade
                    double realizedOptionPnl = state.getOpenOption().getPremiumOpenTotal() - closeCostTotal;

                    // Reporting totals
                    totalRealizedOptionPnl += realizedOptionPnl;
                    state.setRealizedOptionPnl(state.getRealizedOptionPnl() + realizedOptionPnl);

                    if (realizedOptionPnl < 0) {
                        double lossAmt = Math.abs(realizedOptionPnl);
                        totalRealizedOptionLoss += lossAmt;

                        // Share reduction trigger on loss
                        BigDecimal cb = state.getCostBasis();
                        double triggerPx = cb.doubleValue() * (1 + overlayRequest.getShareReductionTriggerPct());
                        int sharesSold = 0;
                        if (currentPrice > triggerPx) {
                            sharesSold = (int) Math.min(state.getShares(), Math.ceil(lossAmt / currentPrice));
                            state.setShares(state.getShares() - sharesSold);
                            state.setCash(state.getCash() + (sharesSold * currentPrice));
                            totalSharesSoldOnCallLoss += sharesSold;
                        }
                    }

                    // Clear option
                    state.setOpenOption(null);
                    state.setOpenNextDay(true);
                    state.setLastCloseDate(date);
                }
            }
            // 6. Open a new option (next day only)
            boolean canOpenToday = (state.getOpenOption() == null) && (state.getShares() > 0);

            if (canOpenToday) {
                boolean eligible = state.getOpenNextDay() &&
                        (state.getLastCloseDate() == null || date.isAfter(state.getLastCloseDate()));

                if (eligible) {
                    int coveredShares = (int) (state.getShares() * (overlayRequest.getCoveragePct() / 100.0));
                    coveredShares = Math.max(0, Math.min(coveredShares, state.getShares().intValue()));

                    if (coveredShares > 0) {
                        double tOpen = overlayRequest.getTargetDteDays() / 365.0;
                        double strike = strikeForTargetDelta(currentPrice, tOpen, riskFreeRate, volatility,
                                overlayRequest.getTargetDelta());
                        double premiumOpenPerShare = blackScholesCall(currentPrice, strike, tOpen, riskFreeRate,
                                volatility);
                        double premiumOpenTotal = premiumOpenPerShare * coveredShares;

                        state.setCash(state.getCash() + premiumOpenTotal);

                        OptionPos newOption = new OptionPos(
                                date, overlayRequest.getTargetDteDays(), strike, coveredShares,
                                premiumOpenPerShare, premiumOpenTotal);

                        state.setOpenOption(newOption);
                        state.setOpenNextDay(false);
                    }
                }
            }

            double covShares = state.getOpenOption() != null ? state.getOpenOption().getCoveredShares() : 0;
            double optPrice = state.getOpenOption() != null ? state.getOpenOption().getStrike() : 0;
            double optPremium = state.getOpenOption() != null ? state.getOpenOption().getPremiumOpenTotal() : 0;
            double stockValue = state.getShares() * currentPrice;
            double stockPnL = 0.0;
            double optionPnL = totalRealizedOptionPnl;
            double cumTaxes = state.getCumulativeTaxes();
            double cumTLH = state.getCumulativeTlh();
            double realizedStockGain = state.getRealizedStockGain();
            double cash = state.getCash();
            double portfolioValue = stockValue + cash;
            double totalPnL = portfolioValue - initialValue.doubleValue();

            CoveredCallResult result = new CoveredCallResult(state.getShares(), covShares, optPrice, optPremium,
                    stockValue, stockPnL, optionPnL, cumTaxes, cumTLH, realizedStockGain, cash, portfolioValue,
                    totalPnL, optionMark, intrinsic, extrinsic, exitReason);

            results.put(date, result);
            prevPrice = currentPrice;
        }

        // Summary Statistics (Simplified for String Output)
        double finalShares = results.lastEntry().getValue().getShares();
        double finalStockValue = results.lastEntry().getValue().getStockValue();
        double finalCash = results.lastEntry().getValue().getCash();
        double finalPortfolioValue = finalStockValue + finalCash;
        double totalReturn = (finalPortfolioValue / initialValue.doubleValue() - 1.0) * 100.0;
        double totalPnL = results.lastEntry().getValue().getTotalPnL();
        int finalSharedReduced = totalSharesSoldOnCallLoss;

        CoveredCallSummary summary = new CoveredCallSummary(results, initialValue.doubleValue(), finalStockValue,
                finalCash, finalPortfolioValue, totalPnL, (int) finalShares);
        return summary;
    }

    // Standard Normal Cumulative Distribution Function
    private static double normCDF(double x) {
        return 0.5 * (1.0 + erf(x / Math.sqrt(2.0)));
    }

    // Error function approximation
    private static double erf(double z) {
        double t = 1.0 / (1.0 + 0.5 * Math.abs(z));
        double ans = 1 - t * Math.exp(-z * z - 1.26551223 +
                t * (1.00002368 +
                        t * (0.37409196 +
                                t * (0.09678418 +
                                        t * (-0.18628806 +
                                                t * (0.27886807 +
                                                        t * (-1.13520398 +
                                                                t * (1.48851587 +
                                                                        t * (-0.82215223 +
                                                                                t * (0.17087277))))))))));
        if (z >= 0)
            return ans;
        else
            return -ans;
    }

    // Black-Scholes Call Price
    private static double blackScholesCall(double S, double K, double T, double r, double sigma) {
        if (T <= 0 || sigma <= 0)
            return Math.max(0.0, S - K);

        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        return S * normCDF(d1) - K * Math.exp(-r * T) * normCDF(d2);
    }

    // Black-Scholes Call Delta
    private static double blackScholesCallDelta(double S, double K, double T, double r, double sigma) {
        if (T <= 0 || sigma <= 0)
            return S >= K ? 1.0 : 0.0;

        double d1 = (Math.log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        return normCDF(d1);
    }

    // Find Strike for Target Delta
    private static double strikeForTargetDelta(double S, double T, double r, double sigma, double targetDelta) {
        if (T <= 0 || sigma <= 0)
            return S;

        double lowK = S * 0.1;
        double highK = S * 5.0;
        double tolerance = 0.001;

        // Binary search for strike that yields target delta
        while ((highK - lowK) > tolerance) {
            double midK = (lowK + highK) / 2.0;
            double midDelta = blackScholesCallDelta(S, midK, T, r, sigma);

            // Delta decreases as Strike increases
            if (midDelta > targetDelta) {
                lowK = midK;
            } else {
                highK = midK;
            }
        }
        return Math.round(((lowK + highK) / 2.0) * 100.0) / 100.0;
    }

    // Evaluate Option Exit Reason
    private static String evaluateExitReason(double intrinsic, double extrinsic, double premiumOpenPerShare,
            double optionMark, int dteRemaining, double profitCapturePct,
            double stopLossMultiple, double extrinsicThresholdPct) {
        if (dteRemaining <= 0)
            return "EXPIRE";

        // Stop loss (e.g., mark > 2x premium collected)
        if (optionMark >= premiumOpenPerShare * (1.0 + stopLossMultiple))
            return "STOP_LOSS";

        // Profit capture (e.g., bought back for < 50% of premium collected)
        if (optionMark <= premiumOpenPerShare * (1.0 - profitCapturePct))
            return "PROFIT_TARGET";

        // Extrinsic threshold (time premium exhaustion)
        double initialExtrinsic = premiumOpenPerShare;
        if (initialExtrinsic > 0 && (extrinsic / initialExtrinsic) <= extrinsicThresholdPct) {
            return "EXTRINSIC_EXHAUSTED";
        }

        return "";
    }

    @Tool(name = "analyzeConcentratedPosition")
    public static String analyzeConcentratedPosition(
            @Schema(name = "riskScore", description = "Risk Score") Integer riskScore,
            @Schema(name = "symbol", description = "Concenbtrated Position Symbol") String symbol,
            @Schema(name = "dateAcquired", description = "Date Acquired in YYYY-MM-DD format") String dateAcquired,
            @Schema(name = "initialShares", description = "Initial Shares") Integer initialShares,
            @Schema(name = "toolContext", description = "the tool context") ToolContext toolContext) {
        String analysis = "You have a high concentration in " + symbol
                + ". This is a risk to your portfolio. You should consider diversifying your portfolio. ";
        System.out.println("----Concentrated Position----Risk Score: " + riskScore);
        System.out.println("----Concentrated Position----Analysis: " + analysis);
        System.out.println("----Concentrated Position----Date Acquired: " + dateAcquired);
        System.out.println("----Concentrated Position----Symbol: " + symbol);
        System.out.println("----Concentrated Position----Initial Shares: " + initialShares);

        // Get the historical prices for the symbol
        TreeMap<LocalDate, BigDecimal> historicalPrices = getHistoricalPrices(symbol, dateAcquired);
        System.out.println("----Concentrated Position----Historical Prices: " + historicalPrices);

        OverlayRequest overlayRequest = OverlayRequest.builder()
                .costBasis(historicalPrices.firstEntry().getValue())
                .build();
        CoveredCallSummary concentratedPositionSummary = runCoveredCallOverlay(symbol, overlayRequest, historicalPrices,
                dateAcquired, initialShares);

        System.out.println("----Concentrated Position----CP Analysis: " + concentratedPositionSummary);
        BaselineResult baselineResult = new BaselineResult(historicalPrices, initialShares);
        System.out.println("----Concentrated Position----Baseline Result: " + baselineResult);
        ConcentratedPositionAnalysis concentratedPositionAnalysis = new ConcentratedPositionAnalysis(symbol,
                baselineResult, concentratedPositionSummary);
        // System.out.println("----Concentrated Position----Concentrated Position
        // Analysis: " + concentratedPositionAnalysis);
        return concentratedPositionAnalysis.toString();
    }

}

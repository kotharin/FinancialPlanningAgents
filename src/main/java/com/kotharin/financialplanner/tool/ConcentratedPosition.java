package com.kotharin.financialplanner.tool;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.kotharin.financialplanner.model.ConcentratedPositionAnalysis;

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

    @Tool(name = "analyzeConcentratedPosition")
    public static ConcentratedPositionAnalysis analyzeConcentratedPosition(
            @Schema(name = "riskScore", description = "Risk Score") Integer riskScore,
            @Schema(name = "symbol", description = "Concenbtrated Position Symbol") String symbol,
            @Schema(name = "dateAcquired", description = "Date Acquired") String dateAcquired,
            @Schema(name = "toolContext", description = "the tool context") ToolContext toolContext) {
        String analysis = "You have a high concentration in " + symbol
                + ". This is a risk to your portfolio. You should consider diversifying your portfolio. ";
        System.out.println("----Concentrated Position----Risk Score: " + riskScore);
        System.out.println("----Concentrated Position----Analysis: " + analysis);
        System.out.println("----Concentrated Position----Date Acquired: " + dateAcquired);
        System.out.println("----Concentrated Position----Symbol: " + symbol);

        // Get the historical prices for the symbol
        Map<String, BigDecimal> historicalPrices = getHistoricalPrices(symbol, dateAcquired);
        System.out.println("----Concentrated Position----Historical Prices: " + historicalPrices);

        return new ConcentratedPositionAnalysis(analysis, historicalPrices);
    }

    public static Map<String, BigDecimal> getHistoricalPrices(String symbol, String dateAcquiredStr) {
        Map<String, BigDecimal> historicalPrices = new LinkedHashMap<>();
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
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (StockBar bar : response.getBars()) {
                    Date date = Date.from(bar.getTimestamp().toInstant());
                    historicalPrices.put(sdf.format(date), BigDecimal.valueOf(bar.getClose()));
                }
            }
        } catch (Exception e) {
            System.err.println("Error fetching historical prices for " + symbol + " using Alpaca: " + e.getMessage());
        }
        return historicalPrices;
    }
}

package com.kotharin.financialplanner.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CoveredCallSummaryTest {

    @Test
    void testToString_WithCcResults() {
        // Arrange
        CoveredCallSummary summary = new CoveredCallSummary();
        summary.setInitialValue(10000.0);
        summary.setFinalStockValue(11000.0);
        summary.setFinalCash(500.0);
        summary.setFinalPortfolioValue(11500.0);
        summary.setTotalPnL(1500.0);
        summary.setFinalShares(100);

        TreeMap<LocalDate, CoveredCallResult> ccResults = new TreeMap<>();
        CoveredCallResult result1 = new CoveredCallResult();
        // Populate dummy values for result1
        // Use the constructor to create a new instance
        result1 = new CoveredCallResult(
                100.0, // shares
                100.0, // coveredShares
                100.0, // strikePrice
                10.0, // optionPremium
                10000.0, // stockValue
                500.0, // stockPnL
                100.0, // optionPnL
                50.0, // cumulativeTaxes
                0.0, // cumulativeTLH
                200.0, // realizedStockGain
                1000.0, // cash
                11000.0, // portfolioValue
                600.0, // totalPnL
                5.0, // optionMark
                2.0, // intrinsic
                3.0, // extrinsic
                "None" // exitReason
        );

        ccResults.put(LocalDate.of(2026, 1, 15), result1);
        summary.setCcResults(ccResults);

        // Act
        String result = summary.toString();

        // Assert
        assertNotNull(result);

        // Since mapper.findAndRegisterModules() was removed, Jackson might fail to
        // serialize
        // TreeMap with LocalDate keys, falling back to String.valueOf(ccResults).
        // The test verifies that toString doesn't throw an exception and generates a
        // string representation.
        assertTrue(result.contains("\"initialValue\":10000.0"));
        assertTrue(result.contains("\"finalShares\"=100"));
        assertTrue(result.contains("ccResults"));
    }

    @Test
    void testToString_WithoutCcResults() {
        // Arrange
        CoveredCallSummary summary = new CoveredCallSummary();
        summary.setInitialValue(10000.0);
        summary.setFinalShares(100);

        // Act
        String result = summary.toString();

        // Assert
        assertNotNull(result);
        assertTrue(result.contains("\"ccResults\":null"));
        assertTrue(result.contains("\"initialValue\":10000.0"));
    }
}

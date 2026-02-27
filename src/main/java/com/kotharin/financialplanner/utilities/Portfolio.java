package com.kotharin.financialplanner.utilities;

import org.springframework.stereotype.Component;
import org.springframework.core.io.ClassPathResource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to read and manage portfolio data from CSV.
 */
@Component
public class Portfolio {

    private static final Map<Integer, String> riskScoreToPortfolioMap;

    static {
        riskScoreToPortfolioMap = loadPortfolios();
    }

    private Portfolio() {
        // Prevent instantiation
    }

    private static Map<Integer, String> loadPortfolios() {
        Map<Integer, String> map = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("SubAssestPortfolios.csv").getInputStream()))) {

            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }

                String[] parts = line.split(",", 2); // Split only on the first comma
                if (parts.length >= 2) {
                    try {
                        Integer riskScore = Integer.parseInt(parts[0].trim());
                        String portfolioData = parts[1].trim();
                        map.put(riskScore, portfolioData);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid risk score in line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SubAssestPortfolios.csv", e);
        }
        return map;
    }

    public static String getPortfolioByRiskScore(Integer riskScore) {
        return "Your PA should be: " + riskScoreToPortfolioMap.get(riskScore);
    }

    public static Map<Integer, String> getRiskScoreToPortfolioMap() {
        return riskScoreToPortfolioMap;
    }
}

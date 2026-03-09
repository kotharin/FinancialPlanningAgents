package com.kotharin.financialplanner.tool;

import java.util.Map;

import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.ToolContext;
import com.kotharin.financialplanner.utilities.Portfolio;

import dev.langchain4j.agent.tool.Tool;

public class RiskScorePortfolio {

    @Tool(name = "getRiskCommentary")
    public static Map<String, String> getRiskCommentary(
            @Schema(name = "riskScore", description = "Calculated Risk Score") float riskScore,
            @Schema(name = "totalScore", description = "Total Score based on questions") float totalScore,
            @Schema(name = "toolContext", description = "the tool context") ToolContext toolContext) {
        int riskScoreInt = (int) riskScore;
        String portfolio = Portfolio.getPortfolioByRiskScore(riskScoreInt);
        System.out.println("--------Risk Score: " + riskScoreInt);
        System.out.println("--------Portfolio: " + portfolio);
        System.out.println("--------Total Score: " + totalScore);
        return Map.of("riskCommentary", portfolio);
    }
}

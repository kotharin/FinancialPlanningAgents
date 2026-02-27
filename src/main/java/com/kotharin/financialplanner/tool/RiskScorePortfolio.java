package com.kotharin.financialplanner.tool;

import java.util.Map;

import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.ToolContext;
import com.kotharin.financialplanner.utilities.Portfolio;

import dev.langchain4j.agent.tool.Tool;

public class RiskScorePortfolio {

    @Tool(name = "getRiskCommentary")
    public static Map<String, String> getRiskCommentary(
            @Schema(name = "riskScore", description = "Risk Score") Integer riskScore,
            @Schema(name = "toolContext", description = "the tool context") ToolContext toolContext) {
        String portfolio = Portfolio.getPortfolioByRiskScore(riskScore);
        System.out.println("--------Risk Score: " + riskScore);
        System.out.println("--------Portfolio: " + portfolio);
        return Map.of("riskCommentary", portfolio);
    }
}

package com.kotharin.financialplanner.tool;

import java.util.Map;

import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.ToolContext;

import dev.langchain4j.agent.tool.Tool;

public class ConcentratedPosition {
    @Tool(name = "analyzeConcentratedPosition")
    public static Map<String, String> analyzeConcentratedPosition(
            @Schema(name = "riskScore", description = "Risk Score") Integer riskScore,
            @Schema(name = "symbol", description = "Concenbtrated Position Smbol") String symbol,
            @Schema(name = "toolContext", description = "the tool context") ToolContext toolContext) {
        String analysis = "You have a high concentration in " + symbol
                + ". This is a risk to your portfolio. You should consider diversifying your portfolio. ";
        System.out.println("----Concentrated Position----Risk Score: " + riskScore);
        System.out.println("----Concentrated Position----Analysis: " + analysis);
        return Map.of("analysis", analysis);
    }
}

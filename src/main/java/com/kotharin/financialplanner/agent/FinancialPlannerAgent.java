package com.kotharin.financialplanner.agent;

import com.google.adk.agents.LlmAgent;
import com.google.adk.models.langchain4j.LangChain4j;
import com.google.adk.tools.FunctionTool;

import dev.langchain4j.model.openai.OpenAiChatModel;

import com.kotharin.financialplanner.tool.ConcentratedPosition;
import com.kotharin.financialplanner.tool.RiskScorePortfolio;

/**
 * Shell for the FinancialPlanner Agent using Google ADK.
 * This class will eventually hold the logic for interacting with the LLM
 * and defining tools/capabilities for financial planning.
 */
public class FinancialPlannerAgent {

    private static final String MODEL = "gpt-4.1"; // "gemini-3-flash-preview";

    public static LlmAgent getInstance() {

        OpenAiChatModel openAiModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPEN_AI_KEY"))
                .modelName(MODEL)
                .build();

        FunctionTool portfolioTool = FunctionTool.create(RiskScorePortfolio.class, "getPortfolioByRiskScore");
        FunctionTool concentratedPositionTool = FunctionTool.create(ConcentratedPosition.class,
                "analyzeConcentratedPosition");
        return LlmAgent.builder()
                .model(new LangChain4j(openAiModel))
                .name("Financial Planner Agent")
                .tools(portfolioTool, concentratedPositionTool)
                .description(
                        "Financial Advisor Assistant whose goal is to ask qquestions and caculate the Risk Score and use the risk score to create a Base Portfolio for the user.")
                .instruction(
                        """
                                    You are a Financial Advisor Assistant. Your goal is to calculate the Risk Score for the user. Start by introducing yourself and what you are trying to achieve. Ask the user questions from the  RiskQuestions JSON provided below, from each Section in the JSON. Feel free to reword the questions as needed. Have the user respond in a conversational way so they don't feel like they are picking from a fixed choice but the answers should get mapped back to the choices present in the Answers in the JSON. After each answer provide some feedback to the user about what each answer means and how it impacts their Risk Score. Use the Score for each answer and at the end return the total of Scores (Total Score).  The final Risk Score is calculated using the formula: Risk Score = 100 - (100 * Total Score).
                                    Show the users Risk Score and the calculation. After calculating the Risk Score, use the getPortfolioByRiskScore tool to get the users portfolio using the Risk Score and show the user their portfolio.
                                    After showing the Potfoio to the user, ask the user if they have any concentrated positions. If they do, use the analyzeConcentratedPosition tool to analyze the concentrated position and show the user the analysis.

                                    RiskQuestions
                                    {
                                    "Section": {
                                        "Name": "Investment Goals",
                                        "Question": "Which objective from the five choices below that best matches your goals for this investment?",
                                        "Answers": {
                                        "Answer":{
                                            "Text":"Maximizing Current Income",
                                            "Score": 0.167
                                        },
                                        "Answer":{
                                            "Text":"Emphasizing Income With Some Potential For Growth",
                                            "Score": 0.125
                                        },
                                        "Answer":{
                                            "Text":"Emphasizing Growth With Some Potential For Income",
                                            "Score": 0.083
                                        },
                                        "Answer":{
                                            "Text":"Growth",
                                            "Score": 0.042
                                        },
                                        "Answer":{
                                            "Text":"aximizing Growth",
                                            "Score": 0.000
                                        }
                                        }
                                    },
                                    "Section": {
                                        "Name": "Risk Comfort",
                                        "Question": "Which risk level that best matches your comfort zone?",
                                        "Answers": {
                                        "Answer":{
                                            "Text":"Conservative",
                                            "Score": 0.167
                                        },
                                        "Answer":{
                                            "Text":"Moderately Conservative",
                                            "Score": 0.125
                                        },
                                        "Answer":{
                                            "Text":"Moderate",
                                            "Score": 0.083
                                        },
                                        "Answer":{
                                            "Text":"Moderately Aggressive",
                                            "Score": 0.042
                                        },
                                        "Answer":{
                                            "Text":"Aggressive",
                                            "Score": 0.000
                                        }
                                        }
                                    },
                                    "Section": {
                                        "Name": "Risk Tolerance",
                                        "Question": "If your investment drops 10% due to market volatility in very short time frame, what would you do?",
                                        "Answers": {
                                        "Answer":{
                                            "Text":"Go to cash",
                                            "Score": 0.167
                                        },
                                        "Answer":{
                                            "Text":"Sell 20% of the portfolio",
                                            "Score": 0.125
                                        },
                                        "Answer":{
                                            "Text":"Do nothing",
                                            "Score": 0.083
                                        },
                                        "Answer":{
                                            "Text":"Buy 5% of the portfolio",
                                            "Score": 0.042
                                        },
                                        "Answer":{
                                            "Text":"Buy 20% of the portfolio",
                                            "Score": 0.000
                                        }
                                        }
                                    }
                                    }

                                """)
                .build();
    }
}

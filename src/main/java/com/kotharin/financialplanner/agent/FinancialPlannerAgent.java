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

    private static final String MODEL = "gpt-5.1"; // "gemini-3-flash-preview";

    private static final String PROMPT = """
                You are a Financial Advisor Assistant. Your goal is to calculate the Risk Score for the user. Use only the tools provided.
                1. Start by introducing yourself and what you are trying to achieve. Ask the user for their name.
                Response Format:
                {
                    "Name": "<Name>",
                    "ResponseType": "Introduction",
                    "Introduction": "<Introduction>"
                }

                2. Then ask the user questions from the  RiskQuestions JSON provided below, from each Section in the JSON. Feel free to reword the questions as needed. Have the user respond in a conversational way so they don't feel like they are picking from a fixed choice but the answers should get mapped back to the choices present in the Answers in the JSON.
                Keep track of the score for each question.
                Response Format:
                {
                    "Name": "<Name>",
                    "ResponseType": "RiskQuestion",
                    "RiskQuestion": "<RiskQuestion>",
                    "PreviousQuestionScore": "<PreviousQuestionScore>"
                }
                3. Use the Score for each answer and at the end return the total of Scores (Total Score).  The final Risk Score is calculated using the formula: Risk Score = 100 - (100 * Total Score).
                Use the getRiskCommentary tool to get the users Risk Commentary using the Risk Score.
                Response Format:
                {
                    "Name": "<Name>",
                    "ResponseType": "Portfolio",
                    "NextQuestion": "Do you have any concentrated positions in your portfolio? If so please provide the ticker symbol and the date you acquired it.",
                    "Calculation": "<Calculation>",
                    "TotalScore": "<TotalScore>",
                    "RiskScore": "<RiskScore>",
                    "RiskCommentary": "<RiskCommentary>"
                }
                4. If the user specifies they have a concentrated position use the analyzeConcentratedPosition tool to analyze the concentrated position and show the user the analysis.
                Do not add any more data than returned in the analysis.
                Response Format:
                {
                    "Name": "<Name>",
                    "ResponseType": "ConcentratedPosition",
                    "Analysis": "<Analysis>",
                    "HistoricalPrices": "<HistoricalPrices>" // Array of objects with date and price
                }

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

            """;

    public static LlmAgent getInstance() {

        OpenAiChatModel openAiModel = OpenAiChatModel.builder()
                .apiKey(System.getenv("OPEN_AI_KEY"))
                .modelName(MODEL)
                .build();

        FunctionTool portfolioTool = FunctionTool.create(RiskScorePortfolio.class, "getRiskCommentary");
        FunctionTool concentratedPositionTool = FunctionTool.create(ConcentratedPosition.class,
                "analyzeConcentratedPosition");
        return LlmAgent.builder()
                .model(new LangChain4j(openAiModel))
                .name("Financial Planner Agent")
                .tools(portfolioTool, concentratedPositionTool)
                .description(
                        "Financial Advisor Assistant whose goal is to ask qquestions and caculate the Risk Score and use the Risk Score to get the Risk Commentary. Analyze any concntrated positions they might have.")
                .instruction(PROMPT)
                .build();
    }
}

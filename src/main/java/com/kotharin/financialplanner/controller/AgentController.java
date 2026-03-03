package com.kotharin.financialplanner.controller;

import com.google.adk.agents.LlmAgent;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.kotharin.financialplanner.agent.FinancialPlannerAgent;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/agent")
public class AgentController {
    private static final String APP_NAME = "financial-planner-agent";
    private static final String USER_ID = "user123";

    private final LlmAgent agent;
    private final Runner runner;
    private final InMemorySessionService sessionService;

    public AgentController() {
        this.agent = FinancialPlannerAgent.getInstance();
        this.sessionService = new InMemorySessionService();
        this.runner = Runner.builder()
                .agent(agent)
                .appName(APP_NAME)
                .sessionService(sessionService)
                .build();
    }

    @PostMapping("/chat")
    public String chat(@RequestBody com.kotharin.financialplanner.model.AgentRequest requestPayload) {

        String payload = requestPayload.getRequest();
        String conversationId = requestPayload.getConversationId();

        // Get the session based on the conversation ID and user ID
        Session session = getOrCreateSession(conversationId, USER_ID);
        Content content = Content.fromParts(Part.fromText(payload));

        System.out.println("Session ID: " + session.id());
        var events = runner.runAsync(session.userId(), session.id(), content);

        StringBuilder response = new StringBuilder();
        events.blockingForEach(event -> {
            if (event.content().isPresent()) {
                response.append(event.content().get().text());
            }
        });

        String jsonResponse = response.toString();
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode jsonNode = (ObjectNode) mapper.readTree(jsonResponse);
            jsonNode.put("conversationId", session.id());
            return mapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            e.printStackTrace();
            return jsonResponse;
        }
    }

    private Session getOrCreateSession(String conversationId, String userId) {
        if (conversationId != null && !conversationId.isEmpty()) {
            try {
                Session existingSession = runner.sessionService()
                        .getSession(runner.appName(), userId, conversationId, Optional.empty())
                        .blockingGet();
                if (existingSession != null) {
                    return existingSession;
                }
            } catch (Exception e) {
                // Session not found, proceed to create a new one
            }
        }
        return runner.sessionService().createSession(runner.appName(), userId).blockingGet();
    }

}

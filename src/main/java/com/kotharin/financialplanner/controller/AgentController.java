package com.kotharin.financialplanner.controller;

import com.google.adk.agents.LlmAgent;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import com.kotharin.financialplanner.agent.FinancialPlannerAgent;
import com.kotharin.financialplanner.model.AgentResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/agent")
public class AgentController {

    private final LlmAgent agent;
    private final InMemoryRunner runner;
    private final Session session;

    public AgentController() {
        this.agent = FinancialPlannerAgent.getInstance();
        this.runner = new InMemoryRunner(agent);
        this.session = runner
                .sessionService()
                .createSession(runner.appName(), "user123")
                .blockingGet();
    }

    @PostMapping("/chat")
    public String chat(@RequestBody com.kotharin.financialplanner.model.AgentRequest requestPayload) {

        String payload = requestPayload.getRequest();
        Content content = Content.fromParts(Part.fromText(payload));

        System.out.println("Session ID: " + session.id());
        var events = runner.runAsync(session.userId(), session.id(), content);

        StringBuilder response = new StringBuilder();
        events.blockingForEach(event -> {
            if (event.content().isPresent()) {
                response.append(event.content().get().text());
            }
        });

        // System.out.println("Response: " + response.toString());

        // Populate the AgentResponse
        // AgentResponse ar = new AgentResponse("", response.toString());
        // return ar.toJson();
        return response.toString();
    }

}

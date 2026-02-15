package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class AgentWithRouting {

    enum Destination {
        MATH, GENERAL
    }

    interface Router {
        @SystemMessage("You are a router. Your job is to classify the user's request into one of the following categories: MATH, GENERAL. Do not answer the question, just return the category.")
        Destination route(String text);
    }

    interface MathAgent {
        @SystemMessage("You are a mathematician. Solve the math problem.")
        String chat(String message);
    }

    interface GeneralAgent {
        @SystemMessage("You are a helpful general assistant.")
        String chat(String message);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Router router = AiServices.create(Router.class, model);
        MathAgent mathAgent = AiServices.create(MathAgent.class, model);
        GeneralAgent generalAgent = AiServices.create(GeneralAgent.class, model);

        String[] queries = {
                "What is 25 * 48?",
                "Tell me a joke about cats."
        };

        for (String query : queries) {
            System.out.println("User: " + query);
            Destination dest = router.route(query);
            System.out.println("Router decided: " + dest);

            String response;
            if (dest == Destination.MATH) {
                response = mathAgent.chat(query);
            } else {
                response = generalAgent.chat(query);
            }
            System.out.println("Agent: " + response);
            System.out.println("--------------------------------------------------");
        }
    }
}

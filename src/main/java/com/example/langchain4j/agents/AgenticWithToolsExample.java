package com.example.langchain4j.agents;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgenticWithToolsExample {

    public static class Calculator {
        @Tool("Calculates the square root of a number")
        public double squareRoot(@P("number") double number) {
            System.out.println("Called squareRoot with: " + number);
            return Math.sqrt(number);
        }
    }

    public interface MathAssistant {
        @Agent(description = "A math assistant")
        @UserMessage("{{message}}")
        String chat(@V("message") String message);
    }

    public static void main(String[] args) {
        // Using llama3.1 as it has better tool support than llama3
        ChatModel model = OllamaModelFactory.create("llama3.1");

        MathAssistant assistant = AgenticServices.createAgenticSystem(MathAssistant.class, model, 
                context -> {
             context.agentBuilder().tools(new Calculator());
        });

        String response = assistant.chat("What is the square root of 256?");
        System.out.println(response);
    }
}

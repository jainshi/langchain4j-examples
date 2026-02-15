package com.example.langchain4j.agents;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

public class AgentWithToolsAndMemory {

    static class Calculator {
        @Tool("Calculates the square root of a number")
        double squareRoot(double number) {
            System.out.println("Called squareRoot with: " + number);
            return Math.sqrt(number);
        }

        @Tool("Calculates the sum of two numbers")
        double add(double a, double b) {
            System.out.println("Called add with: " + a + " and " + b);
            return a + b;
        }
    }

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create("llama3.1");

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .tools(new Calculator())
                .build();

        System.out.println("--- Conversation Start ---");

        String q1 = "What is the square root of 144?";
        System.out.println("User: " + q1);
        String a1 = assistant.chat(q1);
        System.out.println("Assistant: " + a1);

        String q2 = "Add 50 to that result.";
        System.out.println("User: " + q2);
        String a2 = assistant.chat(q2);
        System.out.println("Assistant: " + a2);

        System.out.println("--- Conversation End ---");
    }
}

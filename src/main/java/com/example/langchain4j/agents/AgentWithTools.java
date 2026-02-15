package com.example.langchain4j.agents;

import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

public class AgentWithTools {

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
                .tools(new Calculator())
                .build();

        String question = "What is the square root of 48590?";
        System.out.println("User: " + question);

        String answer = assistant.chat(question);
        System.out.println("Assistant: " + answer);
    }
}

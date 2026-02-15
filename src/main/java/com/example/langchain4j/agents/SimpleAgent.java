package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

public class SimpleAgent {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        // 1. Create the ChatModel (Ollama)
        // Ensure you have Ollama running locally: 'ollama serve'
        // and the model pulled: 'ollama pull llama3'
        ChatModel model = OllamaModelFactory.create();

        // 2. Create the AI Service (Agent)
        Assistant assistant = AiServices.create(Assistant.class, model);

        // 3. Interact with the Agent
        String userMessage = "Hello! Can you generate a short 2-sentence story about a brave Java developer?";
        System.out.println("User: " + userMessage);

        String answer = assistant.chat(userMessage);
        System.out.println("Assistant: " + answer);
    }
}

package com.example.langchain4j.agents;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

public class AgentWithSystemMessage {

    interface Assistant {
        @SystemMessage("You are a helpful assistant that speaks like a pirate.")
        String chat(String message);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Assistant assistant = AiServices.create(Assistant.class, model);

        String userMessage = "Hello! Who are you?";
        System.out.println("User: " + userMessage);

        String answer = assistant.chat(userMessage);
        System.out.println("Assistant: " + answer);
    }
}

package com.example.langchain4j.agents;

import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.memory.ChatMemory;

public class AgentWithMemory {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        // ChatMemory will keep the last 10 messages
        ChatMemory chatMemory = MessageWindowChatMemory.withMaxMessages(10);

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(model)
                .chatMemory(chatMemory)
                .build();

        System.out.println("--- Conversation Start ---");

        System.out.println("User: Hi, my name is Shishir.");
        String answer1 = assistant.chat("Hi, my name is Shishir.");
        System.out.println("Assistant: " + answer1);

        System.out.println("User: What is my name?");
        String answer2 = assistant.chat("What is my name?");
        System.out.println("Assistant: " + answer2);

        System.out.println("--- Conversation End ---");
    }
}

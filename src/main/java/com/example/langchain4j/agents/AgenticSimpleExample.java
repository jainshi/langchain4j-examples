package com.example.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgenticSimpleExample {

    public interface Assistant {
        @Agent(name = "SimpleChatAgent", description = "A simple chat agent")
        @UserMessage("{{message}}")
        String chat(@V("message") String message);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Assistant assistant = AgenticServices.createAgenticSystem(Assistant.class, model);

        String response = assistant.chat("Hello, how are you?");
        System.out.println(response);
    }
}

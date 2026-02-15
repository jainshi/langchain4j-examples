package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.ollama.OllamaStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import java.time.Duration;

public class AgentWithStreaming {

    interface Assistant {
        TokenStream chat(String message);
    }

    public static void main(String[] args) {
        StreamingChatModel model = OllamaStreamingChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofMinutes(2))
                .logRequests(true)
                .logResponses(true)
                .build();

        Assistant assistant = AiServices.create(Assistant.class, model);

        String userMessage = "Tell me a funny joke about programming.";
        System.out.println("User: " + userMessage);
        System.out.print("Assistant: ");

        assistant.chat(userMessage)
                .onPartialResponse(System.out::print)
                .onCompleteResponse(System.out::print)
                .onError(Throwable::printStackTrace)
                .start();

        System.out.println();
    }
}

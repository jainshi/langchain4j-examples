package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;

import java.time.Duration;

public class OllamaModelFactory {

    private static final String BASE_URL = "http://localhost:11434";
    private static final String MODEL_NAME = "llama3.2";
    private static final Duration TIMEOUT = Duration.ofMinutes(2);

    public static ChatModel create() {
        return create(MODEL_NAME);
    }

    public static ChatModel create(String modelName) {
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(modelName)
                .timeout(TIMEOUT)
                .logRequests(true)
                .logResponses(true)
                .build();
    }
    
    public static ChatModel createJson() {
        return OllamaChatModel.builder()
                .baseUrl(BASE_URL)
                .modelName(MODEL_NAME)
                .timeout(TIMEOUT)
                .responseFormat(ResponseFormat.JSON)
                .build();
    }

    public static EmbeddingModel embeddingModel() {
        return OllamaEmbeddingModel.builder()
                .baseUrl(BASE_URL)
                .modelName("nomic-embed-text")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}

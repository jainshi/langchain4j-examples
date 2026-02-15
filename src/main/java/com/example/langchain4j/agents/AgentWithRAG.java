package com.example.langchain4j.agents;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.time.Duration;
import java.util.List;

import static com.example.langchain4j.agents.OllamaModelFactory.embeddingModel;

public class AgentWithRAG {

    interface Assistant {
        String chat(String message);
    }

    public static void main(String[] args) {
        // 1. Create an Embedding Model
        EmbeddingModel embeddingModel = embeddingModel();

        // 2. Create an Embedding Store
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 3. Ingest Documents
        Document document = Document.from("LangChain4j is a Java library that simplifies integrating LLMs into Java applications.");
        DocumentSplitter splitter = DocumentSplitters.recursive(300, 0);
        List<TextSegment> segments = splitter.split(document);
        embeddingStore.addAll(embeddingModel.embedAll(segments).content(), segments);

        // 4. Create a Content Retriever
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.6)
                .build();

        // 5. Create the Chat Model
        ChatModel chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName("llama3")
                .timeout(Duration.ofMinutes(2))
                .build();

        // 6. Create the AI Service (Agent) with RAG
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatModel(chatModel)
                .contentRetriever(contentRetriever)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(10))
                .build();

        // 7. Interact with the Agent
        String question = "What is LangChain4j?";
        System.out.println("User: " + question);

        String answer = assistant.chat(question);
        System.out.println("Assistant: " + answer);
    }
}

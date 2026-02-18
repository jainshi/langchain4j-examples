package com.example.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.util.List;

import static com.example.langchain4j.agents.OllamaModelFactory.embeddingModel;

public class AgenticWithRAGExample {

    public interface PromptRefiner {
        @Agent(description = "Refines the user's question to be more suitable for retrieval augmented generation", outputKey = "refinedQuestion")
        @UserMessage("Refine the following question to be more specific and suitable for searching in a knowledge base: {{question}}")
        String refine(@V("question") String question);
    }

    public interface RagAssistant {
        @Agent(description = "A helpful assistant that answers questions based on the provided documents")
        @UserMessage("{{refinedQuestion}}")
        String chat(@V("refinedQuestion") String refinedQuestion);
    }

    public interface RAGWorkflow {
        @SequenceAgent(
                name = "RAGWorkflow",
                description = "Refines the question and then answers it using RAG",
                subAgents = {PromptRefiner.class, RagAssistant.class}
        )
        String process(@V("question") String question);
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
        ChatModel chatModel = OllamaModelFactory.create();

        // 6. Create the Agentic Service with RAG
        RAGWorkflow workflow = AgenticServices.createAgenticSystem(RAGWorkflow.class, chatModel, context -> {
            if (context.agentServiceClass().equals(RagAssistant.class)) {
                context.agentBuilder().contentRetriever(contentRetriever);
            }
        });

        // 7. Interact with the Agent
        String question = "What is LangChain4j?";
        System.out.println("User: " + question);

        String answer = workflow.process(question);
        System.out.println("Assistant: " + answer);
    }
}

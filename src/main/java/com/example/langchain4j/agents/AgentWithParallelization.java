package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class AgentWithParallelization {

    interface Critic {
        @SystemMessage("You are a movie critic. Give a short review (1 sentence) of the movie.")
        String review(String movie);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ChatModel model = OllamaModelFactory.create();

        Critic critic = AiServices.create(Critic.class, model);

        String movie = "Inception";
        System.out.println("Movie: " + movie);

        // Parallel execution
        CompletableFuture<String> review1 = CompletableFuture.supplyAsync(() -> critic.review(movie + " (Positive perspective)"));
        CompletableFuture<String> review2 = CompletableFuture.supplyAsync(() -> critic.review(movie + " (Negative perspective)"));
        CompletableFuture<String> review3 = CompletableFuture.supplyAsync(() -> critic.review(movie + " (Neutral perspective)"));

        CompletableFuture.allOf(review1, review2, review3).join();

        System.out.println("Positive: " + review1.get());
        System.out.println("Negative: " + review2.get());
        System.out.println("Neutral:  " + review3.get());
    }
}

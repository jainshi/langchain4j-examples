package com.example.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.declarative.ParallelAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgenticParallelExample {

    public interface PositiveCritic {
        @Agent(description = "Gives a positive review of the movie", outputKey = "review")
        @UserMessage("Give a positive review of the movie {{movie}}")
        String review(@V("movie") String movie);
    }

    public interface NegativeCritic {
        @Agent(description = "Gives a negative review of the movie", outputKey = "review")
        @UserMessage("Give a negative review of the movie {{movie}}")
        String review(@V("movie") String movie);
    }

    public interface MovieReviewSystem {
        @ParallelAgent(
                name = "MovieReviewer",
                description = "Gets multiple reviews for a movie in parallel",
                subAgents = {PositiveCritic.class, NegativeCritic.class},
                outputKey = "review"
        )
        String review(@V("movie") String movie);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        MovieReviewSystem system = AgenticServices.createAgenticSystem(MovieReviewSystem.class, model);

        String result = system.review("Inception");
        System.out.println(result);
    }
}

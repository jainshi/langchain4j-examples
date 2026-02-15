package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgentWithEvaluatorOptimizer {

    interface Generator {
        @SystemMessage("You are a creative writer. Write a short poem about the given topic.")
        String generate(String topic);

        @SystemMessage("You are a creative writer. Improve the poem based on the feedback.")
        @UserMessage("Topic: {{topic}}\n\nPoem:\n{{poem}}\n\nFeedback:\n{{feedback}}\n\nImproved Poem:")
        String improve(@V("topic") String topic, @V("poem") String poem, @V("feedback") String feedback);
    }

    interface Evaluator {
        @SystemMessage("You are a strict poetry critic. Rate the poem from 1 to 10 and provide constructive feedback. Format: 'Score: X/10. Feedback: ...'")
        String evaluate(String poem);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Generator generator = AiServices.create(Generator.class, model);
        Evaluator evaluator = AiServices.create(Evaluator.class, model);

        String topic = "The silence of space";
        System.out.println("Topic: " + topic);

        String poem = generator.generate(topic);
        System.out.println("\n--- Initial Poem ---\n" + poem);

        for (int i = 0; i < 2; i++) { // Iterate 2 times
            String evaluation = evaluator.evaluate(poem);
            System.out.println("\n--- Evaluation " + (i + 1) + " ---\n" + evaluation);

            if (evaluation.contains("10/10")) {
                System.out.println("Perfect score achieved!");
                break;
            }

            poem = generator.improve(topic, poem, evaluation);
            System.out.println("\n--- Improved Poem " + (i + 1) + " ---\n" + poem);
        }
    }
}

package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgentWithReflection {

    interface Generator {
        @SystemMessage("You are a Java expert. Generate clean, efficient Java code for the requested task.")
        String generate(String task);

        @SystemMessage("You are a Java expert. Improve the previous code based on the critique.")
        @UserMessage("Task: {{task}}\n\nPrevious Code:\n{{code}}\n\nCritique:\n{{critique}}\n\nImproved Code:")
        String improve(@V("task") String task, @V("code") String code, @V("critique") String critique);
    }

    interface Critic {
        @SystemMessage("You are a senior Java developer. Review the provided code for errors, inefficiencies, and style issues. Be concise.")
        @UserMessage("Code:\n{{code}}\n\nCritique:")
        String critique(@V("code") String code);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Generator generator = AiServices.create(Generator.class, model);
        Critic critic = AiServices.create(Critic.class, model);

        String task = "Write a method to calculate the factorial of a number recursively.";
        System.out.println("Task: " + task);

        // Step 1: Generate
        String code = generator.generate(task);
        System.out.println("\n--- Initial Code ---\n" + code);

        // Step 2: Critique
        String critique = critic.critique(code);
        System.out.println("\n--- Critique ---\n" + critique);

        // Step 3: Improve
        String improvedCode = generator.improve(task, code, critique);
        System.out.println("\n--- Improved Code ---\n" + improvedCode);
    }
}

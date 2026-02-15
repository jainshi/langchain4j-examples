package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgentWithOrchestratorWorker {

    interface Orchestrator {
        @SystemMessage("You are a project manager. Break down the given task into 3 smaller sub-tasks. Return them as a comma-separated list.")
        String plan(String task);

        @SystemMessage("You are a project manager. Synthesize the results from the workers into a final report.")
        @UserMessage("Task: {{task}}\n\nResults:\n{{results}}\n\nFinal Report:")
        String synthesize(@V("task") String task, @V("results") String results);
    }

    interface Worker {
        @SystemMessage("You are a skilled worker. Execute the given sub-task and provide a concise result.")
        String execute(String subTask);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Orchestrator orchestrator = AiServices.create(Orchestrator.class, model);
        Worker worker = AiServices.create(Worker.class, model);

        String task = "Create a marketing plan for a new coffee brand.";
        System.out.println("Task: " + task);

        // Step 1: Plan
        String plan = orchestrator.plan(task);
        System.out.println("\n--- Plan ---\n" + plan);

        String[] subTasks = plan.split(",");
        StringBuilder results = new StringBuilder();

        // Step 2: Execute (Workers)
        for (String subTask : subTasks) {
            String result = worker.execute(subTask.trim());
            System.out.println("Worker executed: " + subTask.trim());
            results.append("- ").append(subTask.trim()).append(": ").append(result).append("\n");
        }

        // Step 3: Synthesize
        String finalReport = orchestrator.synthesize(task, results.toString());
        System.out.println("\n--- Final Report ---\n" + finalReport);
    }
}

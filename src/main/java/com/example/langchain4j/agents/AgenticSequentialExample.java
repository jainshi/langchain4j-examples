package com.example.langchain4j.agents;

import dev.langchain4j.agentic.Agent;
import dev.langchain4j.agentic.AgenticServices;
import dev.langchain4j.agentic.declarative.SequenceAgent;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

public class AgenticSequentialExample {

    public interface Writer {
        @Agent(description = "Writes a short story about the given topic", outputKey = "text")
        @UserMessage("Write a short story about {{topic}}")
        String write(@V("topic") String topic);
    }

    public interface Translator {
        @Agent(description = "Translates the given text to French", outputKey = "story")
        @UserMessage("Translate the following text to French: {{text}}")
        String translate(@V("text") String text);
    }

    public interface StoryWorkflow {
        @SequenceAgent(
                name = "StoryWriterAndTranslator",
                description = "Writes a story and then translates it",
                subAgents = {Writer.class, Translator.class},
                outputKey = "story"
        )
        String process(@V("topic") String topic);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        StoryWorkflow workflow = AgenticServices.createAgenticSystem(StoryWorkflow.class, model);

        String result = workflow.process("a brave knight");
        System.out.println(result);
    }
}

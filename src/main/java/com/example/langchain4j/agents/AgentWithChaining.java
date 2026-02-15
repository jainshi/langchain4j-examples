package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.SystemMessage;

public class AgentWithChaining {

    interface Writer {
        @SystemMessage("You are a creative writer. Write a short story (max 50 words) about the given topic.")
        String write(String topic);
    }

    interface Translator {
        @SystemMessage("You are a professional translator. Translate the given text into French.")
        String translate(String text);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.create();

        Writer writer = AiServices.create(Writer.class, model);
        Translator translator = AiServices.create(Translator.class, model);

        String topic = "A robot learning to love";
        System.out.println("Topic: " + topic);

        // Step 1: Write
        String story = writer.write(topic);
        System.out.println("\n--- Story (English) ---\n" + story);

        // Step 2: Translate
        String translation = translator.translate(story);
        System.out.println("\n--- Translation (French) ---\n" + translation);
    }
}

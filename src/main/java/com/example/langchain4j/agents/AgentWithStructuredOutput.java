package com.example.langchain4j.agents;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

public class AgentWithStructuredOutput {

    static class Person {
        String firstName;
        String lastName;
        int age;

        @Override
        public String toString() {
            return "Person{firstName='" + firstName + "', lastName='" + lastName + "', age=" + age + "}";
        }
    }

    interface PersonExtractor {
        @UserMessage("Extract information about a person from {{it}}")
        Person extractPerson(String text);
    }

    public static void main(String[] args) {
        ChatModel model = OllamaModelFactory.createJson();

        PersonExtractor extractor = AiServices.create(PersonExtractor.class, model);

        String text = "John Doe is a 30 year old software engineer.";
        System.out.println("Text: " + text);

        Person person = extractor.extractPerson(text);
        System.out.println("Extracted: " + person);
    }
}

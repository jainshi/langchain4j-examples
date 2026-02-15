# LangChain4j Agentic Workflows Examples

This project demonstrates various agentic workflows and patterns using LangChain4j and Ollama.

## Prerequisites

1.  **Java 17+**
2.  **Maven**
3.  **Ollama** running locally (`ollama serve`)
4.  **Llama3 model** pulled (`ollama pull llama3`)

## Examples

### Basic Concepts
*   **SimpleAgent.java**: A basic chat agent.
*   **AgentWithSystemMessage.java**: Using system messages to define persona.
*   **AgentWithMemory.java**: An agent that remembers conversation history.
*   **AgentWithStreaming.java**: Streaming the response token by token.
*   **AgentWithStructuredOutput.java**: Extracting structured data (JSON) from text.

### Tool Use & RAG
*   **AgentWithTools.java**: An agent that can use external tools (Calculator).
*   **AgentWithToolsAndMemory.java**: Combining tools with chat memory.
*   **AgentWithRAG.java**: Retrieval-Augmented Generation (chatting with documents) using `EasyRag`.

### Advanced Agentic Patterns (Standard LangChain4j)
*   **AgentWithReflection.java**: **Reflection Pattern**. An agent that critiques and improves its own output.
*   **AgentWithRouting.java**: **Routing Pattern**. Classifying user intent and routing to specialized agents.
*   **AgentWithChaining.java**: **Chaining Pattern**. Passing the output of one agent as input to another.
*   **AgentWithParallelization.java**: **Parallelization Pattern**. Running multiple agent tasks in parallel and aggregating results.
*   **AgentWithOrchestratorWorker.java**: **Orchestrator-Worker Pattern**. A manager agent breaks down a task, delegates to workers, and synthesizes the results.
*   **AgentWithEvaluatorOptimizer.java**: **Evaluator-Optimizer Pattern**. An iterative loop where one agent generates and another evaluates/critiques until a criteria is met.

### Agentic Framework Examples (`langchain4j-agentic`)
*   **AgenticSimpleExample.java**: Basic usage of `@Agent` and `AgenticServices`.
*   **AgenticWithToolsExample.java**: Using tools within the agentic framework.
*   **AgenticSequentialExample.java**: **Sequential Workflow**. Using `@SequenceAgent` to chain agents.
*   **AgenticParallelExample.java**: **Parallel Workflow**. Using `@ParallelAgent` to run agents concurrently.

## How to Run

You can run any of the examples using your IDE or Maven.

```bash
mvn compile exec:java -Dexec.mainClass="com.example.langchain4j.agents.SimpleAgent"
```

Replace `SimpleAgent` with the class name of the example you want to run.

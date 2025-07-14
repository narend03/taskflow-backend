package com.taskflow.taskflow_backend.service;

import java.util.List;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import com.openai.models.responses.ResponseTextConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.responses.ResponseTextConfig;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.taskflow.taskflow_backend.model.NaturalLanguageRequest;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.time.Duration;

@Service
public class LlmTaskService {
    private final String apiKey;
    private final OpenAIClient openAiClient;
    private final TaskRepository taskRepository;
    private final ObjectMapper mapper = new ObjectMapper();


    public LlmTaskService(@Value("${openai.api.key}") String apiKey, TaskRepository taskRepository) {
        this.apiKey = apiKey;
        this.taskRepository = taskRepository;

        if (apiKey == null || apiKey.isBlank()) {
            System.err.println("OpenAI API key is missing");
        } else {
            System.out.println("OpenAI API key injected ("
                    + apiKey.length() + " chars)");
        }

        this.openAiClient = OpenAIOkHttpClient.builder()
                .apiKey(apiKey)
                .timeout(Duration.ofSeconds(15))
                .build();

    }

    public Task createTaskFromNaturalLanguage(NaturalLanguageRequest request) throws IOException {
        String input = request.getInput();
        System.out.println("request: " + input);

        ResponseCreateParams params = ResponseCreateParams.builder()
                .model(ChatModel.GPT_3_5_TURBO)    
                .input(
                        "Convert the following into a task JSON with keys:\n" +
                                " • title (string)\n" +
                                " • description (string)\n" +
                                " • dueDate (YYYY-MM-DD)\n" +
                                " • priority (1-5)\n\n" +
                                "Here’s the request:\n" + input
                )
                .build();

        System.out.println("→ [NL] sending to OpenAI...");
        Response response = openAiClient.responses().create(params);
        System.out.println("← [NL] got OpenAI response!");

        String rawJson = response
                .text()                                       // Optional<ResponseTextConfig>
                .map(ResponseTextConfig::toString)            // call toString() on the config
                .orElseThrow(() -> new IllegalStateException("No text in response"));

//        String rawJson = response.text().toString();

        // Step 4: Parse JSON (use Jackson, Gson, etc.)
        TaskDto dto = mapper.readValue(rawJson, TaskDto.class);
        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setDueDate(LocalDate.parse(dto.getDueDate()));
        task.setPriority(dto.getPriority());
        task.setStatus("OPEN");

        return taskRepository.save(task);
    }

    private static class TaskDto {
        public String title;
        public String description;
        public String dueDate;
        public int priority;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getDueDate() { return dueDate; }
        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
        public int getPriority() { return priority; }
        public void setPriority(int priority) { this.priority = priority; }
    }

}

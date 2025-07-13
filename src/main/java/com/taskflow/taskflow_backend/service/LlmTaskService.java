//package com.taskflow.taskflow_backend.service;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.openai.client.OpenAIClient;
//import com.openai.client.okhttp.OpenAIOkHttpClient;
//import com.openai.models.ChatModel;
////import com.openai.models.chat.ChatCompletionRequest;
////import com.openai.models.chat.ChatMessage;
////import com.openai.models.chat.ChatCompletion;
//import com.openai.models.responses.Response;
//import com.openai.models.responses.ResponseCreateParams;
//import com.taskflow.taskflow_backend.model.NaturalLanguageRequest;
//import com.taskflow.taskflow_backend.model.Task;
//import com.taskflow.taskflow_backend.repository.TaskRepository;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class LlmTaskService {
//    private final OpenAIClient openAiClient;
//    private final TaskRepository taskRepository;
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    public LlmTaskService(@Value("${openai.api.key}") String apiKey, TaskRepository taskRepository) {
//
////        this.openAiClient = new OpenAIClient(new OpenAIOkHttpClient(apiKey));
////        OpenAIClient client = OpenAIOkHttpClient.fromEnv();
//
////        this.openAiClient = OpenAIOkHttpClient.builder()
////                .apiKey(apiKey)
////                .build();
//        this.openAiClient = OpenAIOkHttpClient.builder()
//                .apiKey(apiKey)
//                .build();
//        this.taskRepository = taskRepository;
//    }
//
//    public Task createTaskFromNaturalLanguage(NaturalLanguageRequest request) throws Exception {
//        String input = request.getInput();
//
//        ResponseCreateParams params = ResponseCreateParams.builder()
//                .model(ChatModel.GPT_3_5_TURBO)    // or GPT_4 if you have access
//                .input(
//                        "Convert the following into a task JSON with keys:\n" +
//                                " • title (string)\n" +
//                                " • description (string)\n" +
//                                " • dueDate (YYYY-MM-DD)\n" +
//                                " • priority (1-5)\n\n" +
//                                "Here’s the request:\n" + input
//                )
//                .build();
//
//        // Step 1: Build the completion request
////        ResponseCreateParams params = ResponseCreateParams.builder()
////                .model(ChatModel.GPT_3_5_TURBO)
////                .messages(List.of(
////                        ResponseCreateParams.Message.builder()
////                                .role("system")
////                                .content("You are a task extraction assistant. Return a JSON object with keys: title, description, dueDate (YYYY-MM-DD), and priority (1-5).")
////                                .build(),
////                        ResponseCreateParams.Message.builder()
////                                .role("user")
////                                .content(input)
////                                .build()
////                ))
////                .temperature(0.2)
////                .build();
//
//        // Step 2: Call the OpenAI API
//        Response response = openAiClient.responses().create(params);
//
//        // Step 3: Extract from LLM's response
//        String rawJson = response.text().toString();
////        String rawJson = response.
//        TaskDto dto = mapper.readValue(rawJson, TaskDto.class);
//
//        // Step 4: Parse JSON (use Jackson, Gson, etc.)
//        // For now, mock the fields as placeholders — replace with actual JSON parsing
//        Task task = new Task();
//        task.setId(UUID.randomUUID());
//        task.setTitle(dto.getTitle());
//        task.setDescription(dto.getDescription());
//        task.setDueDate(LocalDate.parse(dto.getDueDate()));
//        task.setPriority(dto.getPriority());
//        task.setStatus("OPEN");
////        task.setTitle("Example: " + input);
////        task.setDescription("Auto-generated from: " + input);
////        task.setDueDate(java.time.LocalDate.now().plusDays(2));
////        task.setPriority(3);
////        task.setStatus("OPEN");
//
//        // Step 5: Save to DB
//        return taskRepository.save(task);
//    }
//
//    private static class TaskDto {
//        public String title;
//        public String description;
//        public String dueDate;
//        public int priority;
//
//        // getters & setters omitted for brevity
//        // or annotate with Lombok @Data if you like
//        public String getTitle() { return title; }
//        public void setTitle(String title) { this.title = title; }
//        public String getDescription() { return description; }
//        public void setDescription(String description) { this.description = description; }
//        public String getDueDate() { return dueDate; }
//        public void setDueDate(String dueDate) { this.dueDate = dueDate; }
//        public int getPriority() { return priority; }
//        public void setPriority(int priority) { this.priority = priority; }
//    }
//
//}

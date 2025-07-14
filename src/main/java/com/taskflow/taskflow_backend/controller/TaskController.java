package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.model.NaturalLanguageRequest;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import com.taskflow.taskflow_backend.service.LlmTaskService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final LlmTaskService llmService;
    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository, LlmTaskService llmService) {
        this.taskRepository = taskRepository;
        this.llmService = llmService;
    }


    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(201).body(savedTask);
    }

    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable UUID id) {
        return taskRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID id, @Valid @RequestBody Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());
                    task.setDueDate(updatedTask.getDueDate());
                    task.setStatus(updatedTask.getStatus());
                    task.setPriority(updatedTask.getPriority());
                    taskRepository.save(task);
                    return ResponseEntity.ok(task);
                })
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTask(@PathVariable UUID id) {
        return taskRepository.findById(id).map(task -> {taskRepository.delete(task);
            return ResponseEntity.noContent().build();
        })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/from-nl")
    public ResponseEntity<Task> fromNaturalLanguage(@RequestBody NaturalLanguageRequest req) throws Exception {
        Task t = llmService.createTaskFromNaturalLanguage(req);
        return ResponseEntity.status(201).body(t);
    }
}
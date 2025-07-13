package com.taskflow.taskflow_backend.controller;

import com.taskflow.taskflow_backend.model.NaturalLanguageRequest;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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

    @PostMapping("/nl")
    public ResponseEntity<Task> createTaskFromNaturalLanguage(@RequestBody NaturalLanguageRequest request) {
        String input = request.getInput();

        // 🧠 Simulated LLM processing (Replace with actual LLM call later)
        Task task = new Task();
        task.setTitle("Example: " + input);
        task.setDescription("Auto-generated from: " + input);
        task.setDueDate(LocalDate.now().plusDays(3));
        task.setStatus("OPEN");
        task.setPriority(3);

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(201).body(savedTask);
    }

}
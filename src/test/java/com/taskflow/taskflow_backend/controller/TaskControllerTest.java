package com.taskflow.taskflow_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskflow.taskflow_backend.model.Task;
import com.taskflow.taskflow_backend.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TaskRepository taskRepository;

    private Task buildSampleTask() {
        return new Task(
                UUID.randomUUID(),
                "Sample Task",
                "This is a test task",
                LocalDate.now().plusDays(1),
                "OPEN",
                3
        );
    }

    @Test
    @DisplayName("POST /tasks - Success")
    void testCreateTaskSuccess() throws Exception {
        Task task = buildSampleTask();
        Mockito.when(taskRepository.save(Mockito.any(Task.class))).thenReturn(task);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Sample Task"));
    }

    @Test
    @DisplayName("GET /tasks/{id} - Found")
    void testGetTaskByIdFound() throws Exception {
        Task task = buildSampleTask();
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId().toString()));
    }

    @Test
    @DisplayName("GET /tasks/{id} - Not Found")
    void testGetTaskByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(taskRepository.findById(id)).thenReturn(Optional.empty());

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /tasks - Validation Error")
    void testCreateTaskValidationError() throws Exception {
        Task task = new Task(); // empty task to trigger validation errors

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest());
    }
}

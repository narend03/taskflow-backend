package com.taskflow.taskflow_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Title must not be blank")
    private String title;

    @Size(max = 500, message = "Description can't be more than 500 characters")
    private String description;

    @NotNull(message = "Due date is required")
    private LocalDate dueDate;

    @NotBlank(message = "Status is required")
    private String status;

    @Min(value = 1, message = "Priority must be at least 1")
    @Max(value = 5, message = "Priority must be at most 5")
    private int priority;

}

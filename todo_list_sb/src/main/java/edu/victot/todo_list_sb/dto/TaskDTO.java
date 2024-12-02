package edu.victot.todo_list_sb.dto;

import edu.victot.todo_list_sb.model.enums.Status;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        String title,
        String description,
        LocalDateTime createAt,
        LocalDateTime dueDate,
        Status status) {}

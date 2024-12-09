package edu.victot.todo_list_sb.dto;

import edu.victot.todo_list_sb.model.enums.Status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record TaskDTOResponse(
        Long id,
        String title,
        String description,
        LocalDateTime createAt,
        LocalDate dueDate,
        LocalTime dueTime,
        Status status
) {
}

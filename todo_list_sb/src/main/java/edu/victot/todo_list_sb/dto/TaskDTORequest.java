package edu.victot.todo_list_sb.dto;

import edu.victot.todo_list_sb.model.enums.Status;
import java.time.LocalDate;
import java.time.LocalTime;

public record TaskDTORequest(
        String title,
        String description,
        LocalDate dueDate,
        LocalTime dueTime,
        Status status) {}

package edu.victot.todo_list_sb.dto;

import edu.victot.todo_list_sb.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record TaskDTORequest(
        @Schema(description = "title of the task", example = "Title")
        String title,
        @Schema(description = "description of the task", example = "this is an short description of the task")
        String description,
        @Schema(description = "the date when the task will start", example = "12/12/2024")
        LocalDate dueDate,
        @Schema(description = "the time at the clock", example = "12:12:50")
        LocalTime dueTime,
        @Schema(description = "status of the task PENDENTE | EM_ANDAMENTO | CONCLUIDA", example = "PENDENTE")
        Status status) {
}

package edu.victot.todo_list_sb.controller;

import edu.victot.todo_list_sb.dto.TaskDTORequest;
import edu.victot.todo_list_sb.dto.TaskDTOResponse;
import edu.victot.todo_list_sb.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("task/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //TODO Trocar por EntityReponse
    //TODO implementar o handler

    @GetMapping
    public ResponseEntity<List<TaskDTOResponse>> getAllTasks() {
        return ResponseEntity.ok(taskService.getAllTasks());
    }

    @PostMapping
    public ResponseEntity<TaskDTOResponse> createTask(@RequestBody TaskDTORequest taskDTO) {//TODO a data está dando erroff
        TaskDTOResponse response = taskService.createTask(taskDTO);//TODO erro por usar enum usar @Valid para limitar os numeros
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<TaskDTOResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.of(Optional.of(taskService.getTaskById(id)));
    }

    @GetMapping("get-by-duedate/{dueDate}")
    public List<TaskDTOResponse> getTasksByDate(@PathVariable LocalDate dueDate) {
        return taskService.getTasksByDueDate(dueDate);
    }

    @DeleteMapping("delete-by-id/{id}")
    public ResponseEntity<TaskDTOResponse> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("update/{id}")
    public ResponseEntity<TaskDTOResponse> updateTask(@PathVariable Long id, @RequestBody TaskDTORequest taskDTO) {
        TaskDTOResponse task = taskService.updateTask(taskDTO, id);
        return ResponseEntity.status(201).body(task);
    }
}

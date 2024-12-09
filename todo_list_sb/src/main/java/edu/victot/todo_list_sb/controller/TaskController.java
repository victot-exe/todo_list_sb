package edu.victot.todo_list_sb.controller;

import edu.victot.todo_list_sb.dto.TaskDTORequest;
import edu.victot.todo_list_sb.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping
    public TaskDTORequest createTask(@RequestBody TaskDTORequest taskDTO) {
        return taskService.createTask(taskDTO);//TODO erro por usar enum, fazer em normal para ver se resolve, ou utilizar o DTO sem id
    }

    @GetMapping
    public List<TaskDTORequest> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDTORequest> getTaskById(@PathVariable Long id) {
        try{
            return ResponseEntity.of(Optional.of(taskService.getTaskById(id)));
        }catch(IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    public List<TaskDTORequest> getTasksByDate(String date) {
        return null;//TODO Implements
    }


}

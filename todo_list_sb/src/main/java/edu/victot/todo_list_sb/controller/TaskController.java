package edu.victot.todo_list_sb.controller;

import edu.victot.todo_list_sb.dto.TaskDTO;
import edu.victot.todo_list_sb.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("task/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //TODO Trocar por EntityReponse
    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO taskDTO) {
        return taskService.createTask(taskDTO);
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }

    public TaskDTO getTaskById(int id) {
        return null;//TODO Implements
    }

    public List<TaskDTO> getTasksByDate(String date) {
        return null;//TODO Implements
    }


}

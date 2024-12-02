package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTO;
import edu.victot.todo_list_sb.model.Task;
import edu.victot.todo_list_sb.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(TaskDTO taskDTO) {


        //TODO COlocar a validação em outra classe
        if(taskRepository.existsById(taskDTO.id())){
            Task task = taskRepository.findById(taskDTO.id()).get();

            boolean exists = taskDTO.title().equals(task.getTitle())
                    && taskDTO.description().equals(task.getDescription())
                    && taskDTO.dueDate().equals(task.getDueDate());
            if(exists){
                throw new IllegalArgumentException("A tarefa já existe");
            }
        }
        Task task = new Task(
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.dueDate(),
                taskDTO.status()
        );
        taskRepository.save(task);
        return taskDTO;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepository.findAll().stream().map(this::conversionTaskToDTO).toList();
    }

    public TaskDTO conversionTaskToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreateAt(),
                task.getDueDate(),
                task.getStatus());
    }
}

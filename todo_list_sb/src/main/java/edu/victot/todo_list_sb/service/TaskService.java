package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTORequest;
import edu.victot.todo_list_sb.model.Task;
import edu.victot.todo_list_sb.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTORequest createTask(TaskDTORequest taskDTO) {

        if(taskRepository.existsByDueDateAndDueTime(taskDTO.dueDate(), taskDTO.dueTime()))
            throw new IllegalArgumentException("You are busy this time");

        Task task = new Task(
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.dueDate(),
                taskDTO.dueTime(),
                taskDTO.status()
        );
        taskRepository.save(task);
        return taskDTO;
    }

    public List<TaskDTORequest> getAllTasks() {
        List<TaskDTORequest> list = taskRepository.findAll().stream().map(this::conversionTaskToDTO).toList();

        if(list.isEmpty())
            throw new IllegalStateException("List is empty");

        return list;
    }

    public TaskDTORequest getTaskById(Long id) {

        if(taskRepository.existsById(id))
            return conversionTaskToDTO(taskRepository.findById(id).get());

        throw new IllegalArgumentException("Task not found");
    }

    public TaskDTORequest updateTask(TaskDTORequest taskDTO, Long id) {

        if(taskRepository.existsById(id)){
            Optional<Task> result = taskRepository.findById(id);
            if(result.isPresent()){
                Task atualizar = result.get();
                atualizar.setTitle(taskDTO.title());
                atualizar.setDescription(taskDTO.description());
                atualizar.setDueDate(taskDTO.dueDate());
                atualizar.setStatus(taskDTO.status());
                taskRepository.save(atualizar);
                return conversionTaskToDTO(atualizar);
            }
        }
        throw new IllegalArgumentException("Id not found");
    }

    public void deleteTaskById(Long id) {
        if(!taskRepository.existsById(id)){
            throw new IllegalArgumentException("Task not found");
        }
        taskRepository.deleteById(id);
    }

    private TaskDTORequest conversionTaskToDTO(Task task) {
        return new TaskDTORequest(
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getDueTime(),
                task.getStatus());
    }
}

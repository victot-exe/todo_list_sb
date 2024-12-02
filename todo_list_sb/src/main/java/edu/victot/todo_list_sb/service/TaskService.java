package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTO;
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
        List<TaskDTO> list = taskRepository.findAll().stream().map(this::conversionTaskToDTO).toList();

        if(list.isEmpty())
            throw new IllegalStateException("List is empty");

        return list;
    }

    public TaskDTO getTaskById(Long id) {

        if(taskRepository.existsById(id))
            return taskRepository.findById(id).isPresent() ? conversionTaskToDTO(taskRepository.findById(id).get()) : null;

        throw new IllegalArgumentException("Task not found");
    }

    public TaskDTO updateTask(TaskDTO taskDTO) {
        Long id = taskDTO.id();

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

    private TaskDTO conversionTaskToDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreateAt(),
                task.getDueDate(),
                task.getStatus());
    }
}

package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTORequest;
import edu.victot.todo_list_sb.dto.TaskDTOResponse;
import edu.victot.todo_list_sb.model.Task;
import edu.victot.todo_list_sb.repository.TaskRepository;
import edu.victot.todo_list_sb.model.exception.BusyTimeException;
import edu.victot.todo_list_sb.model.exception.NonExistentData;
import org.springframework.stereotype.Service;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTOResponse> getAllTasks() {
        List<TaskDTOResponse> list = taskRepository.findAll().stream().map(this::conversionTaskToDTOResponse).toList();

        if(list.isEmpty())
            throw new NonExistentData("List is empty");

        return list;
    }

    public TaskDTOResponse createTask(TaskDTORequest taskDTO) {

        if(taskRepository.existsByDueDateAndDueTime(taskDTO.dueDate(), taskDTO.dueTime()))
            throw new BusyTimeException("You are busy this time");

        Task task = new Task(
                taskDTO.title(),
                taskDTO.description(),
                taskDTO.dueDate(),
                taskDTO.dueTime(),
                taskDTO.status()
        );
        taskRepository.save(task);

        return this.conversionTaskToDTOResponse(task);
    }

    public TaskDTOResponse getTaskById(Long id) {

        if(taskRepository.existsById(id))
            return conversionTaskToDTOResponse(taskRepository.findById(id).get());

        throw new NonExistentData("Task not found");
    }

    public List<TaskDTOResponse> getTasksByDueDate(LocalDate dueDate) {
        List<Task> list = new ArrayList<>();
        list = taskRepository.getTaskByDueDate(dueDate);
        if(list.isEmpty())
            throw new NonExistentData("List is empty");

        return list.stream().map(this::conversionTaskToDTOResponse).toList();
    }

    public void deleteTaskById(Long id) {
        if(!taskRepository.existsById(id)){
            throw new NonExistentData("Id not found");
        }
        taskRepository.deleteById(id);
    }

    public TaskDTOResponse updateTask(TaskDTORequest taskDTO, Long id) {

        if(taskRepository.existsById(id)){
            Optional<Task> result = taskRepository.findById(id);
            if(result.isPresent()){
                Task atualizar = result.get();
                atualizar.setTitle(taskDTO.title());
                atualizar.setDescription(taskDTO.description());
                atualizar.setDueDate(taskDTO.dueDate());
                atualizar.setDueTime(taskDTO.dueTime());
                atualizar.setStatus(taskDTO.status());
                taskRepository.save(atualizar);
                return conversionTaskToDTOResponse(atualizar);
            }
        }
        throw new NonExistentData("Id not found");
    }

    private TaskDTOResponse conversionTaskToDTOResponse(Task task) {
        return new TaskDTOResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getCreateAt(),
                task.getDueDate(),
                task.getDueTime(),
                task.getStatus()
        );
    }

    public void deleteAll() {
        taskRepository.deleteAll();
    }
}

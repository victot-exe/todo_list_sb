package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTORequest;
import edu.victot.todo_list_sb.dto.TaskDTOResponse;
import edu.victot.todo_list_sb.model.Task;
import edu.victot.todo_list_sb.model.enums.Status;
import edu.victot.todo_list_sb.repository.TaskRepository;
import edu.victot.todo_list_sb.model.exception.BusyTimeException;
import edu.victot.todo_list_sb.model.exception.NonExistentData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task;
    private TaskDTORequest taskDTO;

    @BeforeEach
    public void setUp() {
        String title = "Concluir relatório";
        String description = "Finalizar o relatório trimestral de vendas";
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDate dueDate = LocalDate.of(2024, 12, 30);
        LocalTime dueTime = LocalTime.of(12, 30);
        Status status = Status.PENDENTE;
        task = new Task(title, description, dueDate, dueTime, status);
        task.setId(1L);
        task.setCreateAt(creationDate);
        taskDTO = new TaskDTORequest(title, description, dueDate, dueTime, status);
    }

    @Test
    @DisplayName("Testando create task")
    public void deve_criar_uma_nova_task_e_retornar_a_mesma(){
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(taskRepository.existsByDueDateAndDueTime(LocalDate.of(2024, 12,30), LocalTime.of(12, 30)))
        .thenReturn(false);

        TaskDTOResponse result = taskService.createTask(taskDTO);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertNotNull(result);
    }

    @Test
    @DisplayName("Testando a exceção quando tenta adicionar uma tarefa em uma data e hora já ocupada")
    public void deve_retornar_uma_IllegalArgumentException_quando_tentar_salvar_uma_tarefa_em_uma_data_ocupada(){
        when(taskRepository.existsByDueDateAndDueTime(any(), any())).thenReturn(true);

        Exception result = assertThrows(BusyTimeException.class, () -> taskService.createTask(taskDTO));
        assertEquals("You are busy this time", result.getMessage());
    }

    @Test
    @DisplayName("Testando getAllTasks")
    public void deve_retornar_uma_list_de_taskDTORequest(){
        when(taskRepository.findAll()).thenReturn(List.of(task, task, task));
        List<TaskDTOResponse> result = taskService.getAllTasks();

        assertNotNull(result);
        assertInstanceOf(TaskDTOResponse.class, result.getFirst());
    }

    @Test
    @DisplayName("Testando o lançamento de exceção de getAllTasks quando a lista se encontrar vazia")
    public void deve_retornar_uma_IllegalStateException_pois_a_lista_esta_vazia(){

        when(taskRepository.findAll()).thenReturn(List.of());

        Exception result = assertThrows(NonExistentData.class, () -> taskService.getAllTasks());
        assertEquals("List is empty", result.getMessage());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Testando getTaskById")
    public void deve_retornar_taskDTORequest(){
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskDTOResponse result = taskService.getTaskById(anyLong());
        verify(taskRepository, times(1)).existsById(anyLong());
        verify(taskRepository, times(1)).findById(anyLong());
        assertNotNull(result);
        assertInstanceOf(TaskDTOResponse.class, result);
    }

    @Test
    @DisplayName("Testando o lançamento de exceção de getTaskById")
    public void deve_retornar_IllegalArgumentException_id_nao_encontrado(){
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        Exception result = assertThrows(NonExistentData.class, () -> taskService.getTaskById(anyLong()));
        assertEquals("Task not found", result.getMessage());
        verify(taskRepository, times(0)).findById(anyLong());
    }

    @Test
    @DisplayName("Testando getTaskByDueDate")
    public void deve_executar_getTaskByDueDate(){
        when(taskRepository.getTaskByDueDate(LocalDate.of(2024, 12, 30)))
                .thenReturn(List.of(task));

        List<TaskDTOResponse> result = taskService.getTasksByDueDate(LocalDate.of(2024, 12, 30));
        verify(taskRepository, times(1)).getTaskByDueDate(LocalDate.of(2024, 12, 30));
        assertNotNull(result);
        assertInstanceOf(TaskDTOResponse.class, result.getFirst());
    }

    @Test
    @DisplayName("Testando a exceção de getTaskByDueDate")
    public void deve_executar_getTaskByDueDate_e_retornar_uma_NonExistentData(){
        when(taskRepository.getTaskByDueDate(LocalDate.of(2024, 12, 30)))
                .thenReturn(List.of());

        Exception result = assertThrows(NonExistentData.class, () ->taskService.getTasksByDueDate(LocalDate.of(2024, 12, 30)));
        assertEquals("List is empty", result.getMessage());
        verify(taskRepository, times(1)).getTaskByDueDate(LocalDate.of(2024, 12, 30));
    }

    @Test
    @DisplayName("Testando deleteById")
    public void deve_executar_existsById_e_deleteById(){
        when(taskRepository.existsById(anyLong())).thenReturn(true);

        taskService.deleteTaskById(anyLong());
        verify(taskRepository, times(1)).existsById(anyLong());
        verify(taskRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Testando o lançamento de exceção de deleteById")
    public void deve_executar_existsById_e_lancar_uma_IllegalArgumentException(){
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        Exception result = assertThrows(NonExistentData.class, () -> taskService.deleteTaskById(anyLong()));

        assertEquals("Task not found", result.getMessage());
        verify(taskRepository, times(1)).existsById(anyLong());
    }

    @Test
    @DisplayName("Testando o updateTask")
    public void deve_executar_existsById_e_save_retornar_uma_TaskDTO_atualizada(){

        when(taskRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        TaskDTORequest novaTaskDTO = new TaskDTORequest(
                "novo titulo",
                "nova descricao",
                task.getDueDate(),
                task.getDueTime(),
                Status.CONCLUIDA
        );

        TaskDTOResponse autalizada = taskService.updateTask(novaTaskDTO, 1L);

        verify(taskRepository, times(1)).existsById(anyLong());
        verify(taskRepository, times(1)).findById(anyLong());
        verify(taskRepository, times(1)).save(any(Task.class));

        assertNotNull(autalizada);
    }

    @Test
    @DisplayName("Testando a exceção do updateTask")
    public void deve_executar_existsById_e_lancar_IllegalArgumentException(){
        when(taskRepository.existsById(anyLong())).thenReturn(false);

        Exception result = assertThrows(NonExistentData.class, () -> taskService.updateTask(taskDTO, 1L));
        assertEquals("Id not found", result.getMessage());
        verify(taskRepository, times(1)).existsById(anyLong());
    }

}


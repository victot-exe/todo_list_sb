package edu.victot.todo_list_sb.service;

import edu.victot.todo_list_sb.dto.TaskDTO;
import edu.victot.todo_list_sb.model.Task;
import edu.victot.todo_list_sb.model.enums.Status;
import edu.victot.todo_list_sb.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    private Task task;
    private TaskDTO taskDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        String title = "Concluir relatório";
        String description = "Finalizar o relatório trimestral de vendas";
        LocalDateTime creationDate = LocalDateTime.now();
        LocalDateTime dueDate = LocalDateTime.of(2024, 12, 30, 17, 0);
        Status status = Status.PENDENTE;
        task = new Task(title, description, dueDate, status);
        task.setId(1L);
        task.setCreateAt(creationDate);
        taskDTO = new TaskDTO(1L, title, description, creationDate, dueDate, status);
    }

    @Test
    public void deve_criar_uma_nova_task_e_retornar_a_mesma(){
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskDTO result = taskService.createTask(taskDTO);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Concluir relatório", result.title());
        assertEquals("Finalizar o relatório trimestral de vendas", result.description());
    }

    @Test
    public void deve_retornar_uma_excecao_quando_tentar_salvar_uma_tarefa_existente(){
        when(taskRepository.existsById(anyLong())).thenReturn(true);
        when(taskRepository.findById(anyLong())).thenReturn(Optional.of(task));

        assertThrows(IllegalArgumentException.class, () -> taskService.createTask(taskDTO));
    }

    @Test
    public void testar_conversao(){
        TaskDTO result = taskService.conversionTaskToDTO(task);

        assertNotNull(result);
        assertInstanceOf(TaskDTO.class, result);
    }
}

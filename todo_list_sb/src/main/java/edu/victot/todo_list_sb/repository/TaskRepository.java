package edu.victot.todo_list_sb.repository;

import edu.victot.todo_list_sb.dto.TaskDTOResponse;
import edu.victot.todo_list_sb.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    boolean existsByDueDateAndDueTime(LocalDate dueDate, LocalTime dueTime);

    List<Task> getTaskByDueDate(LocalDate dueDate);
}

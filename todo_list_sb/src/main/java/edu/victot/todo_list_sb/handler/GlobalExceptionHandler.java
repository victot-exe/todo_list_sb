package edu.victot.todo_list_sb.handler;

import edu.victot.todo_list_sb.model.exception.BusyTimeException;
import edu.victot.todo_list_sb.model.exception.NonExistentData;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusyTimeException.class)
    public ResponseEntity<String> handlesBusyTimeException(BusyTimeException ex) {
        String errorResponse = ex.getMessage();
        return ResponseEntity.status(409).body(errorResponse);
    }

    @ExceptionHandler(NonExistentData.class)
    public ResponseEntity<String> handlerNonExistentDataException(NonExistentData ex) {
        String errorResponse = ex.getMessage();
        return ResponseEntity.status(200).body(errorResponse);
    }

}

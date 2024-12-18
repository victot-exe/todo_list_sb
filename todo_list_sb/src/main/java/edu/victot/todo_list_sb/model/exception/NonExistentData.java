package edu.victot.todo_list_sb.model.exception;
//Exceção para quando for retornar uma lista vazia ou um id nao for encontrado
public class NonExistentData extends RuntimeException {

    public NonExistentData() {
        super();
    }

    public NonExistentData(String message) {
        super(message);
    }

    public NonExistentData(String message, Throwable cause) {
        super(message, cause);
    }
}

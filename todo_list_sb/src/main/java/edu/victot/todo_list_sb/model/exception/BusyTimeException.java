package edu.victot.todo_list_sb.model.exception;
//Para quando um
public class BusyTimeException extends RuntimeException{
    public BusyTimeException(){
        super();
    }

    public BusyTimeException(String message){
        super(message);
    }

    public BusyTimeException(String message, Throwable cause){
        super(message, cause);
    }
}

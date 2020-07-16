package jFile.exception;

public class LogException extends RuntimeException{
    public LogException(){
        super();
    }
    public LogException(Exception e){
        super(e);
    }
    public LogException(String msg){
        super(msg);
    }
}

package doan.bai_2.error.exceptions;

public class NotFoundSongException extends RuntimeException{
    public NotFoundSongException(String message){
        super(message);
    }
}

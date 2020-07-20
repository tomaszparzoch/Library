package pl.javastart.library.exception;

public class PublicationsAlreadyExistException extends RuntimeException{
    public PublicationsAlreadyExistException(String message) {
        super(message);
    }
}

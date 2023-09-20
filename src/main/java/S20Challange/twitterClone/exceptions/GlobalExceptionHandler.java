package S20Challange.twitterClone.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler //Bizim hatalar
    public ResponseEntity<MessageResponse> handleException(MessageException messageException){
        MessageResponse messageResponse = new MessageResponse(messageException.getStatus().value(),messageException.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(messageResponse, messageException.getStatus());
    }
    @ExceptionHandler(MethodArgumentNotValidException.class) //Annotation ile entityler üzerindeki hatalar
    public ResponseEntity handleBindErrors(MethodArgumentNotValidException exception){
        List errorList = exception.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    Map<String, String> errors= new HashMap<>();
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                    return errors;
                }).collect(Collectors.toList());
        return ResponseEntity.badRequest().body(errorList);
    }

    @ExceptionHandler //Global hatalar(Bizim dışımızdakiler)
    public ResponseEntity<MessageResponse> handleException(Exception exception){
        MessageResponse messageResponse = new MessageResponse(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage(),System.currentTimeMillis());
        return new ResponseEntity<>(messageResponse, HttpStatus.BAD_REQUEST);
    }
}

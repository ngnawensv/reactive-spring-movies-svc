package moviessvc.exeception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(moviesservice.exeception.MovieInfoClientException.class)
  public ResponseEntity<String> handlerClientException(
      moviesservice.exeception.MovieInfoClientException ex){
    log.error("Exception Caught in handlerClientException : {} ", ex.getMessage());
    return  ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handlerRunTimeException(RuntimeException ex){
    log.error("Exception Caught in handlerRunTimeException : {} ", ex.getMessage());
    return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
  }

}

package moviesservice.exeception;

import lombok.Getter;

@Getter
public class MovieInfoClientException extends RuntimeException{
  private  String message;
  private Integer statusCode;

  public MovieInfoClientException(String message, Integer statusCode) {
    super(message);
    this.message=message;
    this.statusCode = statusCode;
  }
}

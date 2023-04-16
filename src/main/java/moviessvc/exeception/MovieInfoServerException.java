package moviesservice.exeception;

public class MovieInfoServerException extends RuntimeException{
  private  String message;

  public MovieInfoServerException(String message) {
    super(message);
    this.message = message;
  }
}

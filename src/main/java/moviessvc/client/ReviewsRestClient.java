package moviessvc.client;

import lombok.extern.slf4j.Slf4j;
import moviesservice.exeception.ReviewsClientException;
import moviesservice.exeception.ReviewsServerException;
import moviessvc.domain.Review;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class ReviewsRestClient {

  private WebClient webClient;

  @Value("${restClient.reviews.url}")
  private String reviewsUrl;

  public ReviewsRestClient(WebClient webClient) {
    this.webClient = webClient;
  }

  public Flux<Review> retrieveReviews(String movieId){

    //Consume the endpoint with query param
    var url =  UriComponentsBuilder.fromHttpUrl(reviewsUrl)
        .queryParam("movieInfo", movieId)
        .buildAndExpand()
        .toUriString();

    return webClient.get()
        .uri(url,movieId)
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
          log.info("Status code is : {}",clientResponse.statusCode().value());
          if (clientResponse.statusCode().equals(HttpStatus.NOT_FOUND)) {
            return Mono.empty();
          }
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage -> Mono.error(new ReviewsClientException(responseMessage)));
        })

        .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
          log.info("Status code is : {}",clientResponse.statusCode().value());
          return clientResponse.bodyToMono(String.class)
              .flatMap(responseMessage -> Mono.error(new ReviewsServerException("Server Exception in  RewiewsService "+ responseMessage)));
        })
        .bodyToFlux(Review.class)
        .log();

  }
}

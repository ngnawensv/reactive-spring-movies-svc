package moviessvc.intg;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

import java.util.Objects;
import moviessvc.domain.Movie;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(value = "test")
@AutoConfigureWebTestClient
//@AutoConfigureWireMock(port = 8084) //spin up httpserver in port 8084
@TestPropertySource(
    properties = {
        "restClient.moviesInfo.url.http://localhost:8084/v1/moviesinfo",
        "restClient.reviews.url.http://localhost:8084/v1/reviews"
    }
)
 class MovieControllerIntegTest {

  @Autowired
  WebTestClient webTestClient;

  @Test
  void retrieveMovieById(){

    //given
    var movieId ="abc";
    stubFor(get(urlEqualTo("/v1/moviesinfo"+"/"+movieId))
        .willReturn(aResponse()
        .withHeader("Content-Type","application/json")
        .withBodyFile("movieinfo.json")));

    stubFor(get(urlPathEqualTo("/v1/reviews")).willReturn(aResponse()
        .withHeader("Content-Type","application/json")
        .withBodyFile("reviews.json")));

    //when
    webTestClient.get()
        .uri("/v1/movies/{id}", movieId)
        .exchange()
        .expectBody(Movie.class)
        .consumeWith(movieEntityExchangeResult -> {
          var movie = movieEntityExchangeResult.getResponseBody();
          assert Objects.requireNonNull(movie).getReviewList().size()==2;
          //assertEquals("Batman Begins",movie.getMovieInfo().getName());
        });

    //then

  }

}

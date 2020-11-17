package io.ssbswang.nflrushingserver;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NflRushingServerApplication.class,
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NflRushingServerApplicationTests {

	private static final Logger LOG = LoggerFactory.getLogger(NflRushingServerApplicationTests.class);

	// mock random port
	@LocalServerPort
	private int port;

	// HTTP exchange template
	TestRestTemplate restTemplate = new TestRestTemplate();

	@DisplayName("GET /stats - happy path")
	@Test
	public void getAllStatsTest() throws URISyntaxException {
		RequestEntity<Object> request = new RequestEntity<>(HttpMethod.GET, getUriFromRoute("stats"));
		ResponseEntity<Object> response = restTemplate.exchange(request, Object.class);

		assertThat("status code is 200", response.getStatusCode(), equalTo(HttpStatus.OK));

		assertThat("Content-Type is application/json",
				response.getHeaders().getContentType(),equalTo( MediaType.APPLICATION_JSON));

		assertThat("body is present", response.getBody() == null , is(false));
	}

	private URI getUriFromRoute(String route) throws URISyntaxException {
		return new URI(String.format("http://localhost:%s/%s", port, route));
	}
}
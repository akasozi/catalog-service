package co.ke.cloudflow.catalogservice;

import co.ke.cloudflow.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;
import static org.assertj.core.api.Assertions.*;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated() {

		var expectedBook = new Book("1234567890", "Amp It Up", "Frank Slootman", 9.00);

		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(expectedBook.isbn());
				});
	}

	@Test
	void whenGetByIsbnWhenBookDoesNotExistFails() {
		webTestClient
				.get()
				.uri("/books/1234567890")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void whenPostRequestWithIncorrectIsbnTheRequestFails() {

		var expectedBook = new Book("a1234567890", "Amp It Up", "Frank Slootman", 9.00);

		webTestClient
				.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isBadRequest()
				.expectBody(new ParameterizedTypeReference<Map<String,String>>(){}).value(mapEntry -> {
					assertThat(mapEntry).isNotNull();
					assertThat(mapEntry.entrySet().iterator().next().getKey()).isEqualTo("isbn");
					assertThat(mapEntry.entrySet().iterator().next().getValue()).isEqualTo("This ISBN format must be valid.");
				});
	}

}

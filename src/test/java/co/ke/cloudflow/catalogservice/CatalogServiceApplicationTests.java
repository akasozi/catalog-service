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
	void whenPutRequestThenBookUpdated() {

		String isbn = "1234567890";
		var bookToCreate = Book.of(isbn, "Amp It Up", "Frank Slootman", 9.00, "Manning");

		Book createdBook =  webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(bookToCreate.isbn());
				}).returnResult().getResponseBody();

		var bookToUpdate =
				Book.of(createdBook.isbn(), createdBook.title(), "Frank Slootman, Abu Kasozi", 29.99, "Manning");

		webTestClient
				.put()
				.uri("/books/" + createdBook.isbn())
				.bodyValue(bookToUpdate)
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(book -> {
					assertThat(book).isNotNull();
					assertThat(book.price()).isEqualTo(bookToUpdate.price());
				});

	}
	
	@Test
	void whenPostRequestThenBookCreated() {

		var expectedBook = Book.of("1234567892", "Amp It Up", "Frank Slootman", 9.00, "Manning");

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
	void whenGetRequestAndBookExistsThenSuccess() {

		var bookToCreate = Book.of("1234567894", "Amp It Up", "Frank Slootman", 9.00, "Manning");

		Book bookCreated = webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(bookToCreate.isbn());
				}).returnResult().getResponseBody();

		webTestClient
				.get()
				.uri("/books/" + bookCreated.isbn())
				.exchange()
				.expectStatus().isOk()
				.expectBody(Book.class).value(book -> {
					assertThat(book).isNotNull();
					assertThat(book.isbn()).isEqualTo(bookCreated.isbn());
				});
	}


	@Test
	void whenGetByIsbnWhenBookDoesNotExistFails() {
		webTestClient
				.get()
				.uri("/books/1234567895")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void whenPostRequestWithIncorrectIsbnTheRequestFails() {

		var expectedBook = Book.of("a1234567890", "Amp It Up", "Frank Slootman", 9.00, "Manning");

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

	@Test
	void whenDeleteRequestExistingBookThenRequestSucceeds() {

		var bookToCreate = Book.of("1234567893", "Amp It Up", "Frank Slootman", 9.00, "Manning");

		Book bookCreated = webTestClient
				.post()
				.uri("/books")
				.bodyValue(bookToCreate)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook -> {
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.isbn()).isEqualTo(bookToCreate.isbn());
				}).returnResult().getResponseBody();

		webTestClient
				.delete()
				.uri("/books/" + bookCreated.isbn())
				.exchange()
				.expectStatus().isNoContent();


		webTestClient
				.get()
				.uri("/books/" + bookCreated.isbn())
				.exchange()
				.expectStatus().isNotFound()
				.expectBody(String.class).value(errorMessage -> assertThat(errorMessage).isEqualTo("The book with ISBN " + bookCreated.isbn() + " was not found."));
	}

}

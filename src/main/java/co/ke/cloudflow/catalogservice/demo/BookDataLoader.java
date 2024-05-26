package co.ke.cloudflow.catalogservice.demo;


import co.ke.cloudflow.catalogservice.domain.Book;
import co.ke.cloudflow.catalogservice.domain.BookRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//@Profile("load-test-data")
@ConditionalOnProperty(name = "polar.testdata.enabled", havingValue = "true")
public class BookDataLoader {

    private final BookRepository bookRepository;

    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData() {

        bookRepository.deleteAll();

        var book1 = Book.of("1234567891", "Northern Lights", "Lyra Silverstar", 9.90, "Manning");
        var book2 = Book.of("1234567892", "Polar Journey", "Iorek Polarson", 12.90, "Manning");
        var book3 = Book.of("1234567893", "AI Winter", "James Cameron", 19.99, "Oreilly");

        bookRepository.saveAll(List.of(book1, book2, book3));

    }
}

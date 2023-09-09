package com.polarbookshop.catalogservice.web;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.domain.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Book> getAll() {
        return this.bookService.viewBookList();
    }

    @GetMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public Book getByIsbn(@PathVariable String isbn) {
        return this.bookService.viewBookDetails(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book post(@Valid @RequestBody Book book) {
        return this.bookService.addBookToCatalog(book);
    }

    @DeleteMapping("/{isbn}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String isbn) {
        this.bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("/{isbn}")
    @ResponseStatus(HttpStatus.OK)
    public Book put(@PathVariable String isbn, @Valid @RequestBody Book book) {
        return this.bookService.editBookDetails(isbn, book);
    }
}


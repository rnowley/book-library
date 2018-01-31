package demo.library.rest.controller;

import demo.library.rest.Book;
import demo.library.rest.data.BookNotFoundException;
import demo.library.rest.data.BookRepository;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/books")
public class BookController {

    private static final String MAX_LONG_AS_STRING = "9223372036854775807";
    private BookRepository bookRespository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRespository = bookRepository;
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public List<Book> books(
            @RequestParam(value = "max", defaultValue = MAX_LONG_AS_STRING) long max,
            @RequestParam(value = "count", defaultValue = "20") int count) {
        return bookRespository.findBooks(max, count);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET,
            produces = "application/json")
    public Book bookById(@PathVariable Long id) {
        return bookRespository.findOne(id);
    }

    @RequestMapping(value = "/isbn/{isbn}", method = RequestMethod.GET,
            produces = "application/json")
    public Book bookByISBN(@PathVariable String isbn) {
        return bookRespository.findByISBN(isbn);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Book> saveBook(@RequestBody Book book, UriComponentsBuilder ucb) {
        Book saved = bookRespository.save(book);

        HttpHeaders headers = new HttpHeaders();
        URI locationUri = ucb.path("/books/")
                .path(String.valueOf(saved.getId()))
                .build()
                .toUri();
        headers.setLocation(locationUri);

        ResponseEntity<Book> responseEntity = new ResponseEntity<>(saved, headers,
                HttpStatus.CREATED);
        return responseEntity;
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody
    Error bookNotFound(BookNotFoundException e) {
        long bookId = e.getBookId();
        return new Error(4, "Book [" + bookId + "] not found");
    }
}

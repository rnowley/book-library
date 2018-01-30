package demo.library.rest.data;

import demo.library.rest.Book;
import java.util.List;

public interface BookRepository {

    long count();

    List<Book> findBooks(long max, int count);

    Book findByISBN(String isbn);

    Book findOne(long id);

    Book save(Book book);
}

package demo.library.rest.data;

import demo.library.rest.Author;
import demo.library.rest.Book;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcBookRepository implements BookRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcBookRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public List<Book> findBooks(long max, int count) {
        List<Book> results = jdbc.query(
                "select Book.id, Book.title, Book.isbn, Book.date_published, Publisher.id as publisherId, Publisher.name as name, Author.id as authors_id, Author.lastName as authors_lastName, Author.firstName as authors_firstName"
                + " from Book"
                + " inner join Author_Book on Author_Book.bookId = Book.id"
                + " inner join Author on Author_Book.authorId = author.id"
                + " inner join Publisher on Book.publisher = publisher.id"
                + " where Book.id < ?"
                + " order by Book.date_published desc limit ?",
                resultSetExtractor, max, count);
        return results;
    }

    @Override
    public Book findByISBN(String isbn) {
        Book result = jdbc.queryForObject(
                "select Book.id, Book.title, Book.isbn, Book.date_published, Publisher.id as publisher_Id, Publisher.name as publisher_name, Author.id as authors_id, Author.lastName as authors_lastName, Author.firstName as authors_firstName"
                + " from Book"
                + " inner join Author_Book on Author_Book.bookId = Book.id"
                + " inner join Author on Author_Book.authorId = author.id"
                + " inner join Publisher on Book.publisher = publisher.id"
                + " where Book.isbn = ?"
                + " order by Book.id",
                bookResultSetExtractor, isbn);
        return result;
    }

    @Override
    public Book findOne(long id) {

        try {
            return jdbc.queryForObject(
                    "select Book.id, Book.title, Book.isbn, Book.date_published, Publisher.id as publisher_Id, Publisher.name as publisher_name, Author.id as authors_Id, Author.lastName as authors_lastName, Author.firstName as authors_firstName"
                    + " from Book"
                    + " inner join Author_Book on Author_Book.bookId = Book.id"
                    + " inner join Author on Author_Book.authorId = author.id"
                    + " inner join Publisher on Book.publisher = publisher.id"
                    + " where Book.id = ?"
                    + " order by Book.id",
                    bookResultSetExtractor, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException(id);
        }

    }

    @Override
    public Book save(Book book) {
        long bookId = insertBookAndReturnId(book);

        for (Author author : book.getAuthor()) {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbc)
                    .withTableName("Author_Book");
            Map<String, Object> args = new HashMap<>();
            args.put("authorId", author.getId());
            args.put("bookId", bookId);
            jdbcInsert.execute(args);
        }

        return new Book(bookId, book.getTitle(), book.getPublisher(),
                book.getAuthor(), book.getISBN(), book.getDatePublished());
    }

    private long insertBookAndReturnId(Book book) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbc)
                .withTableName("Book");
        jdbcInsert.setGeneratedKeyName("id");
        Map<String, Object> args = new HashMap<>();
        args.put("title", book.getTitle());
        args.put("publisher", book.getPublisher().getId());
        args.put("isbn", book.getISBN());
        args.put("date_published", book.getDatePublished());
        long bookId = jdbcInsert.executeAndReturnKey(args).longValue();
        return bookId;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(
                "select count(id)"
                + " from Book", Long.class);
    }

    @Override
    public void delete(long id) {
        jdbc.update("delete from Author_Book where bookId=?", id);
        jdbc.update("delete from Book where id=?", id);
    }

    /**
     *
     */
    private final ResultSetExtractor<List<Book>> resultSetExtractor
            = JdbcTemplateMapperFactory
                    .newInstance()
                    .newResultSetExtractor(Book.class);

    private final RowMapper<Book> bookResultSetExtractor
            = JdbcTemplateMapperFactory
                    .newInstance()
                    .newRowMapper(Book.class);

}

package demo.library.rest.data;

import demo.library.rest.Author;
import demo.library.rest.Book;
import demo.library.rest.Publisher;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                "select Book.id, Book.title, Publisher.id as publisherId, Publisher.name as name, Author.id as authorId, Author.lastName as lastName, Author.firstName as firstName, Book.isbn, Book.date_published"
                + " from Book, Author, Publisher"
                + " where Book.id < ?"
                + " order by Book.date_published desc limit ?",
                resultSetExtractor, max, count);
        return results;
    }

    @Override
    public Book findByISBN(String isbn) {
        Book result = jdbc.queryForObject(
                "select Book.id, Book.title, Book.isbn, Book.date_published, Publisher.id as publisher_Id, Publisher.name as publisher_name, Author.id as author_id, Author.lastName as author_lastName, Author.firstName as author_firstName"
                + " from Book"
                + " left outer join Author on Book.author = author.id"
                + " left outer join Publisher on Book.publisher = publisher.id"
                + " where Book.isbn = ?",
                bookResultSetExtractor, isbn);
        return result;
    }

    @Override
    public Book findOne(long id) {

        try {
            return jdbc.queryForObject(
                    "select Book.id, Book.title, Book.isbn, Book.date_published, Publisher.id as publisher_Id, Publisher.name as publisher_name, Author.id as author_Id, Author.lastName as author_lastName, Author.firstName as author_firstName"
                    + " from Book, Author, Publisher"
                    + " where Book.publisher = Publisher.id and Book.author = Author.id and Book.id = ?",
                    bookResultSetExtractor, id);
        } catch (EmptyResultDataAccessException e) {
            throw new BookNotFoundException(id);
        }

    }

    @Override
    public Book save(Book book) {
        long bookId = insertBookAndReturnId(book);
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
        args.put("author", book.getAuthor().getId());
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
        jdbc.update("delete from Book where id=?", id);
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            long publisherId = resultSet.getLong("publisherId");
            String publisherName = resultSet.getNString("name");
            Publisher publisher = new Publisher(publisherId, publisherName);
            long authorId = resultSet.getLong("authorId");
            String lastName = resultSet.getNString("lastName");
            String firstName = resultSet.getNString("firstName");
            Author author = new Author(authorId, lastName, firstName);

            return new Book(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    publisher,
                    author,
                    resultSet.getString("isbn"),
                    resultSet.getTime("date_published"));
        }
    }

    private final ResultSetExtractor<List<Book>> resultSetExtractor
            = JdbcTemplateMapperFactory
                    .newInstance()
                    .addKeys("id")
                    .newResultSetExtractor(Book.class);

    private final RowMapper<Book> bookResultSetExtractor
            = JdbcTemplateMapperFactory
                    .newInstance()
                    .newRowMapper(Book.class);

}

package demo.library.rest.data;

import demo.library.rest.Book;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
        return jdbc.query(
                "select id, title, publisher, author, isbn, date_published"
                + " from Book"
                + " where id < ?"
                + " order by date_published desc limit ?",
                new BookRowMapper(), max, count);
    }

    @Override
    public Book findByISBN(String isbn) {
        return jdbc.queryForObject(
                "select id, title, publisher, author, isbn, date_published"
                + " from Book"
                + " where isbn = ?",
                new BookRowMapper(), isbn);
    }

    @Override
    public Book findOne(long id) {
        return jdbc.queryForObject(
                "select id, title, publisher, author, isbn, date_published"
                + " from Book"
                + " where id = ?",
                new BookRowMapper(), id);

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
        args.put("publisher", book.getPublisher());
        args.put("author", book.getAuthor());
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

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Book(
                    resultSet.getLong("id"),
                    resultSet.getString("title"),
                    resultSet.getString("publisher"),
                    resultSet.getString("author"),
                    resultSet.getString("isbn"),
                    resultSet.getTime("date_published"));
        }
    }

}

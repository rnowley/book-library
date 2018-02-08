package demo.library.rest.data;

import demo.library.rest.Author;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author raymond
 */
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcAuthorRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(
                "select count(id)"
                + "from Author", Long.class);
    }

    @Override
    public Author findOne(long id) {

        try {
            return jdbc.queryForObject(
                    "select id, lastName, firstName"
                    + " from Author where id = ?",
                    new AuthorRowMapper(), id
            );
        } catch (EmptyResultDataAccessException e) {
            throw new AuthorNotFoundException(id);
        }
    }

    @Override
    public List<Author> findAuthors(long max, int count) {
        return jdbc.query(
                "select id, lastName, firstName"
                + " from Author"
                + " where id > ?"
                + " order by id asc limit ?",
                new AuthorRowMapper(), max, count);

    }

    @Override
    public Author save(Author author) {
        long authorId = insertAuthorAndReturnId(author);
        return new Author(authorId, author.getLastName(), author.getFirstName());
    }

    private long insertAuthorAndReturnId(Author author) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbc).withTableName("Author");
        jdbcInsert.setGeneratedKeyName("id");
        Map<String, Object> args = new HashMap<>();
        args.put("lastName", author.getLastName());
        args.put("firstName", author.getFirstName());
        long authorId = jdbcInsert.executeAndReturnKey(args).longValue();
        return authorId;
    }

    @Override
    public void delete(long id) {
        jdbc.update("delete from Author where id=?", id);
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Author(
                    resultSet.getLong("id"),
                    resultSet.getNString("lastName"),
                    resultSet.getNString("firstName"));
        }
    }

}

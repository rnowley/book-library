package demo.library.rest.data;

import demo.library.rest.Publisher;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 * @author raymond
 */
public class JdbcPublisherRepository implements PublisherRepository {

    private JdbcTemplate jdbc;

    @Autowired
    public JdbcPublisherRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    public long count() {
        return jdbc.queryForObject(
                "select count(id)"
                + " from Publisher", Long.class);
    }

    @Override
    public List<Publisher> findPublishers(long max, int count) {
        return jdbc.query(
                "select id, name"
                + " from Publisher"
                + " where id > ?"
                + " order by id asc limit ?",
                new PublisherRowMapper(), max, count);
    }

    private static class PublisherRowMapper implements RowMapper<Publisher> {

        @Override
        public Publisher mapRow(ResultSet resultSet, int i) throws SQLException {
            return new Publisher(
                    resultSet.getLong("id"),
                    resultSet.getNString("name")
            );
        }

    }

}

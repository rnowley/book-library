package demo.library.rest.data;

import demo.library.rest.Author;
import java.util.List;

/**
 *
 * @author raymond
 */
public interface AuthorRepository {

    long count();

    Author findOne(long id);

    List<Author> findAuthors(long max, int count);

    Author save(Author author);

    void delete(long id);
}

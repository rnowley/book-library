package demo.library.rest.data;

import demo.library.rest.Author;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author raymond
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfig.class)
public class JdbcAuthorRepositoryTest {

    @Autowired
    JdbcAuthorRepository authorRepository;

    @Test
    public void count() {
        assertEquals(4, authorRepository.count());
    }

    @Test
    public void findOne() {
        Author result = authorRepository.findOne(1L);
        assertEquals("Joshi", result.getLastName());
    }

    @Test
    public void findAuthors() {
        List<Author> result1 = authorRepository.findAuthors(0, 2);
        assertEquals(2, result1.size());
        assertEquals("Joshi", result1.get(0).getLastName());
        assertEquals("Daywalt", result1.get(1).getLastName());

        List<Author> result2 = authorRepository.findAuthors(2, 2);
        assertEquals(2, result2.size());
        assertEquals("Lucas", result2.get(0).getLastName());
        assertEquals("Kerrisk", result2.get(1).getLastName());

        List<Author> result3 = authorRepository.findAuthors(5, 2);
        assertEquals(0, result3.size());
    }

    @Test
    @Transactional
    public void save() {
        assertEquals(4, authorRepository.count());
        Author author = new Author(null, "Adams", "Douglas");
        Author saved = authorRepository.save(author);
        assertEquals(5, authorRepository.count());
        assertNewAuthor(saved);
        assertNewAuthor(authorRepository.findOne(5L));
    }

    @Test
    @Transactional
    public void delete() {
        assertEquals(4, authorRepository.count());
        assertNotNull(authorRepository.findOne(4));
        authorRepository.delete(4L);
        assertEquals(3, authorRepository.count());
        assertNotNull(authorRepository.findOne(1));
        assertNotNull(authorRepository.findOne(2));
        assertNotNull(authorRepository.findOne(3));
    }

    private void assertNewAuthor(Author author) {
        assertEquals(5, author.getId().longValue());
    }
}

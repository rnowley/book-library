package demo.library.rest.data;

import demo.library.rest.Author;
import demo.library.rest.Book;
import demo.library.rest.Publisher;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfig.class)
public class JdbcBookRepositoryTest {

    @Autowired
    JdbcBookRepository bookRepository;

    @Test
    public void count() {
        assertEquals(3, bookRepository.count());
    }

    @Test
    public void findByISBN() {
        Book result = bookRepository.findByISBN("9780399255373");
        assertEquals("The Day the Crayons Quit", result.getTitle());
        assertEquals(1L, (long) result.getAuthor().size());
        assertEquals(2L, (long) result.getAuthor().get(0).getId());
        assertEquals("Daywalt", result.getAuthor().get(0).getLastName());
        assertEquals("Drew", result.getAuthor().get(0).getFirstName());
        assertEquals(2L, (long) result.getPublisher().getId());
        assertEquals("Philomel Books", result.getPublisher().getName());
    }

    @Test
    public void findOne() {
        Book result = bookRepository.findOne(1L);
        assertEquals("9780199686766", result.getISBN());
        assertEquals(1L, (long) result.getAuthor().get(0).getId());
        assertEquals("Joshi", result.getAuthor().get(0).getLastName());
        assertEquals("Pankaj S.", result.getAuthor().get(0).getFirstName());
        assertEquals(1L, (long) result.getPublisher().getId());
        assertEquals("Oxford University Press", result.getPublisher().getName());

        result = bookRepository.findOne(2L);
        assertEquals("9780399255373", result.getISBN());

        result = bookRepository.findOne(3L);
        assertEquals("9781101885376", result.getISBN());
    }

    @Test
    @Transactional
    public void save() {
        assertEquals(3, bookRepository.count());
        Calendar date = new GregorianCalendar();
        date.set(2010, 10, 28);
        Publisher publisher = new Publisher(4L, "No Starch Press");
        Author author = new Author(4L, "Kerrisk", "Michael");
        List<Author> authorList = new ArrayList<>();
        authorList.add(author);
        Book newBook = new Book(null, "The Linux Programming Interface",
                publisher, authorList, "9781593272203", date.getTime());
        Book saved = bookRepository.save(newBook);
        assertEquals(4, bookRepository.count());
        assertEquals(4, saved.getId().longValue());
    }

    @Test
    @Transactional
    public void delete() {
        assertEquals(3, bookRepository.count());
        assertNotNull(bookRepository.findOne(2));
        bookRepository.delete(2L);
        assertEquals(2, bookRepository.count());
        assertNotNull(bookRepository.findOne(1));
        assertNotNull(bookRepository.findOne(3));
    }
}

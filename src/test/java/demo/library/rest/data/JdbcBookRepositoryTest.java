package demo.library.rest.data;

import demo.library.rest.Book;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    }

    @Test
    public void findOne() {
        Book result = bookRepository.findOne(1L);
        assertEquals("9780199686766", result.getISBN());
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
        Book newBook = new Book(null, "The Linux Programming Interface",
                "No Starch Press", "Kerrisk, Michael", "9781593272203", date.getTime());
        Book saved = bookRepository.save(newBook);
        assertEquals(4, bookRepository.count());
        assertEquals(4, saved.getId().longValue());
    }
}

package demo.library.rest.data;

import demo.library.rest.Publisher;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author raymond
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = JdbcConfig.class)
public class JdbcPublisherRepositoryTest {

    @Autowired
    JdbcPublisherRepository publisherRepository;

    @Test
    public void count() {
        assertEquals(4, publisherRepository.count());
    }

    @Test
    public void findPublishers() {
        List<Publisher> result1 = publisherRepository.findPublishers(0, 2);
        assertEquals(2, result1.size());
        assertEquals("Oxford University Press", result1.get(0).getName());
        assertEquals("Philomel Books", result1.get(1).getName());

        List<Publisher> result2 = publisherRepository.findPublishers(2, 2);
        assertEquals(2, result2.size());
        assertEquals("Del Rey", result2.get(0).getName());
        assertEquals("No Starch Press", result2.get(1).getName());

        List<Publisher> result3 = publisherRepository.findPublishers(5, 2);
        assertEquals(0, result3.size());
    }
}

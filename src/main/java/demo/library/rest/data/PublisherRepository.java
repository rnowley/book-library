package demo.library.rest.data;

import demo.library.rest.Publisher;
import java.util.List;

/**
 *
 * @author raymond
 */
public interface PublisherRepository {

    long count();

    List<Publisher> findPublishers(long max, int count);

}

package demo.library.rest.data;

/**
 *
 * @author raymond
 */
public class AuthorNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private long authorId;

    public AuthorNotFoundException(long authorId) {
        this.authorId = authorId;
    }

    public long getAuthorId() {
        return authorId;
    }

}

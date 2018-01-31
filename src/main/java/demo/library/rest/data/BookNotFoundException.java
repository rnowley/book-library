package demo.library.rest.data;

public class BookNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private long bookId;

    public BookNotFoundException(long bookId) {
        this.bookId = bookId;
    }

    public long getBookId() {
        return bookId;
    }
}

package demo.library.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.simpleflatmapper.map.annotation.Key;

/*

{
	"title": "Lord of the Rings",
	"publisher": "Harper Collins",
	"author": "Tolkein, J.R.R",
	"isbn": "1234567890123",
	"datePublished": "2011-12-10"
}
 */
public class Book {

    @Key
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Publisher publisher;

    @NotNull
    private List<Author> author = new ArrayList<>();

    @NotNull
    @Size(min = 9, max = 13)
    private String ISBN;

    private Date datePublished;

    public Book() {
    }

    public Book(String title, Publisher publisher, List<Author> authors, String ISBN,
            Date datePublished) {
        this(null, title, publisher, authors, ISBN, datePublished);
    }

    public Book(Long id, String title, Publisher publisher, List<Author> authors,
            String ISBN, Date datePublished) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;

        for (Author item : authors) {
            this.author.add(item);
        }

        this.ISBN = ISBN;
        this.datePublished = datePublished;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the publisher
     */
    public Publisher getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the author
     */
    public List<Author> getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(List<Author> author) {
        this.author = author;
    }

    /**
     * @return the ISBN
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the ISBN to set
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return the datePublished
     */
    public Date getDatePublished() {
        return datePublished;
    }

    /**
     * @param datePublished the datePublished to set
     */
    public void setDatePublished(Date datePublished) {
        this.datePublished = datePublished;
    }

    @Override
    public boolean equals(Object that) {
        return EqualsBuilder.reflectionEquals(this, that, "title", "publisher",
                "author", "ISBN", "datePublished");
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, "title", "publisher",
                "author", "ISBN", "datePublished");
    }

}

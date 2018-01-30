package demo.library.rest;

import java.util.Date;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Book {

    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String publisher;

    @NotNull
    private String author;

    @NotNull
    @Size(min = 9, max = 13)
    private String ISBN;

    private Date datePublished;

    public Book(String title, String publisher, String author, String ISBN,
            Date datePublished) {
        this(null, title, publisher, author, ISBN, datePublished);
    }

    public Book(Long id, String title, String publisher, String author,
            String ISBN, Date datePublished) {
        this.id = id;
        this.title = title;
        this.publisher = publisher;
        this.author = author;
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
    public String getPublisher() {
        return publisher;
    }

    /**
     * @param publisher the publisher to set
     */
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
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

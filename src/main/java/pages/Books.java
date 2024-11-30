package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class Books {
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("title")
    private String title;
    @JsonProperty("subTitle")
    private String subTitle;
    @JsonProperty("author")
    private  String author;
    @JsonProperty("publish_date")
    private Date publish_date;
    @JsonProperty("publisher")
    private String publisher;
    @JsonProperty("pages")
    private int pages;
    @JsonProperty("description")
    private String description;
    @JsonProperty("website")
    private String website;

    public Books(String isbn) {
        this.isbn = isbn;
    }

    public Books(){}

    public Books(String isbn, String title, String subTitle, String author, Date publish_date, String publisher, int pages, String description, String website) {
        this.isbn = isbn;
        this.title = title;
        this.subTitle = subTitle;
        this.author = author;
        this.publish_date = publish_date;
        this.publisher = publisher;
        this.pages = pages;
        this.description = description;
        this.website = website;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}

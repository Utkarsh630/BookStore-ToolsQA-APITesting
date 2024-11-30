package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CollectionOfISBNs {
    @JsonProperty("isbn")
    private String isbn;

    public CollectionOfISBNs() {
    }

    public CollectionOfISBNs(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}

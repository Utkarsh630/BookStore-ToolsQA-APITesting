package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateBooksPage {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("isbn")
    private String isbn;

    public UpdateBooksPage() {
    }

    public UpdateBooksPage(String userId, String isbn) {
        this.userId = userId;
        this.isbn = isbn;
    }
}

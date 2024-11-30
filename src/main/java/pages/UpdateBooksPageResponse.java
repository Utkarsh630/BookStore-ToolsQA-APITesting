package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class UpdateBooksPageResponse {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("books")
    private List<Books> books;

    public UpdateBooksPageResponse() {
    }

    public UpdateBooksPageResponse(String userId, String username, List<Books> books) {
        this.userId = userId;
        this.username = username;
        this.books = books;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Books> getBooks() {
        return books;
    }

    public void setBooks(List<Books> books) {
        this.books = books;
    }
}

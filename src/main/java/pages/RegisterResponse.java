package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class RegisterResponse {
    @JsonProperty("userID")
    private String userID;

    @JsonProperty("username")
    private String username;

    @JsonProperty("books")
    private ArrayList<Books> books;

    public RegisterResponse(){}

    public RegisterResponse(String userID, String username, ArrayList<Books> books) {
        this.userID = userID;
        this.username = username;
        this.books = books;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserId(String userID) {
        this.userID = userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Books> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Books> books) {
        this.books = books;
    }
}

package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class ListBooks {
    @JsonProperty("books")
    private ArrayList<Books> books;

    public ListBooks(ArrayList<Books> books) {
        this.books = books;
    }

    public ListBooks() {
    }

    public ArrayList<Books> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Books> books) {
        this.books = books;
    }
}

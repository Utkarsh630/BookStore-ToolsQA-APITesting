package pages;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class AddBooksByISBNPage {
    @JsonProperty("userId")
    private String userId;

    @JsonProperty("collectionOfIsbns")
    private List<CollectionOfISBNs> collectionOfIsbns;

    public AddBooksByISBNPage(String userId, List<CollectionOfISBNs> collectionOfIsbns) {
        this.userId = userId;
        this.collectionOfIsbns = collectionOfIsbns;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<CollectionOfISBNs> getCollectionOfIsbns() {
        return collectionOfIsbns;
    }

    public void setCollectionOfIsbns(List<CollectionOfISBNs> collectionOfIsbns) {
        this.collectionOfIsbns = collectionOfIsbns;
    }
}

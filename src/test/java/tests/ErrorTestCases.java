package tests;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.testng.annotations.Test;
import pages.AddBooksByISBNPage;
import pages.CollectionOfISBNs;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.with;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

public class ErrorTestCases extends BaseTest {
    @Test(description = "Generate Token: Failure- Validate Response Status, Headers, and Response Body")
    public void generateTokenTest_Failure(){
        with()
                .body(userDetails)
                .post(ConfigLoader.getProperty("generateToken"))
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("token", nullValue())
                .body("expired", nullValue())
                .body("status", equalTo("Failed"))
                .body("result",equalTo("User authorization failed."));
    }
    @Test(priority = 5, description = "Failure: Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body")
    public void addListofBooksTest_Failure(){
        List<CollectionOfISBNs> collectionOfIsbns = new ArrayList<>();
        collectionOfIsbns.add(new CollectionOfISBNs("9781449325862"));
        collectionOfIsbns.add(new CollectionOfISBNs("9781449331818"));
        AddBooksByISBNPage addBooksByISBNPageRequestBody = new AddBooksByISBNPage("22ada42d-61e5-4821-b138-c8c502ce152b",collectionOfIsbns);

        with()
                .body(addBooksByISBNPageRequestBody)
                .post(ConfigLoader.getProperty("addBook"))
                .then()
                .statusCode(401)
                .body("code",equalTo("1200"))
                .body("message",equalTo("User not authorized!"));
    }
}

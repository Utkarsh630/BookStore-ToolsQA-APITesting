package tests;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class E2ETestCases extends BaseTest {
    String username = "Devashe";
    String password = "Abcdef@1234";
    String token="";
    String userID="";
    @Test(priority = 0, description = "User Registration API with authentication: Validate Response status, Headers, and Response Body")
    public void registerUserTest(){
       userID = given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post(ConfigLoader.getProperty("registerUser"))
                .then()
                .log()
                .all()
                .statusCode(201)
                .body("userID",notNullValue())
                .body("username",equalTo(username))
                .body("books",equalTo(new ArrayList<>()))
                .extract().path("userID");

    }
    @Test(description = "check for existing user registration")
    public void userAlreadyExistsTest(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post(ConfigLoader.getProperty("registerUser"))
                .then()
                .log()
                .all()
                .statusCode(406)
                .contentType(ContentType.JSON)
                .body("code",equalTo("1204"))
                .body("message",equalTo("User exists!"));
        }
    @Test(priority = 1,description = "Generate Token: Success- Validate Response Status, Headers, and Response Body")
    public void generateTokenTest_Success(){
         token= given()
                    .contentType(ContentType.JSON)
                    .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                    .when()
                    .log()
                    .all()
                    .post(ConfigLoader.getProperty("generateToken"))
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("token", notNullValue())
                    .body("expires", notNullValue())
                    .body("status", equalTo("Success"))
                    .body("result",equalTo("User authorized successfully."))
                    .extract().path("token");
        }
    @Test(  description = "Generate Token: Failure- Validate Response Status, Headers, and Response Body")
    public void generateTokenTest_Failure(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post(ConfigLoader.getProperty("generateToken"))
                .then()
                .log()
                .all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("token", nullValue())
                .body("expired", nullValue())
                .body("status", equalTo("Failed"))
                .body("result",equalTo("User authorization failed."));
    }

    @Test(description = "Get All Books: Validate Response Status, Headers, and Response Body")
    public void getAllBooksTest(){
        given()
                .contentType(ContentType.JSON)
                .when()
                .log()
                .all()
                .get(ConfigLoader.getProperty("getBooks"))
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("books",notNullValue());

    }

    @Test(description = "Get Book by ISBN: Validate Response Status, Headers, and Response Body" )
    public void getBookByISBNTest(){
        given()
                .contentType(ContentType.JSON)
                .queryParam("ISBN","9781449325862")
                .when()
                .log()
                .all()
                .get(ConfigLoader.getProperty("getBook"))
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test(description = "Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = {"registerUserTest","generateTokenTest_Success"})
    public void addListofBooksTest(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body("{\n" +
                        "  \"userId\": \""+userID+"\",\n" +
                        "  \"collectionOfIsbns\": [\n" +
                        "    {\n" +
                        "      \"isbn\": \"9781449325862\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .log().all()
                .when()
                .post(ConfigLoader.getProperty("addBook"))
                .then()
                .log()
                .all()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("books.isbn",hasItem("9781449325862"));
    }

    @Test(description = "Failure: Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = {"registerUserTest","generateTokenTest_Success"})
    public void addListofBooksTest_Failure(){
        given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "  \"userId\": \""+userID+"\",\n" +
                        "  \"collectionOfIsbns\": [\n" +
                        "    {\n" +
                        "      \"isbn\": \"9781449325862\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .log().all()
                .when()
                .post(ConfigLoader.getProperty("addBook"))
                .then()
                .log()
                .all()
                .statusCode(401)
                .contentType(ContentType.JSON)
                .body("code",equalTo("1200"))
                .body("message",equalTo("User not authorized!"));
    }

    @Test(description = "update books list of user by userId and ISBN: Validate Response Status, Headers, and Response Body")
    public void updateBookListByISBN(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImRldmFzciIsInBhc3N3b3JkIjoiQWJjZGVmQDIyIiwiaWF0IjoxNzMyNDUyMzA4fQ.ePysNSFWYrteMgxe6iiR5ql4I80zpw1OV1R08dOiTvY")
                .body("{\n" +
                        "  \"userId\": \"22ada42d-61e5-4821-b138-c8c502ce152b\",\n" +
                        "  \"isbn\": \"9781449325862\"\n" +
                        "}")
                .log()
                .all()
                .when()

                .put(ConfigLoader.getProperty("addBook")+"9781449365035")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("userId",equalTo("22ada42d-61e5-4821-b138-c8c502ce152b"))
                .body("username",equalTo("devasr"))
                .body("books.isbn[0]",equalTo("9781449325862"));
    }

    @Test(description = "Delete book from collection by ISBN: Validate Response Status, Headers, and Response Body")
    public void deleteBookListByISBN(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImRldmFzciIsInBhc3N3b3JkIjoiQWJjZGVmQDIyIiwiaWF0IjoxNzMyNDUzNDkyfQ.LhFL_eYLZekLkvOCi8rbsJg-JjY80MY4mJC34poqg7s")
                .body("{\n" +
                        "  \"isbn\": \"9781449325862\",\n" +
                        "  \"userId\": \"22ada42d-61e5-4821-b138-c8c502ce152b\"\n" +
                        "}")
                .log()
                .all()
                .when()
                .delete(ConfigLoader.getProperty("deleteBook"))
                .then()
                .log()
                .all()
                .statusCode(204);
    }

}

package tests;

import base.BaseTest;
import config.ConfigLoader;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class E2ETestCases extends BaseTest {
    String username = faker.name().username();
    String password = faker.internet().password(8,16,true,true);
    String token="";
    String userID="";
    @Test(description = "User Registration API with authentication: Validate Response status, Headers, and Response Body")
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
    @Test(priority = 1, description = "check for existing user registration")
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
    @Test(priority = 2,description = "Generate Token: Success- Validate Response Status, Headers, and Response Body")
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
    @Test(priority = 2, description = "Generate Token: Failure- Validate Response Status, Headers, and Response Body")
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
                .statusCode(400)
                .contentType(ContentType.JSON)
                .body("token", nullValue())
                .body("expired", nullValue())
                .body("status", equalTo("Failed"))
                .body("result",equalTo("User authorization failed."));
    }

    @Test(priority = 3, description = "Get All Books: Validate Response Status, Headers, and Response Body")
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

    @Test(priority = 4, description = "Get Book by ISBN: Validate Response Status, Headers, and Response Body" )
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

    @Test(priority = 5, description = "Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = {"registerUserTest","generateTokenTest_Success"})
    public void addListOfBooksTest(){
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

    @Test(priority = 5, description = "Failure: Add list of Books into user's book collection: Validate Response Status, Headers, and Response Body", dependsOnMethods = {"registerUserTest","generateTokenTest_Success"})
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

    @Test(priority = 6, description = "update books list of user by userId and ISBN: Validate Response Status, Headers, and Response Body")
    public void updateBookListByISBN(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImRldmFzciIsInBhc3N3b3JkIjoiQWJjZGVmQDIyIiwiaWF0IjoxNzMyNDUyMzA4fQ.ePysNSFWYrteMgxe6iiR5ql4I80zpw1OV1R08dOiTvY")
                .body("""
                        {
                          "userId": "22ada42d-61e5-4821-b138-c8c502ce152b",
                          "isbn": "9781449325862"
                        }""")
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

    @Test(priority = 7, description = "Delete book from collection by ISBN: Validate Response Status, Headers, and Response Body")
    public void deleteBookListByISBN(){
        given()
                .contentType(ContentType.JSON)
                .header("Authorization","Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyTmFtZSI6ImRldmFzciIsInBhc3N3b3JkIjoiQWJjZGVmQDIyIiwiaWF0IjoxNzMyNDUzNDkyfQ.LhFL_eYLZekLkvOCi8rbsJg-JjY80MY4mJC34poqg7s")
                .body("""
                        {
                          "isbn": "9781449325862",
                          "userId": "22ada42d-61e5-4821-b138-c8c502ce152b"
                        }""")
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

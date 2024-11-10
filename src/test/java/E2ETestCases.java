import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class E2ETestCases {
    String username = "Dev";
    String password = "Abcdef@1234";

    @BeforeClass
    public void setup(){
        baseURI = "https://bookstore.toolsqa.com/";
    }

    @Test(description = "User Registration API with authentication: Validate Response status, Headers, and Response Body")
    public void registerUserTest(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post("Account/v1/User")
                .then()
                .log()
                .all()
                .statusCode(201)
                .body("userID",notNullValue())
                .body("username",equalTo(username))
                .body("books",equalTo(new ArrayList<>()));

    }
    @Test(description = "check for existing user registration")
    public void userAlreadyExistsTest(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post("Account/v1/User")
                .then()
                .log()
                .all()
                .statusCode(406)
                .contentType(ContentType.JSON)
                .body("code",equalTo("1204"))
                .body("message",equalTo("User exists!"));
        }
    @Test(description = "Generate Token: Success- Validate Response Status, Headers, and Response Body")
    public void generateTokenTest_Success(){
            given()
                    .contentType(ContentType.JSON)
                    .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                    .when()
                    .log()
                    .all()
                    .post("Account/v1/GenerateToken")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .body("token", notNullValue())
                    .body("expires", notNullValue())
                    .body("status", equalTo("Success"))
                    .body("result",equalTo("User authorized successfully."));
        }
    @Test(description = "Generate Token: Failure- Validate Response Status, Headers, and Response Body")
    public void generateTokenTest_Failure(){
        given()
                .contentType(ContentType.JSON)
                .body("{\"userName\":\"" +username+ "\", \"password\":\""+password+ "\"}")
                .when()
                .log()
                .all()
                .post("Account/v1/GenerateToken")
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
                .get("BookStore/v1/Books")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("books",notNullValue());

    }

    @Test(description = "Get Book by ISBN: Validate Response Status, Headers, and Response Body")
    public void getBookByISBNTest(){
        given()
                .contentType(ContentType.JSON)
                .queryParam("ISBN","9781449325862")
                .when()
                .log()
                .all()
                .get("BookStore/v1/Book")
                .then()
                .log()
                .all()
                .statusCode(200);
    }



}

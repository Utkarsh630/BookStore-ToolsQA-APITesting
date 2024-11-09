import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


public class E2ETestCases {
    String username = "Charl";
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


}

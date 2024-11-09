import io.restassured.http.ContentType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class E2ETestCases {
    String username = "Charles";
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
                .statusCode(201);
    }

    @Test
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
                .statusCode(406);
        }
}

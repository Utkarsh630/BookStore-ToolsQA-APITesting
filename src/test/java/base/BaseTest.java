package base;

import com.github.javafaker.Faker;
import config.ConfigLoader;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import pages.UserDetails;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected Faker faker = new Faker();
    protected String username = faker.name().username();
    protected String password = faker.internet().password(8,16,true,true,true);
    protected UserDetails userDetails = new UserDetails(username,password);
    protected String token="";
    protected String userID="";
    protected String Isbn;
    protected String Isbn2;
    @BeforeClass
    public void setup(){
        logger.debug("Setting up Base URI!");
        baseURI = ConfigLoader.getProperty("baseURI");
        RestAssured.requestSpecification= new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .log(LogDetail.ALL)
                .build();

        RestAssured.responseSpecification= new ResponseSpecBuilder()
               .expectContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

    }

}

package base;

import config.ConfigLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.baseURI;

public class BaseTest {
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    @BeforeClass
    public void setup(){
        logger.debug("Setting up Base URI!");
        baseURI = ConfigLoader.getProperty("baseURI");
    }

}

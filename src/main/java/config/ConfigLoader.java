package config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {

    private static final Properties properties = new Properties();
    private static final String filePath = System.getProperty("user.dir")+"/src/main/resources/Configuration.properties";
    private static final Logger logger = LogManager.getLogger(ConfigLoader.class);

    static {
        logger.info("Trying to read Configuration file.");
        try(FileInputStream fis = new FileInputStream(filePath)){
            properties.load(fis);
            logger.info("Configuration File loaded successfully.");
        }
        catch (IOException e){
            logger.error("Failed to load configuration file!",e);
            throw new RuntimeException("Error to load configuration file!",e);
        }
    }
    public static String getProperty(String key){
        String value = properties.getProperty(key);
        if(value!=null){
            logger.debug("Property loaded: '{}' = '{}'", key, value);
            return value;
        }else{
            logger.warn("Property with key '{}' is not found in Configuration.properties file!", key);
            throw new RuntimeException( key + ":- not specified in the Configuration.properties file.");
        }
    }

}

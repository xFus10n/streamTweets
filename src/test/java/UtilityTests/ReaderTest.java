package UtilityTests;

import com.xfus10n.twitter.propertiesReader.Reader;
import com.xfus10n.twitter.propertiesReader.TwitterProperties;
import org.junit.Test;

import java.util.Properties;

import static com.xfus10n.twitter.propertiesReader.TwitterProperties.*;
import static org.junit.Assert.assertEquals;

public class ReaderTest {
    @Test
    public void testReader(){
        Properties properties = Reader.readProperties("src/test/resources/twitter.properties");
        String accessTokenSecretProp = properties.getProperty(accessTokenSecret.name());
        String consumerKeyProp = properties.getProperty(TwitterProperties.consumerKey.name());
        String accessTokenProp = properties.getProperty(accessToken.name());
        String consumerSecretProp = properties.getProperty(consumerSecret.name());

        assertEquals("thisIsTokenSecret",accessTokenSecretProp);
        assertEquals("thisIsConsumerKey",consumerKeyProp);
        assertEquals("thisIsAccessToken",accessTokenProp);
        assertEquals("thisIsConsumerSecret",consumerSecretProp);
    }

}

package UtilityTests;

import com.xfus10n.twitter.propertiesReader.Reader;
import com.xfus10n.twitter.domainz.TwitterProperties;
import org.junit.Test;

import java.util.Properties;

import static com.xfus10n.twitter.domainz.TwitterProperties.*;
import static org.junit.Assert.assertEquals;

public class ReaderTest {
    @Test
    public void testReaderTweeter(){
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

    @Test
    public void testReaderKafkaProducer() {
        Properties properties = Reader.readProperties("src/test/resources/kProducer.properties");
        String keySerializer = properties.getProperty("key.serializer");
        String brokerList = properties.getProperty("metadata.broker.list");
        String retries = properties.getProperty("retries");
        assertEquals("0",retries);
        assertEquals("localhost:9092", brokerList);
        assertEquals("org.apache.kafka.common.serialization.StringSerializer", keySerializer);
        assertEquals(9,properties.size());
    }
}

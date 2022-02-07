package UtilityTests;

import com.xfus10n.twitter.propertiesReader.Reader;
import org.junit.Test;

import java.util.Properties;

public class ReaderTest {
    @Test
    public void testReader(){
        Properties properties = Reader.readProperties("src/test/resources/twitter.properties");
        properties.forEach((property, value)->System.out.println(property + "=" + value));
    }

}

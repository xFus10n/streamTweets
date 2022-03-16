package UtilityTests;

import com.xfus10n.twitter.Utilz.CLI;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import scala.util.parsing.combinator.testing.Str;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class CLITest {
    @Test
    public void testCLIoptions() throws ParseException {
        String[] args = {"-t","src/test/resources/twitter.properties"};
        CLI cli = new CLI();
        CommandLine commandLine = cli.parser(args);
        assertEquals("src/test/resources/twitter.properties", commandLine.getOptionValue("t"));
        assertFalse(commandLine.hasOption("p"));
        assertTrue(commandLine.hasOption("t"));
    }

    @Test
    public void testCLIoptionsFail() {
        String[] args = {"-p","src/test/resources/kProducer.properties"};
        CLI cli = new CLI();
        ParseException exception = assertThrows(ParseException.class, () -> cli.parser(args));
        assertEquals("Failed to parse arguments", exception.getMessage());
    }

    @Test
    public void readProxyTest() throws ParseException, URISyntaxException {
        String[] args = {"-f","http://some.host.com:8080","-t","src/test/resources/twitter.properties"};
        CLI cli = new CLI();
        CommandLine commandLine = cli.parser(args);
        String proxy = commandLine.getOptionValue("f");
        URI uri = new URI(proxy);
        assertEquals(8080, uri.getPort());
        assertEquals("some.host.com", uri.getHost());
    }

    @Test
    public void testReadStringToArray() throws ParseException {
        String keywords = " winter , spring , summer , autumn ";
        String[] args = {"-t","src/test/resources/twitter.properties", "-k", keywords};
        CLI cli = new CLI();
        CommandLine commandLine = cli.parser(args);
        assertTrue(commandLine.hasOption("k"));
        assertEquals(keywords, commandLine.getOptionValue("k"));
        String output = commandLine.getOptionValue("k");
        String[] keywordsArr = output.replaceAll(" ", "").split(",");
        System.out.println(String.join("_", keywordsArr));
    }
}

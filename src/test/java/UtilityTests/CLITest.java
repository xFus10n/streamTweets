package UtilityTests;

import com.xfus10n.twitter.Utilz.CLI;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.*;

public class CLITest {
    @Test
    public void testCLIoptions() throws ParseException {
        String[] args = {"-t","src/test/resources/twitter.properties"};
        CLI cli = new CLI();
        CommandLine commandLine = cli.CLIparser(args);
        assertEquals("src/test/resources/twitter.properties", commandLine.getOptionValue("t"));
        assertFalse(commandLine.hasOption("p"));
        assertTrue(commandLine.hasOption("t"));
    }

    @Test
    public void testCLIoptionsFail() {
        String[] args = {"-p","src/test/resources/kProducer.properties"};
        CLI cli = new CLI();
        ParseException exception = assertThrows(ParseException.class, () -> cli.CLIparser(args));
        assertEquals("Failed to parse arguments", exception.getMessage());
    }

    @Test
    public void readProxyTest() throws ParseException, URISyntaxException {
        String[] args = {"-f","http://proxy.lvrix.atrema.deloitte.com:3128","-t","src/test/resources/twitter.properties"};
        CLI cli = new CLI();
        CommandLine commandLine = cli.CLIparser(args);
        String proxy = commandLine.getOptionValue("f");
        URI uri = new URI(proxy);
        assertEquals(3128, uri.getPort());
        assertEquals("proxy.lvrix.atrema.deloitte.com", uri.getHost());
    }
}

package UtilityTests;

import com.xfus10n.twitter.Utilz.CLI;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
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
}

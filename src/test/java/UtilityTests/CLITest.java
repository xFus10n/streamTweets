package UtilityTests;

import com.xfus10n.twitter.Utilz.CLI;
import com.xfus10n.twitter.domainz.TwitterProperties;
import com.xfus10n.twitter.propertiesReader.Reader;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import scala.util.parsing.combinator.testing.Str;

import java.util.Properties;

import static com.xfus10n.twitter.domainz.TwitterProperties.*;
import static org.junit.Assert.*;

public class CLITest {
    @Test
    public void testCLIoptions() {
        String[] args = {"-t","src/test/resources/twitter.properties"};
        CLI cli = new CLI();
        CommandLine commandLine = cli.CLIparser(args);
        assertEquals("src/test/resources/twitter.properties", commandLine.getOptionValue("t"));
        assertFalse(commandLine.hasOption("p"));
        assertTrue(commandLine.hasOption("t"));
    }
}

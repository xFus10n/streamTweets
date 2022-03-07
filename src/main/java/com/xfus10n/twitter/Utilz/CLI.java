package com.xfus10n.twitter.Utilz;

import org.apache.commons.cli.*;

public class CLI {
    private Options createOptions(){
        Options options = new Options();
        options.addRequiredOption("t","tweeter", true, "path to file -> tokens for tweeter streaming (mandatory)");
        options.addOption("p","producer", true, "path to file -> properties for kafka producer (optional)");
        options.addOption("d","topic", true, "topic name, when omit = default (optional)");
        options.addOption("f","proxy", true, "set proxy url (optional)");
        options.addOption("k","keywords", true, "keywords delimited by comma (optional)");
        return options;
    }

    public CommandLine parser(String[] args) throws ParseException {
        Options options = createOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            // parse the command line arguments
            return parser.parse(options, args);
        }
        catch (ParseException exp) {
            // oops, something went wrong
            System.err.println("Parsing failed.  Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("tweeter streamer", options);
            throw new ParseException("Failed to parse arguments");
        }
    }
}

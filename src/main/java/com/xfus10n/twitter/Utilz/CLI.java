package com.xfus10n.twitter.Utilz;

import org.apache.commons.cli.*;

public class CLI {
    private Options createOptions(){
        Options options = new Options();
        options.addRequiredOption("t","tweeter", true, "path to file -> tokens for tweeter streaming");
        options.addOption("p","producer", true, "path to file -> properties for kafka producer");
        return options;
    }

    public CommandLine CLIparser(String[] args){
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
        }
        return null;
    }
}

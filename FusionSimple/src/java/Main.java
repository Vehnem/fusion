
import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Core data
        int    maxQs = 100;
        String inDir = null;
        String ouDir = null;
        String laFil = null;
        List<String> laOrd = new ArrayList<String>();

        // Cli

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        options.addOption("i", "input", true, "input folder");
        options.addOption("o", "out", true, "output folder");
        options.addOption("l", "languages", true, "languages file");
        options.addOption("h", "help", false, "show this help");
        options.addOption("q", "q", true, "max ID");

//        Option option = new Option("l", "language order list");
//        option.setArgs(Option.UNLIMITED_VALUES);
//        options.addOption(option);
//        String[] laLis = commandLine.getOptionValues("l");

        CommandLine commandLine = null;
        CommandLineParser parser = new BasicParser();
        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("h")) {
                formatter.printHelp("SimpleFusion", options);
                System.exit(0);
            }

            if (commandLine.hasOption("i")&&commandLine.hasOption("o")&&commandLine.hasOption("l")) {
                inDir = commandLine.getOptionValue("i");
                ouDir = commandLine.getOptionValue("o");
                laFil = commandLine.getOptionValue("l");
                if( commandLine.hasOption("q")) {
                    maxQs = Integer.parseInt(commandLine.getOptionValue("q"));
                }
            }
            else {
                formatter.printHelp("SimpleFusion", options);
                System.exit(0);
            }

        } catch (ParseException e) {
            formatter.printHelp("SimpleFusion", options);
            System.exit(1);
        }

        try {
            FileReader fr = new FileReader("dsorder");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                laOrd.add(line);
            }

        } catch (FileNotFoundException fne) {// TODO
        } catch (IOException ioe ) {// TODO
        }

        new Fusion(laOrd, inDir, maxQs, ouDir);
        // Fusion part
    }
}

package outdated;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class MainFilter {

    public static void main(String[] args) {

        // Core data
        int    maxQs = 0;
        String inDir = null;
        String out = null;
        String prefFile = null;
        String ids = null;
        boolean gz = false;
        List<String> pref = new ArrayList<String>();

        // Cli

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        options.addOption("i", "input", true, "input folder");
        options.addOption("p", "languages", true, "preference file");
        options.addOption("h", "help", false, "show this help");
        options.addOption("q","ids",true,"wkd id list");

//        Option option = new Option("l", "language order list");
//        option.setArgs(Option.UNLIMITED_VALUES);
//        options.addOption(option);
//        String[] laLis = commandLine.getOptionValues("l");

        CommandLine commandLine = null;
        CommandLineParser parser = new BasicParser();
        try {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("h")) {
                formatter.printHelp("SimpleFusion def.Counter", options);
                System.exit(0);
            }
            if (commandLine.hasOption("i")&&commandLine.hasOption("p")&&commandLine.hasOption("q")) {
                inDir = commandLine.getOptionValue("i");
                prefFile = commandLine.getOptionValue("p");
                ids = commandLine.getOptionValue("q");
            }
            else {
                formatter.printHelp("SimpleFusion def.Counter", options);
                System.exit(0);
            }

        } catch (ParseException e) {
            formatter.printHelp("SimpleFusion def.Counter", options);
            System.exit(1);
        }

        // TODO File or multi value
        try {
            FileReader fr = new FileReader(prefFile);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                pref.add(line);
            }

        } catch (FileNotFoundException fne) {
            Logger.getGlobal().warning("using no pref file");
            pref.add(commandLine.getOptionValue("p"));
        } catch (IOException ioe ) { ioe.printStackTrace();
        }

        // PREFERENCE
        String pref_out = "";
        for (int s = 0; s < pref.size() ; s++) {
            if (s == pref.size() - 1) {
                pref_out += pref.get(s);
            } else {
                pref_out += pref.get(s) + " > ";
            }
        }
        System.out.println("HDT FUSION COUNTER");
        System.out.println("==================");
        System.out.println("preference: "+pref_out);

        // READ ids
        List<String> wkdUris = new ArrayList<>();
        try {
            FileReader fr = new FileReader(ids);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                wkdUris.add(line);
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe ) {
            ioe.printStackTrace();
        }

        // def.Fusion part
        long start = System.currentTimeMillis();
        new FilterDataset(pref,wkdUris, inDir);
        long end = System.currentTimeMillis();
        Logger.getGlobal().info("Took : " + ((end - start) / 1000)+" seconds");


    }
}




import com.hp.hpl.jena.rdf.model.*;
import com.sun.org.apache.xpath.internal.SourceTree;
import org.apache.commons.cli.*;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import javax.jws.WebParam;
import java.io.*;
import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // Core data
        int    maxQs = 0;
        String inDir = null;
        String ouDir = null;
        String laFil = null;
        boolean gz = false;
        List<String> laOrd = new ArrayList<String>();

        // Cli

        HelpFormatter formatter = new HelpFormatter();

        Options options = new Options();
        options.addOption("i", "input", true, "input folder");
        options.addOption("o", "out", true, "output folder");
        options.addOption("l", "languages", true, "languages file");
        options.addOption("h", "help", false, "show this help");
        options.addOption("q", "q", true, "max ID");
        options.addOption("gz","gzip",false,"gzip output files");

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
                if( commandLine.hasOption("gz")) {
                    gz = true;
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
            FileReader fr = new FileReader(laFil);
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                laOrd.add(line);
            }

        } catch (FileNotFoundException fne) {// TODO
        } catch (IOException ioe ) {// TODO
        }

        String pref = "";
        for (int s = 0; s < laOrd.size() ; s++) {
            if (s == laOrd.size() - 1) {
                pref += laOrd.get(s);
            } else {
                pref += laOrd.get(s) + " > ";
            }
        }
        System.out.println("preference "+pref);

        // Fusion part
        new Fusion(laOrd, inDir, maxQs, ouDir, gz);

    }
}



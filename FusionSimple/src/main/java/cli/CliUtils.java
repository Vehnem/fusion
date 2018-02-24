package cli;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CliUtils {

    private static Options options = new Options();
    static {
        options.addOption("i", "infolder", true, "input folder");
        options.addOption("o", "outfile", true, "output file");
        options.addOption("p", "pref-lang", true, "preference file");
        options.addOption("h", "help", false, "show this help");
        options.addOption("gz","gzip",false,"gzip output files");
        options.addOption("q","qmax",true,"wkd id list");
        options.addOption(null,"only-func",false,"wkd id list");
        options.addOption(null,"base-id",true,"suffix of resource Uris");
        options.addOption(null,"file-name",true,"file name of output");
    }

    public static CommandLine getCommnadLine(String[] args ) {
        CommandLineParser clp = new BasicParser();
        CommandLine cl = null;
        try{
            cl = clp.parse(options, args);
        } catch(ParseException pe) {
            printHelp();
        }
        return  cl;
    }


    public static void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("SimpleFusion", options);
        System.exit(0);
    }



    public static List<String> getPreference(String prefFile) {

        List<String> pref = new ArrayList<String>();

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
            pref.add(prefFile);
        } catch (IOException ioe ) { ioe.printStackTrace();
        }

        return pref;
    }

    public static String makePrefStr(List<String> pref) {
        // PREFERENCE
        String pref_out = "";
        for (int s = 0; s < pref.size(); s++) {
            if (s == pref.size() - 1) {
                pref_out += pref.get(s);
            } else {
                pref_out += pref.get(s) + " > ";
            }
        }
        return pref_out;
    }


//
//    // CREATE DIRs
//    File file = new File(out);
//        if(!file.getParentFile().exists()) {
//        if (!file.getParentFile().mkdirs()) {
//            Logger.getGlobal().warning("Failed to create directory " + file.getParent());
//        } else {
//            Logger.getGlobal().info("Created directory "+ file.getParent());
//        }
//    }

}

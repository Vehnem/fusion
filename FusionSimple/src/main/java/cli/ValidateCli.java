package cli;

import org.apache.commons.cli.*;
import fusion.*;
import org.apache.hadoop.mapreduce.tools.CLI;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ValidateCli {

    public static void main(String[] args) {

        // Core data
        long qMax = 0;
        boolean gz = false;
        String inDir = null;
        String outDir = null;
        String prefFile = null;
        String baseUri= "http://wikidata.dbpedia.org/resource/Q";
        String fileName = null;
        boolean onlyfunc = false;
        List<String> pref = new ArrayList<String>();

        // Cli

//        Option option = new Option("l", "language order list");
//        option.setArgs(Option.UNLIMITED_VALUES);
//        options.addOption(option);
//        String[] laLis = commandLine.getOptionValues("l");

        CommandLine commandLine = CliUtils.getCommnadLine(args);

        if (commandLine.hasOption("i") && commandLine.hasOption("o") && commandLine.hasOption("p") && commandLine.hasOption("q")) {
            inDir = commandLine.getOptionValue("i");
            outDir = commandLine.getOptionValue("o");
            prefFile = commandLine.getOptionValue("p");
            qMax = Long.parseLong(commandLine.getOptionValue("q"));
            if (commandLine.hasOption("gz")) gz = true;
            if (commandLine.hasOption("base-id")) baseUri= commandLine.getOptionValue("base-id");
            if (commandLine.hasOption("file-name")) fileName= commandLine.getOptionValue("file-name");
            if (commandLine.hasOption("only-func")) onlyfunc=true;
        } else {
            CliUtils.printHelp();
        }

        pref = CliUtils.getPreference(prefFile);

        System.out.println("HDT FUSION");
        System.out.println("==========");
        System.out.println("preference: ");

        System.out.println(
                String.join(">",pref)+"\n"+
                inDir+"\n"+
                outDir+"\n"+
                qMax+"\n"
        );

        File outputDirectory = new File( outDir);
        if (false == outputDirectory.exists() && false == outputDirectory.mkdirs()) {
            System.err.println("Was not able to create directories: " + outputDirectory.getName());
        }

        fileName = ( null != fileName ) ? fileName : String.join("_",pref)+".nt";

        long start = System.currentTimeMillis();
        new Fusion(pref,qMax, inDir, outDir+"/"+fileName, gz, baseUri,onlyfunc);
        long end = System.currentTimeMillis();
        Logger.getGlobal().info("Took : " + ((end - start) / 1000)+" seconds");


    }
}

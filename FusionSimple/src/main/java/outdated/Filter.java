package outdated;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Filter {

    static FileWriter fw = null;
    static {
        try {
            fw = new FileWriter("fr_small");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static List<String> properties =  Arrays.asList(
            "http://dbpedia.org/ontology/acceleration",
            "http://dbpedia.org/ontology/averageAnnualGeneration",
            "http://dbpedia.org/ontology/birthDate",
            "http://dbpedia.org/ontology/birthYear",
            "http://dbpedia.org/ontology/co2Emission",
            "http://dbpedia.org/ontology/deathDate",
            "http://dbpedia.org/ontology/deathYear",
            "http://dbpedia.org/ontology/diameter",
            "http://dbpedia.org/ontology/displacement",
            "http://dbpedia.org/ontology/foalDate",
            "http://dbpedia.org/ontology/fuelCapacity",
            "http://dbpedia.org/ontology/fuelConsumption",
            "http://dbpedia.org/ontology/height",
            "http://dbpedia.org/ontology/installedCapacity",
            "http://dbpedia.org/ontology/latestReleaseDate",
            "http://dbpedia.org/ontology/length",
            "http://dbpedia.org/ontology/marketCapitalisation",
            "http://dbpedia.org/ontology/netIncome",
            "http://dbpedia.org/ontology/operatingIncome",
            "http://dbpedia.org/ontology/populationTotal",
            "http://dbpedia.org/ontology/powerOutput",
            "http://dbpedia.org/ontology/redline",
            "http://dbpedia.org/ontology/restingDate",
            "http://dbpedia.org/ontology/retirementDate",
            "http://dbpedia.org/ontology/topSpeed",
            "http://dbpedia.org/ontology/torqueOutput",
            "http://dbpedia.org/ontology/weight",
            "http://dbpedia.org/ontology/wheelbase",
            "http://dbpedia.org/ontology/width",
            "http://dbpedia.org/ontology/zipCode",
            RDFS.label.getURI(),
            RDF.type.getURI()
    );

    static final String inDir = "/home/vehnem/workspace/fusion_data/new/";
    public static void main(String[] args) throws IOException {

//        HDT hdt = HDTManager.loadIndexedHDT(inDir + "fr" + "/wkd_uris_selection.gz.hdt", null);
        String last = "";
        HashSet<String> types = new HashSet<String>();

        int c = 0;
        try {
            FileReader fr = new FileReader(inDir + "fr" + "/wkd_uris_selection");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while ((line = br.readLine()) != null) {
                addToFile(line);
                c++;
                //if(c > 100) break;
            }

            fw.close();
            br.close();
        } catch (FileNotFoundException fne) {// TODO
            fne.printStackTrace();
        } catch (IOException ioe) {// TODO
            ioe.printStackTrace();
        }

    }
    public static void addToFile(String line) throws IOException{

        String triple[] = line.split(" ");
        String subject = triple[0].replaceAll("[<>]","");
        String predicate = triple[1].replaceAll("[<>]","");
//        System.out.println(subject+" "+predicate);
                if(subject.startsWith("http://wikidata.dbpedia.org/resource/Q")) {
                    if(properties.contains(predicate))  {
                        fw.write(line+"\n");
                        fw.flush();
                    }
                }
    }
}

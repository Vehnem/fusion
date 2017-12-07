import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.apache.commons.collections.map.HashedMap;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class Fusion {

    protected static FileWriter fw = null;
    protected static String inDir = null;

    protected static boolean gz = false;
    protected static List<String> prefOrd = new ArrayList<String>();


    protected static List<String> wkdUris = null;
    protected static Map<String, HDT> l = null;

    public Fusion(List<String> prefOrd, List wkdUris, String inDir, String out, boolean gz) {
        this.inDir = inDir;
        this.prefOrd = prefOrd;
        this.gz = gz;
        this.wkdUris = wkdUris;

        try {
            fw = new FileWriter(out);
        } catch (Exception e) {
            e.printStackTrace();
        }
        run();

    }

    public static void run () {
        l = getMap();
        for ( String wkdId : wkdUris) {//(String n : wkdUris) {
            HashSet<String> entityTriple= fuseEntity(wkdId);
             writeEntityData(entityTriple, wkdId);
        }
    }

    public static HashSet<String> fuseEntity(String wkdId) {
        String wkdUri = "http://wikidata.dbpedia.org/resource/Q"+wkdId;
        HashSet<String> lookup = new HashSet<String>(properties);
        HashSet<String> result = new HashSet<String>();
        for (String o : prefOrd) {
            HDT hdt = l.get(o);
            try {
                IteratorTripleString it = hdt.search(wkdUri, "", "");
                while (it.hasNext()) {
                    TripleString ts = it.next();
                    String p = ts.getPredicate().toString();
                    if( lookup.contains(p) ) {
                        result.add(new TripleStringHelper(ts).asNtriple().toString());
                        lookup.remove(p);
                    } else if (p.equals(RDFS.label.getURI())) {
                        result.add(new TripleStringHelper(ts).asNtriple().toString());
                    } else if (p.equals(RDF.type.getURI())) {
                        result.add(new TripleStringHelper(ts).asNtriple().toString());
                    }
                }
            } catch (NotFoundException nfe) {
//                Logger.getLogger("NOTFOUND").info(wkdUri+" not found in "+o);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        return result;
    }

    private static long max = 0;

    public static void writeEntityData(HashSet<String> tripleSet, String Uri) {
        Objects.requireNonNull(tripleSet);
        max++;
        try {
            for (String triple: tripleSet) {
                fw.write(triple);
            }
            fw.flush();
        } catch (IOException ioe) {
            Logger.getGlobal().warning("cant write triple");
            ioe.printStackTrace();
        }
        if(max % 10000 == 0) {
            Logger.getGlobal().info("Entites done: "+max);
        }
    }

    public static Map getMap() {

        Map<String, HDT> langToHDT = null;

        if (langToHDT == null) {
            langToHDT = new HashedMap();
            for (String lang : prefOrd) {
                try {
                    HDT hdt =  HDTManager.loadIndexedHDT(inDir +lang +"/wkd_uris_selection.gz.hdt", null);
                    langToHDT.put(lang, hdt);
                    Logger.getGlobal().info("loaded datatset: "+lang);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
            Logger.getGlobal().info("### loaded " + langToHDT.size() + "/" + prefOrd.size()+" ###");
        }

        return langToHDT;

    }

    static final List<String> properties =  Arrays.asList(
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
            "http://dbpedia.org/ontology/zipCode"
    );

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        Fusion.run();
        long end = System.currentTimeMillis();

        System.out.println("Took : " + ((end - start) / 1000));
    }
}

package outdated;

import fusion.TripleStringHelper;
import org.apache.commons.collections.map.HashedMap;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class FilterDataset {

    protected static String inDir = null;

    protected static List<String> prefOrd = new ArrayList<String>();
    protected static List<String> wkdUris = new ArrayList<String>();// null;
    protected static Map<String, HDT> l = null;

    public FilterDataset(List<String> prefOrd, List wkdUris, String inDir) {
        this.inDir = inDir;
        this.prefOrd = prefOrd;
        this.wkdUris = wkdUris;
        run();
    }

    public static void run () {
        l = getMap();
        for ( String wkdId : wkdUris) {//(String n : wkdUris) {
            HashSet<String> entityTriple= fuseEntity(wkdId);
            if(entityTriple.size() > 0) max++;
            if(max % 10000 == 0) {
                Logger.getGlobal().info("Entites done: "+max);
            }
//             writeEntityData(entityTriple, wkdId);
        }
        Logger.getGlobal().info("Entites done: "+max);
        for(String k : counter.keySet()) {
            System.out.println(k.substring(28)+"\t"+counter.get(k)+"\t"+discardedCounter.get(k));
        }
    }

    public static HashSet<String> fuseEntity(String wkdId) {
        String wkdUri = "http://wikidata.dbpedia.org/resource/Q"+wkdId;
        HashSet<String> lookup = new HashSet<String>(properties);
        HashSet<String> result = new HashSet<String>();

        Map<String,Set<String>>  discardedProperty = new HashMap<String,Set<String>>();

        for (String o : prefOrd) {
            HDT hdt = l.get(o);
            try {
                IteratorTripleString it = hdt.search(wkdUri, "", "");
                while (it.hasNext()) {
                    TripleString ts = it.next();
                    String p = ts.getPredicate().toString();
                    if( lookup.contains(p) ) {
//                        result.add(new def.TripleStringHelper(ts).asNtriple().toString());
//                        lookup.remove(p);
                        if(discardedProperty.containsKey(p))
                            discardedProperty.get(p).add(new TripleStringHelper(ts).asNtriple().toString());
                        else {
                            Set<String> init = new HashSet<>();
                            init.add(new TripleStringHelper(ts).asNtriple().toString());
                            discardedProperty.put(p,init);
                        }
                    }
//                    else if (p.equals(RDFS.label.getURI())) {
//                        result.add(new def.TripleStringHelper(ts).asNtriple().toString());
//                    } else if (p.equals(RDF.type.getURI())) {
//                        result.add(new def.TripleStringHelper(ts).asNtriple().toString());
//                    }
                }
            } catch (NotFoundException nfe) {
//                Logger.getLogger("NOTFOUND").info(wkdUri+" not found in "+o);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        for(String key :discardedProperty.keySet()) {
//          if(discardedProperty.get(key).size() > 1) System.out.println(discardedProperty.get(key));
            discardedCounter.get(key).addAndGet(discardedProperty.get(key).size());
            counter.get(key).addAndGet(1);
            result.add("notEmpty");
        }

        return result;
    }

    private static long max = 0;

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

    static Map<String, AtomicInteger> discardedCounter = new HashMap<>();
    static Map<String, AtomicInteger> counter = new HashMap<>();

    static {
        for( String p : properties) {
            discardedCounter.put(p, new AtomicInteger(0));
        }
        for( String p : properties) {
            counter.put(p, new AtomicInteger(0));
        }
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        FilterDataset.run();
        long end = System.currentTimeMillis();

        System.out.println("Took : " + ((end - start) / 1000));
    }
}

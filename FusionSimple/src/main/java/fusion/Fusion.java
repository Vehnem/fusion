package fusion;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import org.apache.commons.collections.map.HashedMap;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 *
 */
public class Fusion {

    protected static FileWriter fw = null;
    protected static String inDir = null;

    protected static long qMax = 0;
    protected static String baseUri = null;

    protected static boolean gz = false;
    protected static List<String> prefOrd = new ArrayList<String>();

    protected static boolean onlyfunc = false;

    protected static FusionWriter fusionWriter = null;

//    protected static List<String> wkdUris = null;
    protected static Map<String, HDT> l = null;

    public Fusion(List<String> prefOrd, long qMax, String inDir, String out, boolean gz, String baseUri, boolean onlyfunc) {
        this.inDir = inDir;
        this.prefOrd = prefOrd;
        this.gz = gz;
        this.qMax = qMax;
        this.baseUri = baseUri;
        this.onlyfunc = onlyfunc;

        fusionWriter = new FusionWriter(out,gz);

        run();
        Logger.getGlobal().info("Entites done: "+fusionWriter.getNumberOfEntities());
        Logger.getGlobal().info("Last written Entity: "+fusionWriter.getLastUri());

        fusionWriter.close();
    }

    /**
     * Iterative process over each Resource
     */
    public static void run () {
        l = getMap();
        for ( long id = 1; id <= qMax; id++) {//(String n : wkdUris) {
            String rUri = baseUri+id;
            HashSet<String> entityTriple= fuseEntity(rUri);
            fusionWriter.write(entityTriple, rUri);
        }
    }

    /**
     * Fusion function for each resource
     * @param rUri
     * @return
     */
    public static HashSet<String> fuseEntity(String rUri) {
        HashSet<String> lookup = new HashSet<String>(FunctionalProperties.get());
        HashSet<String> result = new HashSet<String>();
        for (String o : prefOrd) {
            HDT hdt = l.get(o);
            try {
                IteratorTripleString it = hdt.search(rUri, "", "");
                while (it.hasNext()) {
                    TripleString ts = it.next();
                    String p = ts.getPredicate().toString();
                    if( lookup.contains(p) ) {
                        result.add(new TripleStringHelper(ts).asNtriple().toString());
                        lookup.remove(p);
                    } else if(onlyfunc) {
                        // RDFS:label
                        if (p.equals(RDFS.label.getURI())) {
                            result.add(new TripleStringHelper(ts).asNtriple().toString());
                        }
                        // RDF:TYPE
                        if (p.equals(RDF.type.getURI())) {
                            result.add(new TripleStringHelper(ts).asNtriple().toString());
                        }
                    } else if(!onlyfunc){
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

    /**
     * Return map with loaded HDT databases
     * @return
     */
    public static Map getMap() {

        Map<String, HDT> langToHDT = null;

        if (langToHDT == null) {
            langToHDT = new HashedMap();
            for (String lang : prefOrd) {
                try {
                    // Hardcoded filename for hdts
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
}

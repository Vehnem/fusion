import com.hp.hpl.jena.rdf.model.*;
import org.apache.commons.collections.map.HashedMap;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.*;
import java.util.*;

public class Fusion{

    public static Model model = ModelFactory.createDefaultModel();

    public static int qs = 0;
    public static String inDir = "";
    public static String ouDir = "";
    public static List<String> laOrd = new ArrayList<String>();

    public Fusion(List<String> laOrd, String inDir, int maxQs, String ouDir) {
        this.inDir = inDir;
        this.laOrd = laOrd;
        this.qs = maxQs;
        this.ouDir = ouDir;
        run();
    }

    public static void run () {

        // TODO start variable
        int i = 1;

        Map<String, HDT> l = getMap();

//        Map<String,Model> data = new HashMap<String,Model>();

        // TODO change to set
        Map<String,List<TripleString>> data = new HashMap<String,List<TripleString>>();
        for(String o : Properties.languages) {
            data.put(o, new ArrayList<TripleString>());
//            data.put(o, ModelFactory.createDefaultModel());
        }
        data.put("fused",new ArrayList<TripleString>());

//        System.exit(0);
        for (; i < qs; i++) {
            String wdUri = "http://wikidata.dbpedia.org/resource/Q" + i;

            for (String property : Properties.properties) {

                boolean notFound = true;
                for (String o : laOrd) {

                    try {

                        HDT hdt = l.get(o);

                        IteratorTripleString it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
//                            System.out.println(o);
                            TripleString ts = it.next();
//                            data.get(o).add(ts);
                            if( notFound ) {
                                data.get("fused").add(ts);
                                notFound = false;
                            }
                        }
                    } catch (NotFoundException nfe) {
                        // TODO
                    }
                    try {

                        HDT hdt = l.get(o);
                        IteratorTripleString it = hdt.search(wdUri, "http://dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
//                            System.out.println(o);
                            TripleString ts = it.next();
//                            data.get(o).add(ts);
                            if( notFound ) {
                                data.get("fused").add(ts);
                                notFound = false;
                            }
                        }
                    } catch (NotFoundException nfe) {
                        // TODO
                    }
                }
            }
        }

        Model model = ModelFactory.createDefaultModel();
        for ( TripleString ts : data.get("fused") ) {
            Resource resource = ResourceFactory.createResource(ts.getSubject().toString());
            Property property = ResourceFactory.createProperty(ts.getPredicate().toString());
            String s = ts.getObject().toString();
            model.add(resource, property, s);
        }

        try {
            // TODO increment file number if exists
            OutputStream os = new FileOutputStream(new File(ouDir + "/fused.ttl"));
            model.write(os, "TTL");
        }catch (FileNotFoundException fne) {

        }

//        for ( String d : data.keySet()  ) {
            System.out.println("Fused Triples "+data.get("fused").size());
//        }
//        System.out.println("preference en > de > sv > nl > fr");
    }

    public static Map getMap() {

        Map<String, HDT> langToHDT = null;

        if (langToHDT == null) {
            langToHDT = new HashedMap();
            for (String lang : laOrd) {
                try {

                    langToHDT.put(lang, HDTManager.loadIndexedHDT(inDir + lang + "/wkd_uris_selection.gz.hdt", null));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("loaded " + langToHDT.size() + "/" + Properties.languages.length);
        }

        return langToHDT;

    }

//                int roof = value.indexOf("^^");
//                if (roof > 0) {
//                    value = value.substring(0, roof);
//                }
}

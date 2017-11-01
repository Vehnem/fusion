import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.query.DatasetFactory;
import com.hp.hpl.jena.rdf.model.*;
import com.sun.org.apache.regexp.internal.RE;
import org.apache.commons.collections.map.HashedMap;
import org.dbpedia.fusion.HDTFusion;
import org.dbpedia.fusion.Strings;
import org.dbpedia.fusion.WikidataQ;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTFactory;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.util.*;

public class Fusion {

    public static Map<String,Object> main_map = new HashMap<String,Object>();

    public static void main (String[]args) throws NotFoundException{

        int qs = 100;
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
            WikidataQ valag = new WikidataQ(wdUri);

            Map<String,Object> mp2 = new HashMap<String,Object>();
            for (String property : Properties.properties) {

                boolean notFound = true;
                for (String o : Properties.languages) {

                    try {

                        HDT hdt = l.get(o);

                        IteratorTripleString it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
//                            System.out.println(o);
                            TripleString ts = it.next();
                            data.get(o).add(ts);
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
                            data.get(o).add(ts);
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
//        for( TripleString ts : data.get("en")) {
//            System.out.println(ts);
//        }
//        System.out.println("### FUSED ###");
        for( TripleString ts : data.get("fused")) {
            System.out.println(ts);
        }
        for( TripleString ts : data.get("en")) {
            System.out.println(ts);
        }
        for( TripleString ts : data.get("de")) {
            System.out.println(ts);
        }
        for ( String d : data.keySet()  ) {
            System.out.println("Size "+d+" "+data.get(d).size());
        }
        System.out.println("preference en > de > sv > nl > fr");
    }

    public static Map getMap() {

        Map<String, HDT> langToHDT = null;

        if (langToHDT == null) {
            langToHDT = new HashedMap();
            for (String lang : Properties.languages) {
                try {

                    langToHDT.put(lang, HDTManager.loadIndexedHDT("/home/vehnem/workspace/fusion/data/hdt/downloads.dbpedia.org/2016-10/tmp/data/" + lang + "/wkd_uris_selection.gz.hdt", null));
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            }
//            System.out.println("loaded " + langToHDT.size() + "/" + Properties.languages.length);
        }

        return langToHDT;

    }

//                int roof = value.indexOf("^^");
//                if (roof > 0) {
//                    value = value.substring(0, roof);
//                }
}

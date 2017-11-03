import com.hp.hpl.jena.rdf.model.*;
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
import java.util.zip.GZIPOutputStream;

public class Fusion{

    public static Model model = ModelFactory.createDefaultModel();

    public static int qs = 0;
    public static String inDir = "";
    public static String ouDir = "";
    public static List<String> laOrd = new ArrayList<String>();
    public static Map<String, HDT> l = null;
    private static boolean gz = false;

//    public static Map<String,List<TripleString>> data = new HashMap<String,List<TripleString>>();
public static Map<String,List<String>> data = new HashMap<String,List<String>>();

    public Fusion(List<String> laOrd, String inDir, int maxQs, String ouDir, boolean gz) {
        this.inDir = inDir;
        this.laOrd = laOrd;
        this.qs = maxQs;
        this.ouDir = ouDir;
        this.gz = gz;
        run();
    }

    public static void run () {

        // TODO start variable
        int i = 1;

        l = getMap();

//        Map<String,Model> data = new HashMap<String,Model>();

        // TODO change to set

        for(String o : Properties.languages) {
            data.put(o, new ArrayList<String>());
//            data.put(o, ModelFactory.createDefaultModel());
        }
        data.put("fused",new ArrayList<String>());

//        System.exit(0);
        for (; i < qs; i++) {
            String wdUri = "http://wikidata.dbpedia.org/resource/Q" + i;

            for (String property : Properties.properties) {

                boolean notFound = true;
                for (String o : laOrd) {

                    try {

                        HDT hdt = l.get(o);
                        IteratorTripleString it = hdt.search(wdUri, "http://dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
//                            System.out.println(o);
                            TripleString ts = it.next();
//                            data.get(o).add(ts);
                            if( notFound ) {
                                data.get("fused").add(ts.asNtriple().toString());
                                notFound = false;
                            }
                        }
                    } catch (NotFoundException nfe) {
                        // TODO
                    } catch (IOException e) {

                }
                }
            }
            handleLables(wdUri);
            handleTypes(wdUri);

        }

//        Model model = ModelFactory.createDefaultModel();
//        for ( TripleString ts : data.get("fused") ) {
//            Resource resource = ResourceFactory.createResource(ts.getSubject().toString());
//            Property property = ResourceFactory.createProperty(ts.getPredicate().toString());
//            String s = ts.getObject().toString();
//            model.add(resource, property, s);
//        }
//
//        try {
//            // TODO increment file number if exists
//            OutputStream os = new FileOutputStream(new File(ouDir + "/fused.ttl"));
//            model.write(os, "TTL");
//        }catch (FileNotFoundException fne) {
//
//        }

        try {
            writeFile4(ouDir, "fused.nt", data.get("fused"), gz);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Fused Triples "+data.get("fused").size());

        String pref = "";
        for (int s = 0; s < laOrd.size() ; s++) {
            if (s == laOrd.size() - 1) {
                pref += laOrd.get(s);
            } else {
                pref += laOrd.get(s) + " > ";
            }
        }
        System.out.println("preference "+pref);
    }

    public static void handleLables(String wdUri) {
        for (String o : laOrd) {

            try {
                HDT hdt = l.get(o);
                IteratorTripleString it = hdt.search(wdUri, RDFS.label.getURI(), "");
                //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                while (it.hasNext()) {
//                            System.out.println(o);
                    TripleString ts = it.next();
//                            data.get(o).add(ts);
                    data.get("fused").add(ts.asNtriple().toString());
                }
            } catch (NotFoundException nfe) {
                // TODO
            } catch (IOException e) {

            }
        }
    }

    public static void handleTypes(String wdUri) {

        for (String o : laOrd) {

            try {
                HDT hdt = l.get(o);
                IteratorTripleString it = hdt.search(wdUri, RDF.type.getURI(), "");
                //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                while (it.hasNext()) {
//                            System.out.println(o);
                    TripleString ts = it.next();
//                            data.get(o).add(ts);
                    data.get("fused").add(ts.asNtriple().toString());
                }
            } catch (NotFoundException nfe) {
                // TODO
            } catch (IOException e) {

        }

        }
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
            System.out.println("loaded " + langToHDT.size() + "/" + laOrd.size());
        }

        return langToHDT;

    }
    public static void writeFile4(String outpath, String filename , List<String> data, boolean gz) throws IOException {

        if(gz) {
            File fout = new File(outpath + "/" + filename+".gz");
            FileOutputStream fos = new FileOutputStream(fout);
            OutputStreamWriter osw = new OutputStreamWriter(new GZIPOutputStream(fos), "UTF-8");

            for (String t : data) {
                osw.write(t);
            }
            osw.close();
        } else {
            File fout = new File(outpath + "/" + filename);
            FileOutputStream fos = new FileOutputStream(fout);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");

            for (String t : data) {
                osw.write(t);
            }
            osw.close();
        }
    }

//                int roof = value.indexOf("^^");
//                if (roof > 0) {
//                    value = value.substring(0, roof);
//                }
}

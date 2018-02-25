package outdated;

import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import fusion.TripleStringHelper;
import org.apache.commons.collections.map.HashedMap;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPOutputStream;

public class OldFusion {

    public static int qs = 0;
    public static String inDir = "";
    public static String ouDir = "";
    public static List<String> laOrd = new ArrayList<String>();
    public static Map<String, HDT> l = null;
    public static List<String> wkdUris = null;
    private static boolean gz = false;

// public static Map<String,List<TripleString>> data = new HashMap<String,List<TripleString>>();
public static Map<String,Set<String>> data = new HashMap<String,Set<String>>();

    public OldFusion(List<String> laOrd, List wkdUris, String inDir, int maxQs, String ouDir, boolean gz) {
        this.inDir = inDir;
        this.laOrd = laOrd;
        this.qs = maxQs;
        this.ouDir = ouDir;
        this.gz = gz;
        this.wkdUris = wkdUris;
        run();

    }

    public static void run () {
        // TODO start as input

        int n = 1;
        l = getMap();

        // TODO change to set
//        for(String o : def.Properties.languages) {
//            data.put(o, new HashSet<String>());
////            data.put(o, ModelFactory.createDefaultModel());
//        }

        data.put("fused",new HashSet<String>());

        for (n = 1; n < 100000; n++) {//(String n : wkdUris) {
            String wdUri = "http://wikidata.dbpedia.org/resource/Q" + n;

            for (String property : Properties.properties) {
                boolean found = false;

                for (String o : laOrd) {
                    if(found) break;
                    HDT hdt = l.get(o);
                    try {
                        IteratorTripleString it = hdt.search(wdUri, "http://dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
                            TripleString ts = it.next();
//                            data.get(o).add(ts);
                            data.get("fused").add(ts.asNtriple().toString());
                            found = true;
                            break;
                        }
                    } catch (NotFoundException nfe) {
                        // TODO
                    } catch (IOException e) {
                        // TODO
                    }
                    try {
                        IteratorTripleString it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                        while (it.hasNext()) {
                            TripleString ts = it.next();
//                            data.get(o).add(ts);
                            data.get("fused").add(ts.asNtriple().toString());
                            found = true;
                            break;
                        }
                    } catch (NotFoundException nfe) {
                        // TODO
                    } catch (IOException e) {
                        // TODO
                    }
                }
            }
            handleLables(wdUri);
            handleTypes(wdUri);

        }

        // TODO increment file number
        try {
            writeFile4(ouDir, "fused.nt", data.get("fused"), gz);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("Fused Triples "+data.get("fused").size());
    }

    public static void handleLables(String wdUri) {
        for (String o : laOrd) {

            try {
                HDT hdt = l.get(o);
                IteratorTripleString it = hdt.search(wdUri, RDFS.label.getURI(), "");
                //if( it.hasNext() ) it = hdt.search(wdUri, "http://wikidata.dbpedia.org/ontology/" + property, "");
                while (it.hasNext()) {
                    TripleString ts = it.next();
                    data.get("fused").add(new TripleStringHelper(ts).asNtriple().toString());
                }
            } catch (NotFoundException nfe) {
                // TODO
            }
            catch (IOException e) {
                // TODO
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
                    TripleString ts = it.next();
                    data.get("fused").add(ts.asNtriple().toString());
                }
            } catch (NotFoundException nfe) {
                // TODO
            } catch (IOException e) {
                // TODO
        }

        }
    }

    public static Map getMap() {

        Map<String, HDT> langToHDT = null;

        if (langToHDT == null) {
            langToHDT = new HashedMap();
            for (String lang : laOrd) {
                try {
                    HDT hdt = HDTManager.loadIndexedHDT(inDir+lang+"/wkd_uris_selection.gz.hdt", null);
//                    HDT hdt =  HDTManager.generateHDT("//home/vehnem/workspace/fusion/FusionSimple/fr_small", "http://base.uri", RDFNotation.NTRIPLES, new HDTSpecification(), null);
                    langToHDT.put(lang, hdt);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println(e.getMessage());
                }
            }
            System.out.println("loaded " + langToHDT.size() + "/" + laOrd.size());
        }

        return langToHDT;

    }
    public static void writeFile4(String outpat, String filenam , Set<String> data, boolean gz) throws IOException {

        String filename = "/home/vehnem/workspace/fusion/FusionSimple/results/old.nt";
        String outpath ="";
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
}

package outdated;

import org.rdfhdt.hdt.enums.RDFNotation;
import org.rdfhdt.hdt.exceptions.NotFoundException;
import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.options.HDTSpecification;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class LabelsTest {

    public static void main ( String[] args ) throws IOException {

//        String[] languages =  {"en","nl","sv","fr","de","fused"};

        String[] languages = {"en","wiki","de","sv","nl","fr","fused"};

        String rdfType =  "http://dbpedia.org/ontology/Person";
        String header = "wdUri";
//        Map <String, AtomicInteger> counter = new HashMap<String, AtomicInteger>();

        Map<String, Map<String, Integer>> lanMap = new HashMap<String, Map<String, Integer>>();

        for(String l : languages) {
            header += "\t"+l;
            lanMap.put(l,getCounts("/home/vehnem/workspace/fusion_data/out/2211/k100"+l+"/fused.nt",rdfType));
        }

        header+="\n";

        String out =  rdfType == null ? "label_overall.tsv" :  "label_"+ rdfType.split("/")[rdfType.split("/").length-1] +".tsv";

        FileWriter fw = new FileWriter(new File(out));
        fw.write(header);
        for(int i = 1 ; i <= 100000; i++ ) {
            String q = "http://wikidata.dbpedia.org/resource/Q"+i;

            if( lanMap.get("fused").containsKey(q) ) {
                String line = q;
                for(String l : languages) {
                    int number = lanMap.get(l).get(q) == null ? 0 : lanMap.get(l).get(q);
                    line += "\t"+number;
                }
                line += "\n";
                fw.write(line);
            }
        }
        fw.flush();
        fw.close();

        for(String l : languages) {
            long ct = 0;
            for(int i = 1 ; i <= 100000; i++ ) {
                String q = "http://wikidata.dbpedia.org/resource/Q" + i;
                int number = lanMap.get(l).get(q) == null ? 0 : lanMap.get(l).get(q);
                ct += number;
            }
            System.out.println(l+ " contains "+lanMap.get(l).keySet().size()+" entities "+ct+" labels");
        }

    }

    public static Map<String, Integer> getCounts (String filename, String rdfType) {

        HDT hdt = null;
        Map<String, Integer> ctMap = new HashMap<String, Integer>();
        try {
            hdt = HDTManager.generateHDT(filename, "http://wikidata.dbpedia.org", RDFNotation.NTRIPLES, new HDTSpecification(), null);

        } catch (Exception e) { e.printStackTrace(); }

        int i = 1;
        int k = 100000;
        for( ; i <= k ; i++) {
            String wdUri = "http://wikidata.dbpedia.org/resource/Q"+i;
            try {
                IteratorTripleString it = hdt.search(wdUri, "", "");
                boolean typefound = false;
                Set<String> set = new HashSet<String>();
                while( it.hasNext() ) {
                    TripleString ts = it.next();
                    if(ts.getPredicate().toString().equals("http://www.w3.org/2000/01/rdf-schema#label"))
                        set.add(ts.getObject().toString());
                    if(ts.getObject().toString().equals(rdfType)) typefound = true;
                }
                if(typefound || null == rdfType ) ctMap.put(wdUri,set.size());
            } catch ( NotFoundException nfe ) {
                //nfe.printStackTrace();
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        return ctMap;
    }
}

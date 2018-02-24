package dev;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CardinalityTest {

    public static void main ( String[] args ) throws IOException {
        String filename = "/home/vehnem/workspace/fusion_data/out/k100en/fused.nt";
        String filename2 = "/home/vehnem/workspace/fusion_data/out/k100fused/fused.nt";

        String rdfType =  "http://dbpedia.org/ontology/Person";

        Map<String, Integer> enMap = getCounts(filename, rdfType);
        Map<String, Integer> fusedMap = getCounts(filename2, rdfType);

        String[] spl = rdfType.split("/");
        String out =  rdfType == null ? "cardinality_overall.tsv" :  "cardinality_"+ spl[spl.length-1] +".tsv";

        FileWriter fw = new FileWriter(new File(out));
        for(int i = 1 ; i <= 100000; i++ ) {
            String q = "http://wikidata.dbpedia.org/resource/Q"+i;

           if( fusedMap.containsKey(q) ) fw.write(q+"\t"+enMap.get(q)+"\t"+fusedMap.get(q)+"\n");
        }
        fw.flush();
        fw.close();

        System.out.println("en contains "+enMap.values().size());
        System.out.println("fused contains "+fusedMap.values().size());
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
                    set.add(ts.getPredicate().toString());
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

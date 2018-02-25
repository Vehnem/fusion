package outdated;

import org.rdfhdt.hdt.hdt.HDT;
import org.rdfhdt.hdt.hdt.HDTManager;
import org.rdfhdt.hdt.triples.IteratorTripleString;
import org.rdfhdt.hdt.triples.TripleString;

import java.io.IOException;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Test {

    private static final Logger log = Logger.getLogger( Test.class.getName() );
    public static void main(String[] agrs) throws IOException{

        Logger.getGlobal().log(Level.INFO,"info");

        Logger.getLogger("DEFAULT").setUseParentHandlers(false);
        Logger.getLogger("DEFAULT").log(Level.WARNING, "warning");



//        int maxID = 1;
//        List<Integer> ids = new ArrayList<Integer>();
//        String[] languages = {"en","de","sv","nl","fr"};
//        // def.Test
        String inDir = "/home/vehnem/workspace/fusion_data/new/";
//
//        HDT hdt = HDTManager.loadIndexedHDT(inDir +"wikidata" + "/wkd_uris_selection.gz.hdt", null);
//        HDT hdt = null;
//        try {
//             hdt = HDTManager.generateHDT("/home/vehnem/workspace/fusion_data/out/2211/k100fused/fused.nt", "http://base.uri", RDFNotation.NTRIPLES, new HDTSpecification(), null);
//        } catch (Exception e) {
//
//        }
        String last = "";
        HashSet<String> types = new HashSet<String>();
//        int i = 20000000;
//        while(true){
            try {
                HDT hdt = HDTManager.loadIndexedHDT("/home/vehnem/workspace/fusion_data/new/wikidata/wkd_uris_selection.gz.hdt", null);
                IteratorTripleString it = hdt.search("http://wikidata.dbpedia.org/resource/Q37175","", "");
//                IteratorTripleString it = hdt.search("",
//                        "", "");
                while (it.hasNext()) {
                    TripleString ts = it.next();
                    System.out.println(ts.asNtriple());
//                    int id = Integer.parseInt(ts.getSubject().toString().substring(38));
//                    maxID = id > maxID ?  id :  maxID;
//                def.TripleStringHelper h = new def.TripleStringHelper(ts);
//                //mainmodel.add(getAsStatement(ts));
//                FileWriter fw = new FileWriter(new File("test"));
//                fw.write(h.asNtriple().toString());
//                fw.close();
//                break;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
            }
//            i++;
//            log.info(String.valueOf(maxID));
//        }

//        for(String t : types ) {
//            System.out.println(t);
//        }

//        Model m = ModelFactory.createDefaultModel();
//        Statement st = ResourceFactory.createStatement(ResourceFactory.createResource("http://foo.org/q1"),
//                ResourceFactory.createProperty("http://foo.org/p1"),
//                       ResourceFactory.createLangLiteral("\"foo","de")
//                );
//
//        m.write(System.out, "N-TRIPLES");
//        //RDFDataMgr.writeTriples();
//        m.add(st);

        //mainmodel.write(System.out,"TTL");
//
//        List<String> list = new ArrayList<String>();

// "Lionas \Olympique\"@lv
//        int i;
//        char c;
//        int first = 0;
//        int last =  0;//label.length();
//        StringBuilder appendable = new StringBuilder();
//        appendable.append("\\\\\\\"");
//        for(i = first; i < last; ++i) {
//            c = '\\';//label.charAt(i);
//            if (c == '\\') {
//                appendable.append("\\\\");
//            } else if (c == '"') {
//                appendable.append("\\\"");
//            } else if (c == '\n') {
//                appendable.append("\\n");
//            } else if (c == '\r') {
//                appendable.append("\\r");
//            } else if (c == '\t') {
//                appendable.append("\\t");
//            } else if (c >= 0 && c <= '\b' || c == 11 || c == '\f' || c >= 14 && c <= 31 || c >= 127 && c <= '\uffff') {
//                appendable.append("\\u");
//                appendable.append(toHexString(c, 4));
//            } else if (c >= 65536 && c <= 1114111) {
//                appendable.append("\\U");
//                appendable.append(toHexString(c, 8));
//            } else {
//                appendable.append(c);
//            }
//        }
//        System.out.println(appendable.length());
//    }
//
//    public static String toHexString(int decimal, int stringLength) {
//        StringBuilder sb = new StringBuilder(stringLength);
//        String hexVal = Integer.toHexString(decimal).toUpperCase();
//        int nofZeros = stringLength - hexVal.length();
//
//        for(int i = 0; i < nofZeros; ++i) {
//            sb.append('0');
//        }
//
//        sb.append(hexVal);
//        return sb.toString();
//    }
//
//    public static Model getAsStatement(TripleString ts) {
//        Model m = ModelFactory.createDefaultModel();
//        try {
//            System.out.println(ts.asNtriple().toString());
//            } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return m;
//    }
    }
}

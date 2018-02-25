package fusion;

import java.io.*;
import java.nio.Buffer;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.zip.GZIPOutputStream;

public class FusionWriter {

    protected long max = 0;

    protected String lastUri = "";

    protected BufferedWriter bw = null;

    FusionWriter(String out, boolean gz) {
        if(gz) {
            try {
                bw = new BufferedWriter(new OutputStreamWriter( new GZIPOutputStream(new FileOutputStream(out + ".gz"))));
            } catch (IOException ioe ) { ioe.printStackTrace();}
        } else {
            try {
                bw = new BufferedWriter(new OutputStreamWriter((new FileOutputStream(out))));
            }catch ( IOException ioe ) {ioe.printStackTrace();}
        }
    }

    public void write(HashSet<String> tripleSet, String rUri) {
        Objects.requireNonNull(tripleSet);
        if( tripleSet.size() > 0 ) max++;
        try {
            for (String triple: tripleSet) {
                bw.write(triple);
            }
            bw.flush();
            lastUri = rUri;
        } catch (IOException ioe) {
            Logger.getGlobal().warning("cant write triple");
            ioe.printStackTrace();
        }
        if(max % 10000 == 0) {
            Logger.getGlobal().info("Entites done: "+max);
            Logger.getGlobal().info("last one: "+lastUri);
        }
    }

    public void close() {
        try {
           bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public long getNumberOfEntities() {
        return max;
    }

    public String getLastUri() {
        return lastUri;
    }
}

//protected static FileWriter bw = null;

//            try {
//            bw = new FileWriter(out);
//            } catch (Exception e) {
//            e.printStackTrace();
//            System.exit(1);
//            }

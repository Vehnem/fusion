package fusion;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.logging.Logger;

public class FusionWriter {

    protected static long max = 0;
    protected static FileWriter fw = null;
    protected static String lastUri = null;

    FusionWriter(String out, boolean gz) {
        try {
            fw = new FileWriter(out);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void write(HashSet<String> tripleSet, String rUri) {
        Objects.requireNonNull(tripleSet);
        if( tripleSet.size() > 0 ) max++;
        try {
            for (String triple: tripleSet) {
                fw.write(triple);
            }
            fw.flush();
        } catch (IOException ioe) {
            Logger.getGlobal().warning("cant write triple");
            ioe.printStackTrace();
        }
        if(max % 10000 == 0) {
            Logger.getGlobal().info("Entites done: "+max);
            lastUri = rUri;
            Logger.getGlobal().info("last one: "+lastUri);
        }
    }

    public void close() {
        try {
            fw.close();
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

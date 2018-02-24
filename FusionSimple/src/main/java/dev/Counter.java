package dev;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Counter {



    public static void main(String[] args) {

        try {
            FileReader fr = new FileReader("resrc/test_count");
            BufferedReader br = new BufferedReader(fr);
            String line;
            while( ( line = br.readLine() ) != null ) {
                map.get(line).addAndGet(2);
            }
        } catch (FileNotFoundException fne) {
            fne.printStackTrace();
        } catch (IOException ioe ) {
            ioe.printStackTrace();
        }

        for(String m : map.keySet()) {
            System.out.println(m+" "+map.get(m));
        }

    }

    static final List<String> things =  Arrays.asList(
            "a",
            "b",
            "c",
            "d"
    );

    static Map<String, AtomicInteger> map = new HashMap<>();

    static {
        for( String t : things) {
            map.put(t, new AtomicInteger(0));
        }
    }
}

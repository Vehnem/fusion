import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Tuple2;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

public class Main {

    static int row = 0;
    protected static FileWriter fw = null;

    public static HashSet<String> addElement(HashSet<String> a, HashSet<String> b) {
        HashSet<String> c = a;
        c.addAll(b);
        return c;
    }
    static int c = 0;
    public static void main(String[] args) {
        String master = "local[*]";

        try {
            fw = new FileWriter(new File("out"));
        } catch(Exception e) {e.printStackTrace();}

        Function2<HashSet<String>, HashSet<String>, HashSet<String>> redFunc = (a, b) -> addElement(a,b) ;

        VoidFunction<Tuple2> voidFunc = a -> writ4rdd(a);
        Logger LOGGER = LoggerFactory.getLogger(Main.class);
    /*
     * Initialises a Spark context.
     */
        SparkConf conf = new SparkConf()
                .setAppName(WordCountTask.class.getName())
                .setMaster(master);
        JavaSparkContext context = new JavaSparkContext(conf);
    /*
     * Performs a work count sequence of tasks and prints the output with a logger.
     */

        JavaRDD<String> rdd = context.textFile("/home/vehnem/workspace/fusion/FusionSpark/insort");
        rdd.flatMap(text -> Arrays.asList(text.split("\n")).iterator())
                .mapToPair(line -> new Tuple2<>(line.split(" ")[0], new HashSet<>(Arrays.asList(line))))
                .reduceByKey(redFunc)
                .foreach(result -> writ4rdd(result));
//        context.textFile("/home/vehnem/workspace/fusion_data/new/ru/wkd_uris_selection")
//                .flatMap(text -> Arrays.asList(text.split("\n")).iterator())
//                .mapToPair(word -> new Tuple2<>(word, 1))
//                .reduceByKey((a, b) -> a + b)
//                .foreach(result -> java.util.logging.Logger.getLogger("Default").info(
//                        String.format("Word [%s] count [%d].", result._1(), result._2)));
    }

    static void writ4rdd(Tuple2 str) {
//            if( row < 10000 ) {
                row++;
//                java.util.logging.Logger.getLogger("OUT").info("im at " + row);
                try {

                    HashSet<String> d = (HashSet<String>) str._2;
                    System.out.println(str._1()+"#"+d.size());
//                    fw.write(str._1 ++ "\n");
//                    fw.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
//            }else {System.exit(0);}
    }
}
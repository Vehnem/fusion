//package dev;
//
//import org.apache.jena.query.*;
//
//public class RDFCrawler {
//
////    public static void main(String[]args) {
////        String queryString="" +
////                "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
////                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
////                "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
////                "construct{ ?s dbo:birthDate ?o }\n" +
////                "where {\n" +
////                "   GRAPH <http://fused.dbpedia.org/> { ?s dbo:birthDate ?o } \n" +
////                "   FILTER NOT EXISTS { GRAPH <http://wiki.dbpedia.org/> { ?s dbo:birthDate ?o2 } }\n" +
////                "}";
////
////
////            // now creating query object
////        Query query = QueryFactory.create(queryString);
////        // initializing queryExecution factory with remote service.
////        // **this actually was the main problem I couldn't figure out.**
////            QueryExecution qexec = QueryExecutionFactory.sparqlService("http://v122.de:8890/sparql", query);
////
////        //after it goes standard query execution and result processing which can
////        // be found in almost any Jena/SPARQL tutorial.
////        try {
////                ResultSet results = qexec.execSelect();
////                for (; results.hasNext();) {
////
////                    // Result processing is done here.
////                }
////            }
////        finally {
////                qexec.close();
////            }
////    }
//}
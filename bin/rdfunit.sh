#!/bin/bsah

cd $rdfunit
bin/rdfunit -C \
 -d "http://$1.dbpedia.org/" \
 -g "http://$1.dbpedia.org/" \
 -e "http://localhost:8890/sparql" \
1> "log/$1.out" 2> "log/$1.log"

#!/bin/bash

endpoint=$1
property=$2

return=`curl -s -G $endpoint \
 --data-urlencode query="PREFIX dbo: <http://dbpedia.org/ontology/> SELECT (COUNT(?sub) as ?total) WHERE { { SELECT distinct ?sub ?obj WHERE { ?sub dbo:$property ?obj . } } }" \
 | grep "binding" \
 | cut -d ">" -f3 \
 | cut -d "<" -f1`

echo -e "$property\t$return"

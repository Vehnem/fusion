#!/bin/bash

endpoint=$1
graph=$2
type=$3

return=`curl -s -G $endpoint \
 --data-urlencode query="PREFIX dbo: <http://dbpedia.org/ontology/> SELECT (COUNT(distinct ?sub) as ?total) FROM <$graph> WHERE { ?sub a dbo:$type }" \
 | grep "binding" \
 | cut -d ">" -f3 \
 | cut -d "<" -f1`

echo -e "$type\t$return"

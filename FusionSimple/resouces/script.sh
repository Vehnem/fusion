#/bin/sh
grep -B1 "Functional" dbpedia_ontology.ttl | awk -F '\n' 'ln ~ /^$/ { ln = "matched"; print $1 } $1 ~ /^--$/ { ln = "" }' | grep -o '[a-zA-Z1-9]*' > functional
cat functional | while read -r a; 
do 
    echo '"'$a'",'; 
done


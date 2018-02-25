#!/bin/bash

source config.sh

for i in `ls $hdtfolder` ; do
        echo $i
        if [ ! -f $folder/$i/wkd_uris_selection.gz ]; then
                CMD="sort -mu -S10G -T ./sorttmp "
                for a in `find $hdtfolder/$i -name "*.ttl.bz2"` ; do
                        CMD="$CMD <(lbzip2 -dc '$a') "
                done
                eval  "$CMD" | sed 's|http://wikidata.dbpedia.org/ontology|http://dbpedia.org/ontology|'  | rapper -i ntriples  -O - - file | gzip > $hdtfolder/$i/wkd_uris_selection.gz
        fi
        echo "done"
done

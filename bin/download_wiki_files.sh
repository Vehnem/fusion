#!/bin/bash

source config.sh

echo "download files"

while read -r f
do
        if [[ $f != \#* ]]
        then
                wget -P "$hdtfolder/wiki/" "http://78.46.100.7/wikidata/20180201/wikidatawiki-$wikidata-$f"
        fi
done < $1	
echo "done!"

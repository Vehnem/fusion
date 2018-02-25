#!/bin/bash

source config.sh

for i in `find $hdtfolder -name "*wkd_uris_selection.gz" ` ; do
        echo $i
        if [ ! -f $i.hdt ]; then
                $rdf2hdt -p $i $i.hdt
        fi
        echo "done"
done

#!/bin/bash

source config.sh

echo "download files"
for l in {fr,nl,sv,de,en}
do
	echo "doing $l"
	while read -r f
	do
		if [[ $f != \#* ]]
		then
			wget -P "$hdtfolder/$l/" "http://downloads.dbpedia.org/2016-10/core-i18n/$l/$f$l.ttl.bz2"
		fi
	done < files
done
echo "done!"

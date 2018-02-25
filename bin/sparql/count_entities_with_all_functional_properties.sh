#!/bin/bash 

endpoint=$1
graph=$2
properties_file=$3

while read -r a
do
	./count_entities_with_dbo_property.sh $1 $2 $a
done < $properties_file

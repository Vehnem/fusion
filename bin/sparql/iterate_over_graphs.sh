#!/bin/bash

endpoint=$1
graph_file=$2
type=$3

while read -r a
do
	echo -ne $a"\t"
	./count_dbo_type.sh $endpoint $a $type
done < $graph_file


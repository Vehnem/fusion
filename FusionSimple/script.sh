#/bin/sh
cat functional | while read -r a; 
do 
    echo '"'$a'",'; 
done


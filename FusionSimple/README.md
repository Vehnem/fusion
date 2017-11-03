# SimpleFusion based on hdt files

## download data
```
mkdir -p data/hdt
cd data/hdt
wget -r --no-parent http://downloads.dbpedia.org/2016-10/tmp/data/
```

## build and run
```
mvn package
java -jar target/FusionSimple-1.0-SNAPSHOT-jar-with-dependencies.jar -i <input/data/folder> -o <output/folder> -l <file> -q <n>
```
actually, `-Xmx12G` is enough 

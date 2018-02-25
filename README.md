# fusion

algorithms to fuse dbpedia and wikidata

```
git clone https://github.com/Vehnem/fusion.git
```

## Usage

**basics** 

```
bin/simple-fusion -h
```

**example call**

```
bin/simple-fusion -i /fusion/hdtfolder/ -o /fusion/out/ -p FusionSimple/resources/preference  -q 100000
```
## Advanced

For automated setup with hdt creation and rdfunit, 
configure **config.sh** and edit properties for your setup.

### Create hdts

```
cd bin/
./download_files.sh files; download_wiki_files.sh;
./togz.sh
./tohdt.sh
```

### Statistics

I created some simple curl scripts to get information about your loaded rdf from a sparql endpoint 

## HDTFolder structure 

├── hdtfolder  
│   ├── en  
│   │   └── wkd_uris_selection.gz.hdt  
│   ├── wiki  
│   │   └── wkd_uris_selection.gz.hdt  
│   ├── de  
│   │   └── wkd_uris_selection.gz.hdt  

## RDFUnit

please use https://github.com/Vehnem/RDFUtmp.git

### Usage
i created some reconfigurable scripts in the fusion repo too!

**example call**

```
bin/rdfunit -C \
 -d "name_of_dataset" \
 -g "graph_name" \
 -e "endpoint_name" \
1> "rdfunit.out" 2> "rdfunit.log"
```

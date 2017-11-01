import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RetrieveRemoteRDF {
    public static void main(String[] args) {
        final String url = "http://downloads.dbpedia.org/2016-10/core-i18n/de/2016-10_dataid_de.ttl";
        final Model model = ModelFactory.createDefaultModel();
        model.read(url);
        model.write(System.out);
    }
}
package org.imirsel.probes;

import java.io.InputStream;

import com.hp.hpl.jena.rdf.model.*;

public class NEMARepositoryGenerator {
	
	
	public static void main(String args[]){
		NEMARepositoryGenerator.getTestHelloWorldRepository();
		
	}

	public static Model getTestHelloWorldRepository() {
	    Model m = ModelFactory.createDefaultModel();
	    InputStream in = NEMARepositoryGenerator.class.getResourceAsStream("demo_repository.ttl");
		RDFReader  reader = m.getReader("TTL");
		reader.read(m, in, null);
		return m;
	}

}

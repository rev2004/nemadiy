package org.imirsel.probes;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.PropertiesDescriptionDefinition;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;

import com.hp.hpl.jena.rdf.model.Model;

public class NemaMetadataRepositoryTest {
	
	
	@Test
	public void testGetAvailableExecutableComponents() {
		Model model = NEMARepositoryGenerator.getTestHelloWorldRepository();
		QueryableRepository qr = new RepositoryImpl(model);
	
		
		Set<ExecutableComponentDescription> set= qr.getAvailableExecutableComponentDescriptions();
		Iterator<ExecutableComponentDescription> it = set.iterator();
		System.out.println("Number of Component Descriptions: " + set.size());
		while(it.hasNext()){
		ExecutableComponentDescription ecd=	it.next();
		System.out.println(ecd.getName().toUpperCase());
		System.out.println(">Looking for Properties: ");
		PropertiesDescriptionDefinition pdd= ecd.getProperties();
		Set<String> keyList=pdd.getKeys();
		Iterator<String> key = keyList.iterator();
		String thisKey=null;
		
		while(key.hasNext()){
			thisKey = key.next();
			System.out.println("KEY: " + thisKey + " Value: " + pdd.getValue(thisKey));
		}
		
		Map<String, String> ss= pdd.getOtherProperties("count");
		System.out.println("==>"+ss.size());
		Set<String> propKeySet = ss.keySet();
		Iterator<String> its = propKeySet.iterator();
		while(its.hasNext()){
			String keyString = its.next();
			System.out.println(keyString +" -- "+ ss.get(keyString));
		}
		
		
		}
		
	}


}

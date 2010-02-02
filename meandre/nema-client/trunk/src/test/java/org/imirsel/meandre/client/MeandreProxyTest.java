package org.imirsel.meandre.client;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.QueryableRepository;

import com.hp.hpl.jena.rdf.model.Resource;

/** MeandreProxy Tests
 * 
 * @author kumaramit01
 * @since 0.5.0
 */
public class MeandreProxyTest {
	private MeandreProxy mp;
	
	@Before
	public void setUp(){
		mp = new MeandreProxy("admin","admin","nema.lis.uiuc.edu",11709);
			
	}
	
	
	@Test
	 public  void testGetFlows(){
		QueryableRepository qp= mp.getRepository();
		Set<Resource> resources=qp.getAvailableFlows();
		assertTrue(resources.size()>0);
		
		Iterator<Resource> itr=resources.iterator();
		
		System.out.println("The list of flows are: ");
		while(itr.hasNext()){
			Resource resource=itr.next();
			System.out.println("URI: "+resource.getURI());
		}
		
		Map<String, FlowDescription> map=qp.getAvailableFlowDescriptionsMap();
		
		Iterator<String> itk = map.keySet().iterator();
		
		
		while(itk.hasNext()){
			String key = itk.next();
			System.out.println("Number of Components in "+key + "  " + map.get(key).getExecutableComponentInstances().size());
			Set<ExecutableComponentInstanceDescription> se=map.get(key).getExecutableComponentInstances();
			Iterator<ExecutableComponentInstanceDescription> ite = se.iterator();
			while(ite.hasNext()){
				ExecutableComponentInstanceDescription ecd = ite.next();
				Map<String,String> propMap=ecd.getProperties().getValueMap();
				System.out.println("Component: and uri: " + ecd.getName() +" " +ecd.getExecutableComponentInstance().getURI());
				Iterator<String> its = propMap.keySet().iterator();
				
				while(its.hasNext()){
					System.out.println("property: " + its.next());
				}
			}
		}
		
	/*	
		System.out.println("Now working with a particular flow: ");

		String flowUri="http://imirsel.org/analysis/melody/melody_durrieu/melody_durrieu/";
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefix("meandre", Store.MEANDRE_ONTOLOGY_BASE_URL );
		model.setNsPrefix("xsd", XSD.getURI());
		model.setNsPrefix("rdf", RDF.getURI());
		model.setNsPrefix("rdfs",RDFS.getURI());
		model.setNsPrefix("dc",DC.getURI());
		ExecutableComponentDescription ecd=qp.getExecutableComponentDescription(model.createResource(flowUri));
		
		
		System.out.println("ecd is "+ ecd);
		
		PropertiesDescriptionDefinition propertiesDefn=ecd.getProperties();
		
		Collection<String> values=propertiesDefn.getValues();
		
		Iterator<String> its = values.iterator();
		
		while(its.hasNext()){
			System.out.println("value: "+its.next());
		}
		
		Map<String,String>s =propertiesDefn.getOtherProperties("SampleRate");
		System.out.println(s.keySet().size());
		
		Iterator<String> it1 = s.keySet().iterator();
		
		while(it1.hasNext()){
			String key = it1.next();
			System.out.println(key +"  " + s.get(key));
		}
		Set<String> keyset=propertiesDefn.getKeys();
		Iterator<String> its1 = keyset.iterator();
		while(its1.hasNext()){
			System.out.println("key is: "+its1.next());
		}
		*/
		
		
	}
	
}

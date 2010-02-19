package org.imirsel.meandre.client;

import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
	
	@BeforeClass
	public void setUp(){
		mp = new MeandreProxy("admin","admin","nema.lis.uiuc.edu",11709);
	}
	
	@AfterClass
	public void teardown(){
		mp.detroy();
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
		
	}
	
}

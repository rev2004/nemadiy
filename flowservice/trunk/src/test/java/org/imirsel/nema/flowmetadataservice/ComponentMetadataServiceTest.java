package org.imirsel.nema.flowmetadataservice;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.annotatons.parser.beans.DataTypeBean;
import org.imirsel.nema.flowmetadataservice.impl.ComponentMetadataServiceImpl;
import org.imirsel.nema.flowservice.MeandreServerProxy;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author kumaramit01
 * @since 0.5.0
 *
 */
public class ComponentMetadataServiceTest{
	
	private final ComponentMetadataServiceImpl componentMetadataManager = new ComponentMetadataServiceImpl();
	private MeandreServerProxy meandreServerProxy;
	private RepositoryClientConnectionPool repositoryClientConnectionPool;
	
	@Before
	public void setup(){
		String host ="nema.lis.uiuc.edu";
		String password = "admin";
		String username ="admin";
		int port = 11709;
		int maxConcurrentJobs =1;
		
		MeandreServerProxyConfig config = new MeandreServerProxyConfig(
				username,password,host,port,maxConcurrentJobs);
		
		meandreServerProxy = new MeandreServerProxy(config);
		meandreServerProxy.init();
		
		repositoryClientConnectionPool = RepositoryClientConnectionPool.getInstance();
		
		componentMetadataManager.setMeandreServerProxy(meandreServerProxy);
		
		componentMetadataManager.setRepositoryClientConnectionPool(repositoryClientConnectionPool);
	}
	


	@Test
	public void testGetComponentDataType() throws TransmissionException, SQLException{
		String componentUri="meandre://seasr.org/components/datatypetestcomponent";
		String instanceUri = "http://test.org/datatypetest/instance/datatypetestcomponent/1";
		Component component= new Component();
		component.setInstanceUri(instanceUri);
		component.setUri(componentUri);
		Map<String, Property> map=componentMetadataManager.getComponentPropertyDataType(component, instanceUri);
		System.out.println("--> here "+ map.size());
		Iterator<Entry<String,Property>> its = map.entrySet().iterator();
		while(its.hasNext()){
			Entry<String,Property> tmp = its.next();
			Property property =tmp.getValue();
			List<DataTypeBean> ltb = property.getDataTypeBeanList();
			System.out.println(property.getName() + "  " + property.getDefaultValue());
			 Iterator<DataTypeBean> itb = ltb.iterator();
			while(itb.hasNext()){
				DataTypeBean dt = itb.next();
				System.out.println("class Name:"+ dt.getClass().getName());
				System.out.println("Renderer: "+dt.getRenderer());
			}
			System.out.println("---------------------");
		}
		 

	
	}
	
	

}

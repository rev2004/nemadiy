package org.imirsel.nema.service;

import java.util.List;

import org.imirsel.nema.model.Component;
import org.imirsel.nema.service.BaseManagerTestCase;
import org.imirsel.nema.service.impl.FlowMetadataServiceImpl;
import org.imirsel.nema.service.impl.MeandreProxyWrapper;


public class FlowMetadataServiceTest extends BaseManagerTestCase{
	
	private FlowMetadataServiceImpl flowMetadataService = new FlowMetadataServiceImpl();
	private MeandreProxyWrapper meandreProxyWrapper;
	
	public FlowMetadataServiceTest(){
		meandreProxyWrapper = new MeandreProxyWrapper();
		meandreProxyWrapper.init();
		flowMetadataService.setMeandreProxyWrapper(meandreProxyWrapper);
	}
	
	public void testGetComponents(){
		String flowUri="http://test.org/datatypetest/";
		List<Component>componentList=flowMetadataService.getComponents(flowUri);
		for(int i=0;i< componentList.size();i++){
			System.out.println("--> "+componentList.get(i).getInstanceUri());
		}
		//componentMetadataManager.getComponentPropertyDataType(componentId);
	}
	

}

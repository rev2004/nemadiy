package org.imirsel.nema.flowmetadataservice.impl;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.client.beans.repository.WBFlowDescription;
import org.imirsel.nema.flowmetadataservice.CorruptedFlowException;
import org.imirsel.nema.flowmetadataservice.FlowMetadataService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.flowservice.MeandreServerProxy;
import org.imirsel.nema.model.Component;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;

import com.hp.hpl.jena.rdf.model.Resource;


/**This class encapsulated all the flow related services and provides
 * an injectable implementation of the FlowMetadataService to the flow service
 * 
 * @author kumaramit01
 * @since 0.5.0
 *
 */
public class FlowMetadataServiceImpl implements FlowMetadataService {
	public MeandreServerProxy meandreServerProxy;
	private Repository repository;
	
	private static final Logger LOGGER= Logger.getLogger(FlowMetadataService.class.getName());

	
	public MeandreServerProxy getMeandreServerProxy() {
		return meandreServerProxy;
	}

	public void setMeandreServerProxy(MeandreServerProxy meandreServerProxy) {
		this.meandreServerProxy = meandreServerProxy;
	}
	
	
	/**For the given flowUrl return the list of components urls that make the flow.
	 * 
	 * @param flowUrl
	 * @return List<Component> The list of components
	 * @throws TransmissionException 
	 */
	public List<Component> getComponents(String flowUri) throws MeandreServerException{
		Map<String, FlowDescription> map=null;
		map = meandreServerProxy.getAvailableFlowDescriptionsMap();
		if(map==null){
			LOGGER.severe("Could not find components for the flowUri: "+ flowUri);
			return null;
		}
		FlowDescription fdesc = map.get(flowUri);
		if(fdesc==null){
			LOGGER.severe("Could not find components for the flowUri: "+ flowUri);
			return null;
		}
		Set<ExecutableComponentInstanceDescription> se = fdesc.getExecutableComponentInstances();
		ArrayList<Component> list = new ArrayList<Component>(se.size());
		Iterator<ExecutableComponentInstanceDescription> ite = se.iterator();
		while(ite.hasNext()){
			ExecutableComponentInstanceDescription ecd = ite.next();
			String uri=ecd.getExecutableComponent().getURI();
			String instanceUri=ecd.getExecutableComponentInstance().getURI();
			Component component = new Component();
			component.setUri(uri);
			component.setInstanceUri(instanceUri);
			component.setName(ecd.getName());
			component.setDescription(ecd.getDescription());
			component.setHidden(isHiddenComponentForFlow(flowUri,instanceUri));
			list.add(component);
		}
		return list;
	}
	
	
	/**
	 *  //TODO:
	 * 
	 * @param flowUri
	 * @param componentInstanceUri
	 * @return
	 */
	private boolean isHiddenComponentForFlow(String flowUri, String componentInstanceUri){
			if(componentInstanceUri.indexOf("fork")!=-1 || 
		     componentInstanceUri.indexOf("printobject")!=-1 ||
			 componentInstanceUri.indexOf("runbinary") !=-1|| 
			 componentInstanceUri.indexOf("StructuralSegmentationEvaluator".toLowerCase())!=-1	||
			 componentInstanceUri.indexOf("TrainTestGateKeeper".toLowerCase())!=-1	||
			 componentInstanceUri.indexOf("ClassificationEvaluator".toLowerCase())!=-1||	
			 componentInstanceUri.indexOf("ClassificationEvaluationsAggregator".toLowerCase())!=-1	
			){
			return true;
		}
		return false;
	}
	
	private String[] getFlowUriList() throws TransmissionException{
		Set<Resource> resources = meandreServerProxy.getAvailableFlows();
		
		if(resources==null){
			LOGGER.warning("Did not find any flow resources.");
			return null;
		}
		LOGGER.info("Number of flows found: " + resources.size());
		int size = resources.size();
		if(size==0){
			return null;
		}
		String[] flowList = new String[size];
		int count=0;
		Iterator<Resource> itr=resources.iterator();
		while(itr.hasNext()){
			Resource resource=itr.next();
			flowList[count]= resource.getURI();
			count++;
		}
		return flowList;
	}

	/**This method takes parameter map with custom properties that
	 * the user has set based on a template flow and creates
	 * a new flow.
	 * @returns URI of the new flow.
	 * @throws CorruptedFlowException 
	 * @throws MeandreCommunicationException 
	 * @throws CorruptedFlowException 
	 */
	public synchronized String createNewFlow(HashMap<String, String> paramMap,  String flowUri) throws MeandreServerException, CorruptedFlowException {
		WBFlowDescription flowDesc=this.getRepository().retrieveFlowDescriptor(flowUri);
		String name = flowDesc.getName();
		name = name + System.currentTimeMillis();
		flowDesc.setName(name);
        flowDesc.setDescription("Derived from " +flowUri);
		flowDesc.setRights("owned by user");
		flowDesc.setCreationDate(new Date());
		flowDesc.updateParameters(flowUri,paramMap);
        boolean uploadSuccess=this.repository.uploadFlow(flowDesc, false);
        LOGGER.info("upload was "+ uploadSuccess);
	   return flowDesc.getFlowURI();
	}
	
	
	/**This method removes a flow from meandre
	 * @throws MeandreServerException 
	 * 
	 * @returns success
	 */
	public boolean removeFlow(String uri) throws MeandreServerException{
		boolean result=this.meandreServerProxy.removeResource(uri);
		return result;
	}

	

	public String getConsole(String uri) throws MeandreServerException {
		String text=this.meandreServerProxy.getConsole(uri);
		return text;
	}
	

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}




}

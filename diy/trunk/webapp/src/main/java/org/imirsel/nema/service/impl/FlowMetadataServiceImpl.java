package org.imirsel.nema.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
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
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.service.FlowMetadataService;
import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.PropertiesDescriptionDefinition;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.repository.RepositoryImpl;
import org.meandre.core.store.Store;
import org.meandre.webapp.CorruptedFlowException;
import org.meandre.webapp.MeandreCommunicationException;
import org.meandre.webapp.Repository;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFWriter;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**This class encapsulated all the flow related services and provides
 * an injectable implementation of the FlowMetadataService to the middleware
 * 
 * @author Amit Kumar
 *
 */
public class FlowMetadataServiceImpl implements FlowMetadataService {
	public MeandreProxyWrapper meandreProxyWrapper;
	private Repository repository;
	
	private static final Logger log= Logger.getLogger(FlowMetadataService.class.getName());

	
	public MeandreProxyWrapper getMeandreProxyWrapper() {
		return meandreProxyWrapper;
	}

	public void setMeandreProxyWrapper(MeandreProxyWrapper meandreProxyWrapper) {
		this.meandreProxyWrapper = meandreProxyWrapper;
	}
	
	
	/**For the given flowUrl return the list of components urls that make the flow.
	 * 
	 * @param flowUrl
	 * @return
	 * @throws TransmissionException 
	 */
	public List<Component> getComponents(String flowUri) throws TransmissionException{
		Map<String, FlowDescription> map=null;
			map = meandreProxyWrapper.getAvailableFlowDescriptionsMap();
		
		if(map==null){
			log.severe("Could not find components for the flowUri: "+ flowUri);
			return null;
		}
		FlowDescription fdesc = map.get(flowUri);
		if(fdesc==null){
			log.severe("Could not find components for the flowUri: "+ flowUri);
			return null;
		}
		Set<ExecutableComponentInstanceDescription> se = fdesc.getExecutableComponentInstances();
		
		ArrayList<Component> list = new ArrayList<Component>(se.size());
		int count=0;
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
			count++;
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
			if(componentInstanceUri.indexOf("fork")!=-1 || componentInstanceUri.indexOf("printobject")!=-1
			|| componentInstanceUri.indexOf("runbinary") !=-1		
			){
			return true;
		}
		return false;
	}
	
	private String[] getFlowUriList() throws TransmissionException{
		Set<Resource> resources = null;
		resources = meandreProxyWrapper.getAvailableFlows();
		
		if(resources==null){
			log.warning("Did not find any flow resources.");
			return null;
		}
		log.info("Number of flows found: " + resources.size());
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
	 *  Returns the uri of the new flow.
	 * @throws MeandreCommunicationException 
	 * @throws CorruptedFlowException 
	 */
	public synchronized String createNewFlow(HashMap<String, String> paramMap,  String flowUri) throws MeandreCommunicationException, CorruptedFlowException {
		WBFlowDescription flowDesc=this.getRepository().retrieveFlowDescriptor(flowUri);
		String name = flowDesc.getName();
		name = name + System.currentTimeMillis();
		flowDesc.setName(name);
        flowDesc.setDescription("Derived from " +flowUri);
		flowDesc.setRights("owned by user");
		flowDesc.setCreationDate(new Date());
		flowDesc.updateParameters(flowUri,paramMap);
        boolean uploadSuccess=this.repository.uploadFlow(flowDesc, false);
	    System.out.println("upload was "+ uploadSuccess);
	   return flowDesc.getFlowURI();
	}
	
	
	public synchronized boolean removeFlow(String uri) throws TransmissionException{
		boolean result=this.meandreProxyWrapper.removeResource(uri);
		return result;
	}


	private String getPropertyPrefix(String uri) {
			int index = uri.lastIndexOf("/");
			int second = uri.substring(0, index).lastIndexOf("/");
			String cname=uri.substring(second+1,index);
			String count = uri.substring(index+1);
			return cname+"_"+count;
	}

	private Model getEmptyModel() {
			Model model= ModelFactory.createDefaultModel();
			model.setNsPrefix("meandre", Store.MEANDRE_ONTOLOGY_BASE_URL );
			model.setNsPrefix("xsd", XSD.getURI());
			model.setNsPrefix("rdf", RDF.getURI());
			model.setNsPrefix("rdfs",RDFS.getURI());
			model.setNsPrefix("dc",DC.getURI());
			return model;
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}



}

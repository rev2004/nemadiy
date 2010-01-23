package org.imirsel.nema.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.Constants;
import org.imirsel.nema.annotatons.parser.beans.DataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.StringDataTypeBean;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.renderers.CollectionRenderer;
import org.imirsel.nema.repository.NEMADataset;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.RepositoryClientInterface;
import org.imirsel.nema.service.ComponentMetadataService;

import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.PropertiesDescription;
import org.meandre.core.repository.PropertiesDescriptionDefinition;
import org.meandre.core.repository.QueryableRepository;
import org.meandre.core.store.Store;

import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**This service implementation provides component metadata definition
 * along with the datatype information for the various component properties
 * that make the flow.
 * 
 * @author Amit Kumar
 *
 */
public class ComponentMetadataServiceImpl implements ComponentMetadataService {
	
	private static final String DATATYPE_KEY="DATATYPE";
	private static final Logger log = Logger.getLogger(ComponentMetadataService.class.getName());
	private MeandreProxyWrapper meandreProxyWrapper;
	private XStream xstream;
	private RepositoryClientConnectionPool repositoryClientConnectionPool;
	
    
	public ComponentMetadataServiceImpl(){
		 xstream = new XStream(new JettisonMappedXmlDriver());
		 xstream.setMode(XStream.NO_REFERENCES);
	}


	public RepositoryClientInterface getRepositoryClient() {
		return repositoryClientConnectionPool.getFromPool();
	}





	public void setMeandreProxyWrapper(MeandreProxyWrapper meandreProxyWrapper) {
		this.meandreProxyWrapper = meandreProxyWrapper;
	}


	public RepositoryClientConnectionPool getRepositoryClientConnectionPool() {
		return repositoryClientConnectionPool;
	}


	public void setRepositoryClientConnectionPool(
			RepositoryClientConnectionPool repositoryClientConnectionPool) {
		this.repositoryClientConnectionPool = repositoryClientConnectionPool;
	}


	public Map<String, Property> getComponentPropertyDataType(Component component, String flowUri) throws TransmissionException, SQLException {
		RepositoryClientInterface rpi=this.getRepositoryClient();
		List<NEMADataset> nemaDatasets = rpi.getDatasets();
		QueryableRepository qp= meandreProxyWrapper.getRepository();
		Model model =getEmptyModel();
		ExecutableComponentDescription ecd=qp.getExecutableComponentDescription(model.createResource(component.getUri()));
		FlowDescription fd=qp.getFlowDescription(model.createResource(flowUri));
		
	
		if(ecd==null){
			log.severe("component: " + component.getUri()+ " could not be found.");
			repositoryClientConnectionPool.returnToPool(rpi);
			return null;
		}
		Model m = this.getEmptyModel();
		ExecutableComponentInstanceDescription ecid = fd.getExecutableComponentInstanceDescription(m.createResource(component.getInstanceUri()));
		
		PropertiesDescriptionDefinition propertiesDefn=ecd.getProperties();
		Set<String> propertiesSet=propertiesDefn.getKeys();
		Iterator<String> it = propertiesSet.iterator();
		Map<String,Property> dataTypeMap = new HashMap<String,Property>();
		boolean foundDataType=Boolean.FALSE;
		while(it.hasNext()){
			String propertyName = it.next();
			Property property = new Property();
			property.setName(propertyName);
			
			Map<String,String>otherPropertyMap =propertiesDefn.getOtherProperties(propertyName);
			String description = propertiesDefn.getDescription(propertyName);
			String defaultValue=propertiesDefn.getValue(propertyName);
			String value =ecid.getProperties().getValue(propertyName);
			if(value!=null){
				property.setValue(value);
			}else{
				property.setValue(defaultValue);
			}
			property.setDefaultValue((Object)defaultValue);
			property.setDescription(description);
			List<DataTypeBean> dataTypes = null;
			if(!otherPropertyMap.isEmpty()){
				Iterator<String> it1 = otherPropertyMap.keySet().iterator();
				while(it1.hasNext()){
					String key = it1.next();
					if(key.endsWith(propertyName+DATATYPE_KEY)){
						String value1 =otherPropertyMap.get(key);
						dataTypes = getDataTypeBeanFromJson(value1);
						updatePropertyWithCollectionMetadata(property,dataTypes);
						property.setDataTypeBeanList(dataTypes);
						foundDataType= true;
					}
				}
				// add the default data type
				if(!foundDataType){
					dataTypes = getDefaultDataTypeBean();
					property.setDataTypeBeanList(dataTypes);
				}
			}else{
				if(!foundDataType){
					dataTypes = getDefaultDataTypeBean();
					property.setDataTypeBeanList(dataTypes);
				}
			}
			
			dataTypeMap.put(propertyName,property);
			// reset to false for the next property
			foundDataType=Boolean.FALSE;
		}
		repositoryClientConnectionPool.returnToPool(rpi);
		return dataTypeMap;
	}

	private void updatePropertyWithCollectionMetadata(Property property,
			List<DataTypeBean> dataTypes) {
			for(DataTypeBean dtb:dataTypes){
				if(dtb.getRenderer().equals(CollectionRenderer.class.getName())){
					// this is a collection
					ArrayList<String> labelList = new ArrayList<String>();
					ArrayList<Object> valueList = new ArrayList<Object>();
					RepositoryClientInterface rpi=this.getRepositoryClient();
					try {
						List<NEMADataset> ltb=rpi.getDatasets();
						for(NEMADataset dataset:ltb){
							String label=dataset.getName();
							int value=dataset.getId();
							labelList.add(label);
							valueList.add(value);
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}finally{
						this.getRepositoryClientConnectionPool().returnToPool(rpi);
					}
					
					if(labelList.size()>0){
						property.setEnumeratedValueList(labelList);
						property.setEnumneratedLabelList(valueList);
					}
					
					
				}
			}
		
	}


	private List<DataTypeBean> getDefaultDataTypeBean() {
		List<DataTypeBean> list = new ArrayList<DataTypeBean>();
		list.add(new StringDataTypeBean());
		return list;
	}


	@SuppressWarnings("unchecked")
	private List<DataTypeBean> getDataTypeBeanFromJson(String value) {
		List<DataTypeBean> list=(List<DataTypeBean>)xstream.fromXML(value);
       return list;
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


}

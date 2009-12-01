package org.imirsel.nema.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.annotatons.parser.beans.DataTypeBean;
import org.imirsel.nema.annotatons.parser.beans.StringDataTypeBean;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Property;
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
	
	private static final Logger log = Logger.getLogger(ComponentMetadataService.class.getName());
	private MeandreProxyWrapper meandreProxyWrapper;
	private XStream xstream;
   
	
	public ComponentMetadataServiceImpl(){
		 xstream = new XStream(new JettisonMappedXmlDriver());
		 xstream.setMode(XStream.NO_REFERENCES);
	}
   


	public void setMeandreProxyWrapper(MeandreProxyWrapper meandreProxyWrapper) {
		this.meandreProxyWrapper = meandreProxyWrapper;
	}

	public void dd() throws TransmissionException{
		QueryableRepository qp= meandreProxyWrapper.getRepository();
		Model model =getEmptyModel();
		String flow_uri="http://test.org/datatypetest/";
		FlowDescription fd=qp.getFlowDescription(model.createResource(flow_uri));
		List<ExecutableComponentInstanceDescription> flowComponents=fd.getExecutableComponentInstancesOrderedByName();
		Iterator<ExecutableComponentInstanceDescription> it =  flowComponents.iterator();
		
		
		System.out.println("List of components making this particular flow: ");
		while(it.hasNext()){
			ExecutableComponentInstanceDescription ecid = it.next();
			ExecutableComponentDescription ecd = qp.getAvailableExecutableComponentDescriptionsMap().get(ecid.getExecutableComponent().getURI());
			System.out.println(ecid.getExecutableComponent().getURI()+"--"+ecid.getExecutableComponentInstance().getURI()+"\n\n");
			
			PropertiesDescription props =  ecd.getProperties();
			
			 for(String key:props.getKeys()){
				 String val = ecid.getProperties().getValue(key);
				 System.out.println(key + ": 1 " +val);
				 System.out.println(key + ": 2 " + props.getValue(key));
				
			 }
			 
		}
		
		
		
		Model m = this.getEmptyModel();
	
		ExecutableComponentInstanceDescription e = fd.getExecutableComponentInstanceDescription(m.createResource("http://test.org/datatypetest/instance/datatypetestcomponent/1"));
		
		System.out.println("name: " + e.getName());
		System.out.println("==>"+e.getProperties().getValue("SampleRate"));
		
		
		
	}

	public HashMap<String, Property> getComponentPropertyDataType(Component component, String flowUri) throws TransmissionException {
		QueryableRepository qp= meandreProxyWrapper.getRepository();
		Model model =getEmptyModel();
		ExecutableComponentDescription ecd=qp.getExecutableComponentDescription(model.createResource(component.getUri()));
		FlowDescription fd=qp.getFlowDescription(model.createResource(flowUri));
		
	
		if(ecd==null){
			log.severe("component: " + component.getUri()+ " could not be found.");
			return null;
		}
		Model m = this.getEmptyModel();
		ExecutableComponentInstanceDescription ecid = fd.getExecutableComponentInstanceDescription(m.createResource(component.getInstanceUri()));
		
		PropertiesDescriptionDefinition propertiesDefn=ecd.getProperties();
		Set<String> propertiesSet=propertiesDefn.getKeys();
		Iterator<String> it = propertiesSet.iterator();
		HashMap<String,Property> dataTypeMap = new HashMap<String,Property>();
		boolean foundDataType=Boolean.FALSE;
		while(it.hasNext()){
			String propertyName = it.next();
			Property property = new Property();
			property.setName(propertyName);
			
			Map<String,String>otherPropertyMap =propertiesDefn.getOtherProperties(propertyName);
			String description = propertiesDefn.getDescription(propertyName);
			String defaultValue=propertiesDefn.getValue(propertyName);
			String value =ecid.getProperties().getValue(propertyName);
			System.out.println(propertyName+ " "+"value: " + value + " defaultValue: " + defaultValue);
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
					if(key.endsWith(propertyName+"DATATYPE")){
						String value1 =otherPropertyMap.get(key);
						dataTypes = getDataTypeBeanFromJson(value1);
						property.setDataTypeBeanList(dataTypes);
						foundDataType= true;
					}
				}
				
				if(!foundDataType){
					// add the default data type?
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
		
		
		return dataTypeMap;
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

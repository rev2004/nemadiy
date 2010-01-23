package org.imirsel.repository;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import org.imirsel.demo.components.PrintObjectComponentHack;
import org.meandre.core.repository.ConnectorDescription;
import org.meandre.core.repository.CorruptedDescriptionException;
import org.meandre.core.repository.DataPortDescription;
import org.meandre.core.repository.ExecutableComponentDescription;
import org.meandre.core.repository.ExecutableComponentInstanceDescription;
import org.meandre.core.repository.FlowDescription;
import org.meandre.core.repository.PropertiesDescription;
import org.meandre.core.repository.PropertiesDescriptionDefinition;
import org.meandre.core.repository.TagsDescription;
import org.meandre.demo.components.ConcatenateStringsComponent;

import org.meandre.demo.components.PushStringComponent;
import org.meandre.demo.repository.DemoRepositoryGenerator;


import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.ibm.icu.text.SimpleDateFormat;

public class NEMADemoRepository extends DemoRepositoryGenerator {
	
private static Date baseDate;

/** The simple date formater */
private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	
	static {
		try {
			baseDate = sdf.parse("2009-10-05 21:06:03");
		} catch (ParseException e) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			e.printStackTrace(new PrintStream(baos));
			log.warning("Could not create date for internal demo components\n"+baos.toString());
		}
	}
	

	public static Model getTestHelloWorldWithSleepRepository(String sBaseURL) {
		///
		// Create the components
		//
		ExecutableComponentDescription ecdPS = getPushStringComponent(sBaseURL); 
		ExecutableComponentDescription ecdCS = getConcatenateStringsComponent(sBaseURL); 
		ExecutableComponentDescription ecdPO = getPrintObjectComponent(sBaseURL); 
		
		sBaseURL += "/flow/test-hello-world-sleep";
		
		//
		// Assemble the flow
		//
		Resource resFlowComponent = ModelFactory.createDefaultModel().createResource(sBaseURL);
		
		//
		// Flow properties
		//
		String sName = "Hello World!!!";
		String sDescription = "A simple hello world test";
		String sRights = "University of Illinois/NCSA open source license";
		String sCreator = "Xavier Llor&agrave;";
		Date dateCreation = baseDate;
		
		//
		// Create the instances
		//
		Set<ExecutableComponentInstanceDescription> setExecutableComponentInstances = new HashSet<ExecutableComponentInstanceDescription>();

		Resource resInsPS0 = ModelFactory.createDefaultModel().createResource(sBaseURL+"instance/push-string/0");
		Resource resInsPS0Component = ecdPS.getExecutableComponent();
		String sInsPS0Name = "Push String 0";
		String sInsPS0Desc = "Push hello world";
		PropertiesDescription pdPInsPS0Properties = new PropertiesDescription();
		ExecutableComponentInstanceDescription ecidPS0 = new ExecutableComponentInstanceDescription(resInsPS0,resInsPS0Component,sInsPS0Name,sInsPS0Desc,pdPInsPS0Properties);

		Resource resInsPS1 = ModelFactory.createDefaultModel().createResource(sBaseURL+"instance/push-string/1");
		Resource resInsPS1Component = ecdPS.getExecutableComponent();
		String sInsPS1Name = "Push String 1";
		String sInsPS1Desc = "Push hello world";
		PropertiesDescription pdPInsPS1Properties = new PropertiesDescription();
		ExecutableComponentInstanceDescription ecidPS1 = new ExecutableComponentInstanceDescription(resInsPS1,resInsPS1Component,sInsPS1Name,sInsPS1Desc,pdPInsPS1Properties);

		Resource resInsCS0 = ModelFactory.createDefaultModel().createResource(sBaseURL+"instance/concatenate_string/2");
		Resource resInsCS0Component = ecdCS.getExecutableComponent();
		String sInsCS0Name = "Concatenate String 0";
		String sInsCS0Desc = "Concatenates two strings";
		PropertiesDescription pdPInsCS0Properties = new PropertiesDescription();
		ExecutableComponentInstanceDescription ecidCS0 = new ExecutableComponentInstanceDescription(resInsCS0,resInsCS0Component,sInsCS0Name,sInsCS0Desc,pdPInsCS0Properties);

		Resource resInsPO0 = ModelFactory.createDefaultModel().createResource(sBaseURL+"instance/print-object/3");
		Resource resInsPO0Component = ecdPO.getExecutableComponent();
		String sInsPO0Name = "Print Object 0";
		String sInsPO0Desc = "Prints the concatenated object";
		PropertiesDescription pdPInsPO0Properties = new PropertiesDescription();
		ExecutableComponentInstanceDescription ecidPO0 = new ExecutableComponentInstanceDescription(resInsPO0,resInsPO0Component,sInsPO0Name,sInsPO0Desc,pdPInsPO0Properties);

		setExecutableComponentInstances.add(ecidPS0);
		setExecutableComponentInstances.add(ecidPS1);
		setExecutableComponentInstances.add(ecidCS0);
		setExecutableComponentInstances.add(ecidPO0);
		
		//
		// Connecting the instances
		//
		Set<ConnectorDescription> setConnectorDescription = new HashSet<ConnectorDescription>();
		
		Iterator<DataPortDescription> iter = ecdCS.getInputs().iterator();
		Resource resOne = iter.next().getResource();
		Resource resTwo = iter.next().getResource();
		
		ConnectorDescription cdPS0 = new ConnectorDescription(
				ModelFactory.createDefaultModel().createResource(sBaseURL+"connector/0"),
				ecidPS0.getExecutableComponentInstance(), ecdPS.getOutputs().iterator().next().getResource(),
				ecidCS0.getExecutableComponentInstance(), resOne);

		ConnectorDescription cdPS1 = new ConnectorDescription(
				ModelFactory.createDefaultModel().createResource(sBaseURL+"connector/1"),
				ecidPS1.getExecutableComponentInstance(), ecdPS.getOutputs().iterator().next().getResource(),
				ecidCS0.getExecutableComponentInstance(), resTwo);
		
		ConnectorDescription cdPO0 = new ConnectorDescription(
				ModelFactory.createDefaultModel().createResource(sBaseURL+"connector/2"),
				ecidCS0.getExecutableComponentInstance(), ecdCS.getOutputs().iterator().next().getResource(),
				ecidPO0.getExecutableComponentInstance(), ecdPO.getInputs().iterator().next().getResource());

		setConnectorDescription.add(cdPS0);
		setConnectorDescription.add(cdPS1);
		setConnectorDescription.add(cdPO0);
		
		//
		// Tags
		//
		HashSet<String> hsTags = new HashSet<String>();
		hsTags.add("demo");
		hsTags.add("hello_world");
 		TagsDescription tagsDesc = new TagsDescription(hsTags);
		
 		//
 		// Create the flow
 		//
		FlowDescription fd = new FlowDescription(resFlowComponent, sName,
				sDescription, sRights, sCreator, dateCreation,
				setExecutableComponentInstances, setConnectorDescription,
				tagsDesc);
		
		//
		// Return the aggregated model
		//
		return ecdPS.getModel().add(ecdCS.getModel())
                               .add(ecdPO.getModel())
                               .add(fd.getModel());
	}
	
	
	/** Create the description for org.meandre.demo.components.PushStringComponent.
	 * 
	 * @param sBaseURL The base URL
	 * @return The executable component description
	 */
	private static ExecutableComponentDescription getPushStringComponent(String sBaseURL) {
		
		sBaseURL += "/component/";
		
		ExecutableComponentDescription ecdRes = null;
		
		Resource resExecutableComponent =  ModelFactory.createDefaultModel().createResource(sBaseURL+"push-string");
		
		// General properties
		String sName = "Push String";
		String sDescription = "Pushes the string stored into the properties to the output";
		String sRights = "University of Illinois/NCSA open source license";
		String sCreator = "Xavier Llor&agrave;";
		Date dateCreation = baseDate;
		
		// Context
		Set<RDFNode> setContext = new HashSet<RDFNode>();
		setContext.add(ModelFactory.createDefaultModel().createResource(sBaseURL));
		
		// Location
		Resource resLocation = ModelFactory.createDefaultModel().createResource(sBaseURL+PushStringComponent.class.getName());
		
		// Empty input ports
		Set<DataPortDescription> setInputs = new HashSet<DataPortDescription>();
		
		// One output port
		Set<DataPortDescription> setOutputs = new HashSet<DataPortDescription>();
		Resource resDPDOutput = ModelFactory.createDefaultModel().createResource(resExecutableComponent.toString()+"/output/string");
		String sDPDIdent = resDPDOutput.toString(); 
		String sDPDName = "string";
		String sDPDDesc = "The string being pushed";
		try {
			setOutputs.add(new DataPortDescription(resDPDOutput,sDPDIdent,sDPDName,sDPDDesc));
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		// Properties
		Hashtable<String,String> htValues = new Hashtable<String,String>();
		Hashtable<String,String> htDescriptions = new Hashtable<String,String>();
		htValues.put("message", "Hello World!!! Happy Meandring!!!");
		htDescriptions.put("message", "The string message to be pushed trough the output port");
		htValues.put("times", "1");
		htDescriptions.put("times", "Number of time to push the string");
		PropertiesDescriptionDefinition pddProperties = new PropertiesDescriptionDefinition(htValues,htDescriptions);
		
		// Tags
		HashSet<String> hsTags = new HashSet<String>();
		hsTags.add("demo");
		hsTags.add("string");
		hsTags.add("hello_world");
 		TagsDescription tagDesc = new TagsDescription(hsTags);
		
		String sRunnable = "java";
		String sFiringPolicy = "all";
		String sFormat = "java/class";
		
		try {
			ecdRes = new ExecutableComponentDescription(resExecutableComponent,
					sName, sDescription, sRights, sCreator, dateCreation,
					sRunnable, sFiringPolicy, sFormat, setContext, resLocation,
					setInputs, setOutputs, pddProperties, tagDesc,ExecutableComponentDescription.COMPUTE_COMPONENT);
			
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		return ecdRes;
	}
	
	
	/** Create the description for org.meandre.demo.components.ConcatenateStringsComponent.
	 * 
	 * @param sBaseURL The base URL
	 * @return The executable component description
	 */
	private static ExecutableComponentDescription getConcatenateStringsComponent(String sBaseURL) {
		
		sBaseURL += "/component/";
		
		ExecutableComponentDescription ecdRes = null;
		
		Resource resExecutableComponent =  ModelFactory.createDefaultModel().createResource(sBaseURL+"concatenate-strings");
		
		// General properties
		String sName = "Concatenate Strings";
		String sDescription = "Concatenates the to input string to the output";
		String sRights = "University of Illinois/NCSA open source license";
		String sCreator = "Xavier Llor&agrave;";
		Date dateCreation = baseDate;
		
		// Context
		Set<RDFNode> setContext = new HashSet<RDFNode>();
		setContext.add(ModelFactory.createDefaultModel().createResource(sBaseURL));
		
		// Location
		Resource resLocation = ModelFactory.createDefaultModel().createResource(sBaseURL+ConcatenateStringsComponent.class.getName());
		
		// Empty input ports
		Set<DataPortDescription> setInputs = new HashSet<DataPortDescription>();
		Resource resDPDInput1 = ModelFactory.createDefaultModel().createResource(resExecutableComponent.toString()+"/input/string_one");
		String sDPDIn1Ident = resDPDInput1.toString(); 
		String sDPDIn1Name = "string_one";
		String sDPDIn1Desc = "The first string to concatenate";
		try {
			setInputs.add(new DataPortDescription(resDPDInput1,sDPDIn1Ident,sDPDIn1Name,sDPDIn1Desc));
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		Resource resDPDInput2 = ModelFactory.createDefaultModel().createResource(resExecutableComponent.toString()+"/input/string_two");
		String sDPDIn2Ident = resDPDInput2.toString(); 
		String sDPDIn2Name = "string_two";
		String sDPDIn2Desc = "The second string to concatenate";
		try {
			setInputs.add(new DataPortDescription(resDPDInput2,sDPDIn2Ident,sDPDIn2Name,sDPDIn2Desc));
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		// One output port
		Set<DataPortDescription> setOutputs = new HashSet<DataPortDescription>();
		Resource resDPDOutput = ModelFactory.createDefaultModel().createResource(resExecutableComponent.toString()+"/output/concatenated_string");
		String sDPDIdent = resDPDOutput.toString(); 
		String sDPDName = "concatenated_string";
		String sDPDDesc = "The concatenated string";
		try {
			setOutputs.add(new DataPortDescription(resDPDOutput,sDPDIdent,sDPDName,sDPDDesc));
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		// Properties
		Hashtable<String,String> htValues = new Hashtable<String,String>();
		Hashtable<String,String> htDescriptions = new Hashtable<String,String>();
		PropertiesDescriptionDefinition pddProperties = new PropertiesDescriptionDefinition(htValues,htDescriptions);
		
		// Tags
		HashSet<String> hsTags = new HashSet<String>();
		hsTags.add("demo");
		hsTags.add("string");
		hsTags.add("concatenate");
 		TagsDescription tagDesc = new TagsDescription(hsTags);
		
		String sRunnable = "java";
		String sFiringPolicy = "all";
		String sFormat = "java/class";
		
		try {
			ecdRes = new ExecutableComponentDescription(resExecutableComponent,
					sName, sDescription, sRights, sCreator, dateCreation,
					sRunnable, sFiringPolicy, sFormat, setContext, resLocation,
					setInputs, setOutputs, pddProperties, tagDesc,ExecutableComponentDescription.COMPUTE_COMPONENT);
			
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		return ecdRes;
	}
	
	

	/** Create the description for org.meandre.demo.components.PushString.
	 * 
	 * @param sBaseURL
	 * @return The executable component description
	 */
	private static ExecutableComponentDescription getPrintObjectComponent(String sBaseURL) {
		sBaseURL += "/component/";
		ExecutableComponentDescription ecdRes = null;
		
		Resource resExecutableComponent =  ModelFactory.createDefaultModel().createResource(sBaseURL+"print-object");
		
		// General properties
		String sName = "Print Object";
		String sDescription = "Prints the object in the input to the standard output";
		String sRights = "University of Illinois/NCSA open source license";
		String sCreator = "Xavier Llor&agrave;";
		Date dateCreation = baseDate;
		
		// Context
		Set<RDFNode> setContext = new HashSet<RDFNode>();
		setContext.add(ModelFactory.createDefaultModel().createResource(sBaseURL));
		
		// Location
		Resource resLocation = ModelFactory.createDefaultModel().createResource(sBaseURL+PrintObjectComponentHack.class.getName());
		
		// Empty input ports
		Set<DataPortDescription> setInputs = new HashSet<DataPortDescription>();
		Resource resDPDInput = ModelFactory.createDefaultModel().createResource(resExecutableComponent.toString()+"/input/object");
		String sDPDInIdent = resDPDInput.toString(); 
		String sDPDInName = "object";
		String sDPDInDesc = "The object to print";
		try {
			setInputs.add(new DataPortDescription(resDPDInput,sDPDInIdent,sDPDInName,sDPDInDesc));
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		// One output port
		Set<DataPortDescription> setOutputs = new HashSet<DataPortDescription>();
		
		// Properties
		Hashtable<String,String> htValues = new Hashtable<String,String>();
		Hashtable<String,String> htDescriptions = new Hashtable<String,String>();
		htValues.put("count", "true"); 
		htDescriptions.put("count", "If set to true prints the count of printed objects");
		PropertiesDescriptionDefinition pddProperties = new PropertiesDescriptionDefinition(htValues,htDescriptions);
		
		// Tags
		HashSet<String> hsTags = new HashSet<String>();
		hsTags.add("demo");
		hsTags.add("object");
		hsTags.add("print");
		TagsDescription tagDesc = new TagsDescription(hsTags);
		
		String sRunnable = "java";
		String sFiringPolicy = "all";
		String sFormat = "java/class";
		
		try {
			ecdRes = new ExecutableComponentDescription(resExecutableComponent,
					sName, sDescription, sRights, sCreator, dateCreation,
					sRunnable, sFiringPolicy, sFormat, setContext, resLocation,
					setInputs, setOutputs, pddProperties, tagDesc, ExecutableComponentDescription.COMPUTE_COMPONENT);
			
		} catch (CorruptedDescriptionException e) {
			log.severe("An exception should have not been trown: "+e);
		}
		
		return ecdRes;
	}


}

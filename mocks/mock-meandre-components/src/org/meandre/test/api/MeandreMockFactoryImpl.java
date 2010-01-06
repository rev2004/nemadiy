/*
 * @(#) MeandreMockFactoryImpl.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.api;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Logger;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ExecutableComponent;

import static org.meandre.test.matchers.Meandre.*;
import static org.meandre.test.actions.Meandre.*;


/**JMock implementation
 * Factory Methods for implementing the ComponentContext and ComponentContextProperites
 * 
 * @author Amit Kumar
 * Created on Mar 7, 2009 6:05:39 AM
 *
 */
public final class MeandreMockFactoryImpl extends BaseMeandreMockFactory implements MeandreMockFactory {

	private Mockery mockery = new Mockery();
	private static final MeandreMockFactory INSTANCE = new MeandreMockFactoryImpl();
	

	
	private MeandreMockFactoryImpl(){
		super();
	}
	
	public static MeandreMockFactory getInstance(){
		return INSTANCE;
	}


	/**Returns ComponentContextProperties mock object.
	 * This object implements all the functions except the ones
	 * related to the WebUI 
     * @param String name -to create unique ComponentContextProperties object
     * @param Class ComponentClass
     * @return ComponentContextProperties
	 */
	public ComponentContextProperties getComponentContextProperties(final String name, final Class<? extends ExecutableComponent> componentClass) throws ComponentContextException {
		final String[] inputNames = getInputNames(componentClass);
		final String[] outputNames = getOutputNames(componentClass);
		final String[] propertyNames = getPropertyNames(componentClass);
		final HashMap<String,String> propertyDefaultValues = getPropertyDefaultValues(componentClass);
		
		final ComponentContextProperties componentContextProperties =mockery.mock(ComponentContextProperties.class,name);
			
			mockery.checking(new Expectations() {
			{
			allowing(componentContextProperties).getLogger(); will(returnValue(Logger.getAnonymousLogger()));
			allowing(componentContextProperties).getInputNames(); will(returnValue(inputNames));
			allowing(componentContextProperties).getOutputNames(); will(returnValue(outputNames));
			allowing(componentContextProperties).getPropertyNames(); will(returnValue(propertyNames));
			
			allowing(componentContextProperties).getProperty(with(aValidProperty(propertyNames))); will(returnPropertyValue(propertyDefaultValues));
			
			allowing(componentContextProperties).getOutputConsole();will(returnValue(System.out));
			allowing(componentContextProperties).getExecutionInstanceID(); will(returnValue(name+"execution_instance_id"));
			allowing(componentContextProperties).getFlowExecutionInstanceID(); will(returnValue(name+"flow_instance_id"));
			allowing(componentContextProperties).getFlowID(); will(returnValue(name+"flow_id"));
			
			/*not implemented*/
			never(componentContextProperties).getPlugin(with(any(String.class)));
			never(componentContextProperties).getProxyWebUIUrl(with(any(Boolean.class))); //will(returnValue("http://127.0.0.1/test"));
			
			}
				
			});
			
		return componentContextProperties;
	}

	
	
	/**This function returns a mocked ComponentContext
	 * 
	 * @param String name -to create unique ComponentContextProperties object
     * @param Class ComponentClass
     * @param Hashtable<String,Object> htInput -Input port key value hashtable
     * @param Hashtable<String,Object> htOutput -Output port key value hashtable
     * @return ComponentContext
     * 
	 */
	public ComponentContext getComponentContext(final String name,Class<? extends ExecutableComponent> componentClass,
			 final Hashtable<String, Object> htInput, final Hashtable<String, Object> htOutput) throws ComponentContextException {
		 final ComponentContext componentContext = mockery.mock(ComponentContext.class,name);
		 final String[] inputNames = getInputNames(componentClass);
		 final String[] outputNames = getOutputNames(componentClass);
		 final String[] propertyNames = getPropertyNames(componentClass);
		 final HashMap<String,String> propertyDefaultValues = getPropertyDefaultValues(componentClass);
			
			
		 
		 mockery.checking(new Expectations(){{
				allowing(componentContext).getLogger(); will(returnValue(Logger.getAnonymousLogger()));
				allowing(componentContext).getInputNames(); will(returnValue(inputNames));
				allowing(componentContext).getOutputNames(); will(returnValue(outputNames));
				allowing(componentContext).getPropertyNames(); will(returnValue(propertyNames));
				allowing(componentContext).getProperty(with(aValidProperty(propertyNames))); will(returnPropertyValue(propertyDefaultValues));
				allowing(componentContext).getOutputConsole();will(returnValue(System.out));
				allowing(componentContext).getExecutionInstanceID(); will(returnValue(name+"execution_instance_id"));
				allowing(componentContext).getFlowExecutionInstanceID(); will(returnValue(name+"flow_instance_id"));
				allowing(componentContext).getFlowID(); will(returnValue(name+"flow_id"));
				
				allowing(componentContext).getDataComponentFromInput(with(aValidInput(inputNames))); will(returnInputValue(htInput));
			  
				allowing(componentContext).pushDataComponentToOutput(with(aValidOutput(outputNames)), with(aValidOutputValue(htOutput))); will(checkOutputValue(htOutput));
			
			
			
			
			
		}});
		return componentContext;
		
	}
	
	

	/**
	 * @return the mockery
	 */
	public Mockery getMockery() {
		return mockery;
	}

}

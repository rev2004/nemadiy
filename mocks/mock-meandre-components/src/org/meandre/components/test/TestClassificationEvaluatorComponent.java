package org.meandre.components.test;


import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.imirsel.nema.components.evaluation.ClassificationEvaluatorComponent;
import org.junit.After;
import org.junit.Before;
import org.meandre.annotations.ComponentInput;
import org.meandre.annotations.ComponentOutput;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.test.api.MeandreMockFactory;
import org.meandre.test.api.MeandreMockFactoryImpl;

public class TestClassificationEvaluatorComponent {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	
	public void testComponentExecution(){
		ComponentContextProperties componentContextProperties;
		ComponentContext componentContext;


		
		ClassificationEvaluatorComponent  classificationEvaluatorComponent = new ClassificationEvaluatorComponent();
		MeandreMockFactory meandreMockFactory = MeandreMockFactoryImpl.getInstance();
		String[] fileLists= new String[]{};
		String[] gtFileName= new String[]{};
		String dirName="";
			
			
		Hashtable htInput = new Hashtable();
		htInput.put("Results List", fileLists);
		htInput.put("Ground-truth File", gtFileName);
		
		Hashtable htOutput = new Hashtable();
		htInput.put("Results Directory", dirName);
		

		
		
		try {
			componentContextProperties =  
			meandreMockFactory.getComponentContextProperties("defaultProperties",StringConcatenateComponent.class);
			componentContext = 
			meandreMockFactory.getComponentContext("defaultComponent", ClassificationEvaluatorComponent.class, htInput, htOutput);
		
			
			classificationEvaluatorComponent.initialize(componentContextProperties);
			classificationEvaluatorComponent.execute(componentContext);
			classificationEvaluatorComponent.dispose(componentContextProperties);
		} catch (ComponentExecutionException e) {
			fail(e.getMessage());
		} catch (ComponentContextException e) {
			fail(e.getMessage());
		}
	
	
	}
	
	
}

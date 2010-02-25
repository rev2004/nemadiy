/*
 * @(#) TestStringConcatenateComponent.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.components.test;


import static org.junit.Assert.fail;

import java.util.Hashtable;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.test.api.MeandreMockFactory;
import org.meandre.test.api.MeandreMockFactoryImpl;
import org.meandre.test.datatypes.MultipleUnorderedPushDataOutput;




/**Sample Test that demonstrates use of MeandreMockFactory
 * @author Amit Kumar
 * Created on Apr 6, 2009 2:25:58 PM
 * 
 */
public class TestStringToCharacterComponent {


	
	
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
			
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Test method for {@link org.meandre.components.test.StringConcatenateComponent#execute(org.meandre.core.ComponentContext)}.
	 */
	@Test
	public void testExecute() {
		ComponentContextProperties componentContextProperties;
		ComponentContext componentContext;

		final String input1="ping";
		final Character[] output1=new Character[]{'p','i','n','g'};
		MultipleUnorderedPushDataOutput<Character> mpd = new MultipleUnorderedPushDataOutput<Character>(output1);
		
		final boolean output2=true;
		// hashtable values of input
		Hashtable<String, Object> htInput = new Hashtable<String,Object>();
		htInput.put("string1", input1);
		
		//hashtable values of output
		Hashtable<String,Object> htOutput = new Hashtable<String,Object>();
		htOutput.put("singleChar", mpd);
		htOutput.put("debug", output2);
		
		StringToCharacterComponent stringToCharacterComponent = new StringToCharacterComponent();
		MeandreMockFactory meandreMockFactory = MeandreMockFactoryImpl.getInstance();
		try {
			componentContextProperties =  
			meandreMockFactory.getComponentContextProperties("defaultProperties",stringToCharacterComponent.getClass());
			componentContext = 
			meandreMockFactory.getComponentContext("defaultComponent", stringToCharacterComponent.getClass(), htInput, htOutput);
		
			
			stringToCharacterComponent.initialize(componentContextProperties);
			stringToCharacterComponent.execute(componentContext);
			stringToCharacterComponent.dispose(componentContextProperties);
		} catch (ComponentExecutionException e) {
			fail(e.getMessage());
		} catch (ComponentContextException e) {
			fail(e.getMessage());
		}
		
	}

}

package org.imirsel.nema.test;


import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.imirsel.nema.model.ProcessArtifact;
import org.imirsel.nema.test.components.remote.ExtendedRemoteComponent;
import org.imirsel.nema.test.components.remote.RemoteTimeComponent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.test.api.MeandreMockFactory;
import org.meandre.test.api.MeandreMockFactoryImpl;

public class TestRemoteTimeComponent {
	

	private ComponentContextProperties componentContextProperties;
	private ComponentContext componentContext;


	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testRemoteTimeComponent(){
		MeandreMockFactory  meandreMockFactory = MeandreMockFactoryImpl.getInstance();
		ExtendedRemoteComponent rtc = new ExtendedRemoteComponent();
		// hashtable values of input
		Hashtable<String, Object> htInput = new Hashtable<String,Object>();
	
		ProcessArtifact pa = new ProcessArtifact("/tmp/0.date","file");
		List<ProcessArtifact> list = new ArrayList<ProcessArtifact>();
		list.add(pa);
		
		//hashtable values of output
		Hashtable<String,Object> htOutput = new Hashtable<String,Object>();
		htOutput.put("processResult", list);
		
		
		try {
			componentContextProperties =  
			meandreMockFactory.getComponentContextProperties("defaultProperties",ExtendedRemoteComponent.class);
			componentContext = 
			meandreMockFactory.getComponentContext("defaultComponent", ExtendedRemoteComponent.class, htInput, htOutput);
			rtc.initialize(componentContextProperties);
			rtc.execute(componentContext);
			rtc.dispose(componentContextProperties);
		} catch (ComponentExecutionException e) {
			fail(e.getMessage());
		} catch (ComponentContextException e) {
			fail(e.getMessage());
		}
		
		
		
	}

}

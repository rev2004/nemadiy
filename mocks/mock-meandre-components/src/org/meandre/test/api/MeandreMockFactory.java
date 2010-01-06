/*
 * @(#) MeandreMockFactory.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.test.api;

import java.util.Hashtable;

import org.jmock.Mockery;
import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ExecutableComponent;

/**Abstracts mock implementations
 * 
 * @author Amit Kumar
 * Created on Mar 7, 2009 6:06:25 AM
 *
 */
public interface MeandreMockFactory {
	public ComponentContextProperties getComponentContextProperties(final String name,final Class<? extends ExecutableComponent> componentClass) throws ComponentContextException;
	public ComponentContext getComponentContext(final String name,final Class<? extends ExecutableComponent> componentClass, Hashtable<String, Object> htInput, Hashtable<String, Object> htOutput) throws ComponentContextException;
	public Mockery getMockery();
}

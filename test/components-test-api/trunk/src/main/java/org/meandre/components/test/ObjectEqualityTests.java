package org.meandre.components.test;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/** simple class to test object equality
 * 
 * @author kumaramit01
 *
 */
public class ObjectEqualityTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testStringArray(){
		String[] one = new String[]{"amit","anuj"};
		String[] two = new String[]{"amit","anuj"};
		assertTrue(Arrays.equals(one,two));
		
		Object castedOne = (Object)one;
		assertTrue(castedOne.getClass().isArray());
		Object castedTwo = (Object)two;
		assertTrue(castedTwo.getClass().isArray());
		
		assertTrue(Arrays.equals((Object[])castedOne,(Object[])castedTwo));
		
		ArrayList<String> oneA = new ArrayList<String>();
		oneA.add("amit");
		oneA.add("anuj");
		
		ArrayList<String> oneB= new ArrayList<String>();
		oneB.add("amit");
		oneB.add("anuj");
		
		assertFalse(oneA.getClass().isArray());
		assertTrue(oneA.equals(oneB));
		
		
	}

}

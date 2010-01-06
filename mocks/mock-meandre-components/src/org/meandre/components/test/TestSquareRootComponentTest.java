/*
 * @(#) TestSquareRootComponentTest.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */
package org.meandre.components.test;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoint;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;





import static org.junit.Assume.*;
import static org.junit.Assert.*;
@RunWith(Theories.class)
public class TestSquareRootComponentTest {
	
	
	
	public static @DataPoint int value1 = 30;
	public static @DataPoint int value2 =-1;
	public static @DataPoint int value3 = 20;
	
	

	@Theory public void defnOfSqrRoot(int n){
		assumeTrue(n>0);
		System.out.println("Doing---> "+ n);
		assertEquals(n,sqrRoot(n)*sqrRoot(n),0.01);
	}
	

	
	 public double sqrRoot(int n) {
		return Math.sqrt(n);
	}

	 
	 
	 @Theory public void divide(int n, int l){
			assumeTrue(l!=0);
			System.out.println("Divide "+  n + " " + l);
			assertEquals(n,(n/l*l));
		}
		

		
		

	
	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

}

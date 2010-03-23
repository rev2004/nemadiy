/**
 * 
 */
package org.imirsel.nema.model.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.imirsel.nema.model.Job;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author singh14
 *
 */
public class JobTest {
	
	private Job job; 

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		job = new Job();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link org.imirsel.nema.model.Job#getUpdateTimestamp()}.
	 */
	@Test
	public void testGetUpdateTimestamp() {
		Date dt = new Date();
		
		dt = job.getUpdateTimestamp();
		
	//	assertTrue(dt!=null);
	
		System.out.println("update time stamp : " + dt);
	}

	/**
	 * Test method for {@link org.imirsel.nema.model.Job#setUpdateTimestamp(java.util.Date)}.
	 */
//	@Test
//	public void testSetUpdateTimestamp() {
//		fail("Not yet implemented"); // TODO
//	}

}

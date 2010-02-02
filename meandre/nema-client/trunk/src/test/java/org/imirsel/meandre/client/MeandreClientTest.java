package org.imirsel.meandre.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**Test Case for running async model
 * 
 * @author kumaramit01
 *
 */
public class MeandreClientTest {

	private MeandreClient mclient=null;
	
	@Before
	public void setUp() throws Exception {
		mclient = new MeandreClient("nema.lis.uiuc.edu",11709);
		mclient.setCredentials("admin", "admin");
	}

	@After
	public void tearDown() throws Exception {
	mclient.destroy();
	}

	@Test
	public void testRunAsyncModel() {
		assertTrue(mclient!=null);
		HashMap<String,String> probeList = new HashMap<String,String>();
		probeList.put("nema", "true");
		String token = System.currentTimeMillis() + "-token";
		
		String fileName ="/Users/amitku/Desktop/1.nt";
		try {
			mclient.runAsyncModel(fileName, token, probeList);
		} catch (TransmissionException e) {
			fail(e.getMessage());
			e.printStackTrace();
		}
		
	}

}

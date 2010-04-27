package org.imirsel.nema.contentrepository.client;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteContentRepositoryServiceTest extends BaseManagerTestCase{
	

	SimpleCredentials nemaCredentials;
	Repository repository = null;
	ClientRepositoryFactory factory = new ClientRepositoryFactory();
	static String RMI_URL = "rmi://localhost:2099/jackrabbit.repository";
	
	@Before
	public void setUp() throws Exception {
		repository = factory.getRepository(RMI_URL);
		nemaCredentials = new SimpleCredentials("user", "user".toCharArray());
	}


	@After
	public void tearDown() throws Exception {
	}
	
	

	@Test
	public void testGetExecutableBundle() {
		String resourcePath ="/users/user/flows/executables/test.zip";
		RepositoryResourcePath rrp = new RepositoryResourcePath(resourcePath);
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		try {
			ExecutableBundle bundle=crs.getExecutableBundle(nemaCredentials, rrp);
			assertEquals(bundle.getFileName(),"test.zip");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}
	}
	
	


}

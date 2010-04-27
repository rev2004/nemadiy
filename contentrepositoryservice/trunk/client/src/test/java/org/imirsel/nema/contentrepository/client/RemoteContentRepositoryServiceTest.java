package org.imirsel.nema.contentrepository.client;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteContentRepositoryServiceTest extends BaseManagerTestCase{
	

	private SimpleCredentials nemaCredentials;
	private Repository repository = null;
	private ClientRepositoryFactory factory = new ClientRepositoryFactory();
	private static String RMI_URL = "rmi://localhost:2099/jackrabbit.repository";
	
	@Before
	public void setUp() throws Exception {
		repository = factory.getRepository(RMI_URL);
		nemaCredentials = new SimpleCredentials("user", "user".toCharArray());
	}


	@After
	public void tearDown() throws Exception {
	}
	
	
	// test the imirsel nodes exist
	@Test
	public void testImirselNodes() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		boolean isValid=crs.validateNodeTypes(nemaCredentials);
		assertEquals(isValid,true);
	}
	
	
	
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
	
	
	public void testRemoveExecutableBundle(){
		String resourcePath ="/users/user/flows/executables/test.zip";
		RepositoryResourcePath rrp = new RepositoryResourcePath(resourcePath);
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		try {
			boolean success=crs.removeExecutableBundle(nemaCredentials, rrp);
			assertEquals(success,true);
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}
	}



}

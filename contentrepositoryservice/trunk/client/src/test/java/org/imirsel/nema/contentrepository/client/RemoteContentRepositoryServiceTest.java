package org.imirsel.nema.contentrepository.client;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;


import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.Flow.FlowType;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RemoteContentRepositoryServiceTest extends BaseManagerTestCase{
	

	private SimpleCredentials nemaCredentials;
	private Repository repository = null;
	private ClientRepositoryFactory factory = new ClientRepositoryFactory();
	private static String RMI_URL = "rmi://nema-dev.lis.illinois.edu:2099/jackrabbit.repository";
	
	@Before
	public void setUp() throws Exception {
		repository = factory.getRepository(RMI_URL);
		nemaCredentials = new SimpleCredentials("user", "user".toCharArray());
	}


	@After
	public void tearDown() throws Exception {
	}
	
	
	
	@Test
	public void testImirselNodes() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		boolean isValid=crs.validateNodeTypes(nemaCredentials);
		assertEquals(isValid,true);
	}
	
	
	
	public void testSaveFlow() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		byte[] flowContent = ContentRepositoryTestUtil.getFlowContent();
		String flowInstanceId = "flowInstance";
		Flow flow = new Flow();
		flow.setCreatorId(1l);
		flow.setDateCreated(new Date());
		flow.setDescription("Some flow description");
		flow.setId(1l);
		flow.setKeyWords("keyword1 keyword2 keyword3");
		flow.setName("Flow instance name");
		flow.setTemplate(false);
		flow.setType(FlowType.FEATURE_EXTRACTION);
		flow.setUri("http://www.imirsel.org/test/testinstanceFlow");
		ResourcePath rp=crs.saveFlow(nemaCredentials, flow, flowInstanceId, flowContent);
		assertEquals(rp.getPath(),"/users/user/flows/flowInstance/flowInstance");
	}
	
	@Test
	public void testFileSystemPath() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		RepositoryResourcePath resourcePath = new RepositoryResourcePath("/users/user/flows/executables/testFlow/test.zip");
		String fileSystemPath=crs.getExecutableBundleFSPath(nemaCredentials, resourcePath);
		System.out.println("FS Path is: " + fileSystemPath);
	}
	
	
	public void testHttpUrlForResource(){
		
	}
	
	
	public void testSaveExecutableBundle() throws ContentRepositoryServiceException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getExecutableBundle();
		ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,"testFlow", bundle);
		System.out.println(rp.getPath());
		assertEquals(rp.getPath(), "/users/user/flows/executables/testFlow/test.zip");
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

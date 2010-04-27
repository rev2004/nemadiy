package org.imirsel.nema.contentrepository.client;

import static org.junit.Assert.*;

import java.io.File;
import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.test.BaseManagerTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ContentRepositoryServiceTest extends BaseManagerTestCase {

	SimpleCredentials nemaCredentials;
	Repository repository = null;
	ClientRepositoryFactory factory = new ClientRepositoryFactory();

	@Before
	public void setUp() throws Exception {
		String tmpFolder = System.getProperty("java.io.tmpdir");
		File file = new File(tmpFolder, "data");
		if (file.exists()) {
			boolean success = deleteDirectory(file);
			System.out.println("repository deleted: " + success);
		}
		file.mkdir();
		String configFile = copyFile("client/src/resources/repository.xml",file.getAbsolutePath());
		String repHomeDir = file.getAbsolutePath();
		repository = ContentRepositoryTestUtil.getRepository(configFile, repHomeDir);
		nemaCredentials = new SimpleCredentials("user", "user".toCharArray());
		if (repository == null) {
			throw new Exception("Repository is null...");
		}
	}


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveExecutableBundle() {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getExecutableBundle();
		try {
			ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,
					"testFlow", bundle);
			System.out.println(rp.getPath());
			assertEquals(rp.getPath(), "/users/user/flows/executables/testFlow/test.zip");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testSaveFlow() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBundleMetadataNemaCredentialsString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetExecutableBundle() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBundleMetadataNemaCredentialsResourcePath() {
		fail("Not yet implemented");
	}



	

}

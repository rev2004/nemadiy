package org.imirsel.nema.contentrepository.client;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.util.zip.ZipException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.nodetype.NodeTypeIterator;

import org.apache.jackrabbit.api.JackrabbitNodeTypeManager;
import org.apache.jackrabbit.commons.NamespaceHelper;
import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.test.BaseManagerTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ContentRepositoryServiceTest extends BaseManagerTestCase {

	private SimpleCredentials nemaCredentials;
	private Repository repository = null;
	private ClientRepositoryFactory factory = new ClientRepositoryFactory();
	private static String RMI_URL = "rmi://nema.lis.uiuc.edu:2099/jackrabbit.repository";
	


	
	@Before
	public void setUp() throws Exception {
		//repository = ContentRepositoryTestUtil.getTempRepository();
		repository = factory.getRepository(RMI_URL);
		nemaCredentials = new SimpleCredentials("admin", "admin".toCharArray());
		if (repository == null) {
			throw new Exception("Repository is null...");
		}
	}


	@After
	public void tearDown() throws Exception {
	}

	@Ignore
	@Test
	public void testC1SaveExecutableBundle() throws ContentRepositoryServiceException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		crs.validateNodeTypes(nemaCredentials);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getC1ExecutableBundle(ContentRepositoryTestUtil.unixOs);
		try {
			crs.removeExecutableBundle(nemaCredentials,
					new RepositoryResourcePath("jcr","default","/users/user/flows/executables/testFlowC/c1.zip"));
			ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,
					"testFlow", bundle);
			System.out.println(rp.getPath());
			assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowC/c1.zip");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}

	}
	
	@Ignore
	@Test
	public void testJavaSaveExecutableBundle() throws ContentRepositoryServiceException, ZipException, InvalidCommandLineFlagException, IOException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		crs.validateNodeTypes(nemaCredentials);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getJavaExecutableBundle(ContentRepositoryTestUtil.unixOs);
		try {
			if(crs.exists(nemaCredentials, new RepositoryResourcePath("jcr", "default", "/users/user/flows/executables/testFlowJava/java1.zip"))){
			crs.removeExecutableBundle(nemaCredentials,
					new RepositoryResourcePath("jcr","default","/users/user/flows/executables/testFlowJava/java1.zip"));
			}
			ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,
					"testFlowJava", bundle);
			System.out.println(rp.getPath());
			assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowJava/java1.zip");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}

	}
	
	@Ignore
	@Test
	public void testJarExeSaveExecutableBundle() throws ContentRepositoryServiceException, InvalidCommandLineFlagException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		crs.validateNodeTypes(nemaCredentials);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getJarExecutableBundle(ContentRepositoryTestUtil.unixOs);
		try {
			crs.removeExecutableBundle(nemaCredentials,
					new RepositoryResourcePath("jcr","default","/users/user/flows/executables/testFlowJar/exechello.jar"));
			ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,
					"testFlowJar", bundle);
			System.out.println(rp.getPath());
			assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowJar/exechello.jar");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}

	}

	
	@Test
	public void registerNodes() throws LoginException, RepositoryException, IOException{
		Session session = repository.login(nemaCredentials);
	    NamespaceHelper namespaceHelper = new NamespaceHelper(session);
	    namespaceHelper.registerNamespace("imirsel","http://www.imirsel.org/jcr");
	
	     JackrabbitNodeTypeManager manager =
	    (JackrabbitNodeTypeManager) session.getWorkspace().getNodeTypeManager();
	    
	    NodeTypeIterator nti=manager.getAllNodeTypes();
	    while(nti.hasNext()){
	    	System.out.println(nti.nextNodeType().getName());
	    }
	    
	    // create exec file
	    if(!manager.hasNodeType("exec:file")){
	   	   if (!manager.hasNodeType("ns:file")){
	        String cnd = "<exec = 'http://www.imirsel.org/jcr/exec'>\n";
	        cnd += "[exec:file] > nt:file\n";
	        cnd += "- execId (STRING) mandatory\n";
	        cnd += "- typeName (STRING) mandatory\n";
	        cnd += "- mainClass (STRING) mandatory ignore\n";
	        cnd += "- executableName (STRING) mandatory ignore\n";
	        cnd += "- commandLineFlags (STRING) mandatory\n";
	    
	        
	        byte cndArray[] = cnd.getBytes();
	        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(cndArray);
	        manager.registerNodeTypes(byteArrayInputStream,JackrabbitNodeTypeManager.TEXT_X_JCR_CND); 
	        byteArrayInputStream.close();
		  }
	    }	  
         session.save();
         session.logout();
	}

	

	

}

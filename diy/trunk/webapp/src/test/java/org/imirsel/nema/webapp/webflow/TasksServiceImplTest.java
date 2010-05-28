package org.imirsel.nema.webapp.webflow;

import static org.junit.Assert.*;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.service.UserManager;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.test.MockExternalContext;

import edu.emory.mathcs.backport.java.util.Arrays;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/test-bean.xml","/testTasksService-bean.xml"})

public class TasksServiceImplTest {
	Mockery context=new Mockery();
	final FlowService flowService=context.mock(FlowService.class);
	private UserManager userManager=context.mock(UserManager.class);
	private String uploadDirectory="upload";
	private ArtifactService artifactService=context.mock(ArtifactService.class);
	private UUID uuid;
	static private Log logger=LogFactory.getLog(TasksServiceImplTest.class);
	TasksServiceImpl tasksService=new TasksServiceImpl();
	@Before
	public void setUp() throws Exception {
		tasksService.setArtifactService(artifactService);
		tasksService.setFlowService(flowService);
		tasksService.setUserManager(userManager);
		tasksService.setUploadDirectory(uploadDirectory);
		uuid=UUID.randomUUID();
	}

	@Resource
	Component component1;
	
	@Resource 
	Component component2;
	
	@Resource
	Component component3;
	
	
	@Resource
	Map<String,Property> datatypeMap1;
	
	@Resource
	Map<String,Property> datatypeMap2;
	@Resource
	Map<String,Property> datatypeMap3;
	
	@Resource
	Property propertyTrue;
	@Resource
	Property propertyFalse;
	
	@Test
	public final void testAddExecutable() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testClearBundles() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetFlowTemplates() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFindBundle() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testFillDefaultParameter() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetDatatypeMaps() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testSetComponentList() {
		fail("Not yet implemented"); // TODO
	}


	@Resource
	HttpServletRequest mockHttpServletRequest;

	@Resource 
	MockExternalContext mockExternalContext;
	@Test
	public final void testSetUploadingPaths() {
		final ServletContext mockServletContext=context.mock(ServletContext.class);
		final String root="/mock/home/webapp/";
		context.checking(new Expectations(){{
			oneOf(mockServletContext).getRealPath(uploadDirectory); will(returnValue(root+uploadDirectory));
			oneOf(mockServletContext).getRealPath("/"); will(returnValue(root));			
		}});
		mockExternalContext.setNativeContext(mockServletContext);
		
		final String webDirPre="randomPreset";
		final String physicalDirPre="physicalPreset";
		tasksService.setWebDir(webDirPre);
		tasksService.setPhysicalDir(physicalDirPre);
		assertEquals(webDirPre,tasksService.getWebDir());
		assertEquals(physicalDirPre,tasksService.getPhysicalDir());
		tasksService.setUploadingPaths(mockExternalContext, uuid);
		assertEquals(webDirPre,tasksService.getWebDir());
		assertEquals(physicalDirPre,tasksService.getPhysicalDir());
		
		tasksService.setWebDir(null);
		tasksService.setUploadingPaths(mockExternalContext, uuid);
		assertEquals("http://mock.nema.lis.illinois.edu:1111/mock/Context/"+uploadDirectory+"/"+mockHttpServletRequest.getRemoteUser()+"/"+uuid+"/",
				tasksService.getWebDir());
		assertEquals(root+uploadDirectory+"/"+mockHttpServletRequest.getRemoteUser()+"/"+uuid+"/",tasksService.getPhysicalDir());
		
	}

	
	@Test
	public final void testShownMap() {
		Map<String,Property> map1=new HashMap<String,Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		Map<String,Property> shown=tasksService.shownMap(map1);
		String[] niceKeys={"TestField1","TestField2",tasksService.REMOTE_COMPONENT};
		Set<String> niceKeysSet=new HashSet<String>(Arrays.asList(niceKeys));
		assertEquals(shown.keySet(),niceKeysSet);
		
		map1=new HashMap<String,Property>(datatypeMap2);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		shown=tasksService.shownMap(map1);
		String[] niceKeys2={"TestField1","TestField2","TestField3","TestField4","TestField5","TestField6",tasksService.REMOTE_COMPONENT};
		niceKeysSet=new HashSet<String>(Arrays.asList(niceKeys2));
		assertEquals(shown.keySet(),niceKeysSet);

	}
	
	
	@Test
	public final void testShownRemoteMap() {
		Map<String,Property> map1=new HashMap<String,Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		map1.put(tasksService.CREDENTIALS, null);
		map1.put(tasksService.EXECUTABLE_URL,null);
		map1.put(tasksService.GROUP, null);
		map1.put(tasksService.OS, null);
		Map<String,Property> shown=tasksService.shownRemoteMap(map1);
		String[] niceKeys={"TestField1","TestField2"};
		Set<String> niceKeysSet=new HashSet<String>(Arrays.asList(niceKeys));
		assertEquals(shown.keySet(),niceKeysSet);
		
		map1=new HashMap<String,Property>(datatypeMap2);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		map1.put(tasksService.CREDENTIALS, null);
		map1.put(tasksService.EXECUTABLE_URL,null);
		map1.put(tasksService.GROUP, null);

		shown=tasksService.shownRemoteMap(map1);
		String[] niceKeys2={"TestField1","TestField2","TestField3","TestField4","TestField5","TestField6"};
		niceKeysSet=new HashSet<String>(Arrays.asList(niceKeys2));
		assertEquals(shown.keySet(),niceKeysSet);

	}
	
	@Test
	public final void testIsRemoteServiceComponent() {
		Map<String,Property> map1=new HashMap<String,Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		assertFalse(tasksService.isRemoteServiceComponent(map1));
		assertFalse(tasksService.isRemoteServiceComponent(datatypeMap1));			
		Map<String,Property> map2=new HashMap<String,Property>(datatypeMap2);
		map2.put(tasksService.REMOTE_COMPONENT,propertyTrue);
		assertTrue(tasksService.isRemoteServiceComponent(map2));
		assertFalse(tasksService.isRemoteServiceComponent(datatypeMap2));			
	
	}

	@Test
	public final void testUpdateDataMap() {
		fail("Not yet implemented"); // TODO
	}

	

	@Test
	public final void testRun() {
		fail("Not yet implemented"); // TODO
	}

}

package org.imirsel.nema.webapp.webflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;
import javax.jcr.SimpleCredentials;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.ArtifactService;
import org.imirsel.nema.contentrepository.client.ContentRepositoryServiceException;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.Sequence;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.binding.message.DefaultMessageContext;
import org.springframework.binding.message.MessageContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.webflow.test.MockExternalContext;
import org.springframework.webflow.test.MockParameterMap;

import edu.emory.mathcs.backport.java.util.Arrays;
import edu.emory.mathcs.backport.java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-bean.xml",
		"/testTasksService-bean.xml" })
public class TasksServiceTest {
	Mockery context = new JUnit4Mockery() ;
	final FlowService flowService = context.mock(FlowService.class);

	@Resource
	private UserManager userManager;
	private String uploadDirectory = "/upload";
	private ArtifactService artifactService = context
			.mock(ArtifactService.class);
	private UUID uuid;
	static private Log logger = LogFactory.getLog(TasksServiceTest.class);
	TasksServiceImpl tasksService = new TasksServiceImpl();

	@Before
	public void setUp() throws Exception {
		tasksService.setArtifactService(artifactService);
		tasksService.setFlowService(flowService);
		tasksService.setUserManager(userManager);
		tasksService.setUploadDirectory(uploadDirectory);
		uuid = UUID.randomUUID();
	}

	@Resource
	Component component1;

	@Resource
	Component component2;

	@Resource
	Component component3;

	@Resource
	Map<String, Property> datatypeMap1;

	@Resource
	Map<String, Property> datatypeMap2;
	@Resource
	Map<String, Property> datatypeMap3;

	@Resource
	Property propertyTrue;
	@Resource
	Property propertyFalse;
	@Resource
	Property propertyFile;
	@Resource
	Map<Component, Map<String, Property>> datatypeMaps;

	@Resource
	List<Component> componentList;

	@Resource
	SimpleCredentials credentials;

	@Resource
	ResourcePath path1;
	@Resource
	ResourcePath path2;
	@Resource
	ResourcePath path3;
	@Resource
	ResourcePath path4;

	@Resource
	Map<Component, ResourcePath> executableMap;

	@Resource
	UploadedExecutableBundle uploadBundle; 
	@Test
	public final void testAddExecutable() {
		try {
		
			final Sequence addExecutable=context.sequence("addExecutable");
			context.checking(new Expectations() {
				{
					oneOf(artifactService).exists(credentials, path2);	will(returnValue(true));inSequence(addExecutable);
					oneOf(artifactService).removeExecutableBundle(credentials,path2); inSequence(addExecutable);
					oneOf(artifactService).saveExecutableBundle(credentials,uuid.toString(),uploadBundle); will(returnValue(path3));inSequence(addExecutable);
					oneOf(artifactService).exists(credentials, path1);	will(returnValue(false)); inSequence(addExecutable);
					oneOf(artifactService).saveExecutableBundle(credentials,uuid.toString(),uploadBundle); will(returnValue(path4));inSequence(addExecutable);
				}
			});
			MessageContext messageContext=new DefaultMessageContext();
			Map<Component, ResourcePath> map = new HashMap<Component, ResourcePath>(executableMap);
			Map<String,Property> data=new HashMap<String,Property>(datatypeMap2);
			tasksService.addExecutable(component2,data,uploadBundle,uuid,map,messageContext);
			assertEquals(path3.getPath(),data.get(tasksService.EXECUTABLE_URL).getValue());
			assertEquals("true", data.get(tasksService.REMOTE_COMPONENT).getValue());
			assertEquals(uploadBundle.getPreferredOs(), data.get(tasksService.OS).getValue());
			assertEquals(uploadBundle.getGroup(), data.get(tasksService.GROUP).getValue());
			assertEquals(credentials.getUserID() + ":"+ new String(credentials.getPassword()),
					data.get(tasksService.CREDENTIALS).getValue());
			assertEquals(path3,map.get(component2));

			tasksService.addExecutable(component3,data,uploadBundle,uuid,map,messageContext);
			assertEquals(path4.getPath(),data.get(tasksService.EXECUTABLE_URL).getValue());
			assertEquals("true", data.get(tasksService.REMOTE_COMPONENT).getValue());
			assertEquals(uploadBundle.getPreferredOs(), data.get(tasksService.OS).getValue());
			assertEquals(uploadBundle.getGroup(), data.get(tasksService.GROUP).getValue());
			assertEquals(credentials.getUserID() + ":"+ new String(credentials.getPassword()),
					data.get(tasksService.CREDENTIALS).getValue());
			assertEquals(path4,map.get(component3));

		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}
		context.assertIsSatisfied();
	}

	@Test
	public final void testClearBundles() {
		try {
			context.checking(new Expectations() {
				{
					oneOf(artifactService).exists(credentials, path2);
					will(returnValue(true));
					oneOf(artifactService).removeExecutableBundle(credentials,
							path2);
					oneOf(artifactService).exists(credentials, path1);
					will(returnValue(false));
				}
			});
			Map<Component, ResourcePath> map = new HashMap<Component, ResourcePath>(
					executableMap);
			tasksService.clearBundles(map);
		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}
		context.assertIsSatisfied();
	}

	@Test
	public final void testGetFlowTemplates() {
		context.checking(new Expectations() {
			{
				exactly(3).of(flowService).getFlowTemplates();	will(returnValue(flowSet));
				
			}
		});
	
		List<Flow> list=tasksService.getFlowTemplates(null);
		Set<Flow>  set=new HashSet<Flow>(list);
		assertEquals(flowSet, set );
	
		Set<Flow>  featureExtractionSet=new HashSet<Flow>();
		featureExtractionSet.add(flow1);featureExtractionSet.add(flow2);
		list=tasksService.getFlowTemplates("Feature Extraction");
		set=new HashSet<Flow>(list);
		assertEquals(featureExtractionSet,set);
		
		list=tasksService.getFlowTemplates("not existing type");
		set=new HashSet<Flow>(list);
		assertEquals(flowSet,set);
	
		context.assertIsSatisfied();
	}

	@Test
	public final void testFindBundle() {

		try {
			context.checking(new Expectations() {
				{
					oneOf(artifactService).exists(credentials, path1);
					will(returnValue(false));
					oneOf(artifactService).exists(credentials, path2);
					will(returnValue(true));
					oneOf(artifactService).getExecutableBundle(credentials,
							path2);
				}
			});
			assertNull(tasksService.findBundle(path1, datatypeMap2));
			UploadedExecutableBundle foundBundle = tasksService.findBundle(
					path2, datatypeMap2);
			assertEquals(datatypeMap2.get(tasksService.OS).getValue(),
					foundBundle.getPreferredOs());
			assertEquals(datatypeMap2.get(tasksService.GROUP).getValue(),
					foundBundle.getGroup());
		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}

		context.assertIsSatisfied();
	}

	@Test
	public final void testRemoveExecutable() {

		try {

			context.checking(new Expectations() {
				{
					oneOf(artifactService).exists(credentials, path1);
					will(returnValue(false));
					oneOf(artifactService).exists(credentials, path2);
					will(returnValue(true));
					oneOf(artifactService).removeExecutableBundle(credentials,
							path2);
				}
			});
			Map<Component, ResourcePath> map = new HashMap<Component, ResourcePath>(
					executableMap);
			Map<String, Property> data3 = new HashMap<String, Property>(
					datatypeMap3);
			Map<String, Property> data2 = new HashMap<String, Property>(
					datatypeMap2);
			tasksService.removeExecutable(component3, map, data3);
			assertEquals("", data3.get(tasksService.EXECUTABLE_URL).getValue());
			assertFalse(map.containsKey(component3));
			logger.debug(map.get(component2) == path2);
			logger.debug(data2);
			tasksService.removeExecutable(component2, map, data2);
			assertEquals("", data3.get(tasksService.EXECUTABLE_URL).getValue());
			assertFalse(map.containsKey(component2));

		} catch (ContentRepositoryServiceException e) {
			logger.error(e, e);
		}

		context.assertIsSatisfied();
	}

	@Resource
	Flow flow1;
	@Resource
	Flow flow2;
	
	@Resource 
	Set<Flow> flowSet;

	@Test
	public final void testSetDatatypeMaps() {

		context.checking(new Expectations() {
			{
				oneOf(flowService).getComponents(flow1.getUri());
				will(returnValue(componentList));
				oneOf(flowService).getComponentPropertyDataType(
						componentList.get(2), flow1.getUri());
				will(returnValue(datatypeMaps.get(componentList.get(2))));
				oneOf(flowService).getComponentPropertyDataType(
						componentList.get(1), flow1.getUri());
				will(returnValue(datatypeMaps.get(componentList.get(1))));
				oneOf(flowService).getComponentPropertyDataType(
						componentList.get(0), flow1.getUri());
				will(returnValue(datatypeMaps.get(componentList.get(0))));
			}
		});
		Map<Component, Map<String, Property>> map = tasksService
				.setDatatypeMaps(flow1);
		assertEquals(datatypeMaps, map);
		context.assertIsSatisfied();
	}

	@Test
	public final void testSetComponentList() {
		List<Component> list = tasksService.setComponentList(datatypeMaps);
		logger.debug("before sort:" + componentList.get(0).getName() + ","
				+ componentList.get(1).getName());
		Collections.sort(componentList);
		logger.debug("after sort:" + componentList.get(0).getName() + ","
				+ componentList.get(1).getName());
		assertEquals(componentList, list);
	}

	@Resource
	HttpServletRequest mockHttpServletRequest;

	@Resource
	MockExternalContext mockExternalContext;

	/**
	 * TODO
	 * this test is not well writen, it is going to fail in windows due to the jmock setting
	 */
	@Test
	public final void testSetUploadingPaths() {
		final String subStr = uploadDirectory + "/"
				+ mockHttpServletRequest.getRemoteUser() + "/" + uuid + "/";

		final ServletContext mockServletContext = context
				.mock(ServletContext.class);
		final String root = "/mock/home/webapp";
		context.checking(new Expectations() {
			{
				oneOf(mockServletContext).getRealPath(subStr);
				will(returnValue(root + uploadDirectory + "/"
						+ mockHttpServletRequest.getRemoteUser() + "/" + uuid));
			}
		});
		mockExternalContext.setNativeContext(mockServletContext);

		final String webDirPre = "randomPreset";
		final String physicalDirPre = "physicalPreset";
		tasksService.setWebDir(webDirPre);
		tasksService.setPhysicalDir(physicalDirPre);
		assertEquals(webDirPre, tasksService.getWebDir());
		assertEquals(physicalDirPre, tasksService.getPhysicalDir());
		tasksService.setUploadingPaths(mockExternalContext, uuid);
		assertEquals(webDirPre, tasksService.getWebDir());
		assertEquals(physicalDirPre, tasksService.getPhysicalDir());

		tasksService.setWebDir(null);
		tasksService.setUploadingPaths(mockExternalContext, uuid);
		assertEquals("http://mock.nema.lis.illinois.edu:1111/mock/Context"
				+ subStr, tasksService.getWebDir());
		assertEquals(root + uploadDirectory + "/"
				+ mockHttpServletRequest.getRemoteUser() + "/" + uuid + File.separator,
				tasksService.getPhysicalDir());
		context.assertIsSatisfied();
	}

	@Test
	public final void testShownMap() {
		Map<String, Property> map1 = new HashMap<String, Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		Map<String, Property> shown = tasksService.shownMap(map1);
		String[] niceKeys = { "TestField1", "TestField2",
				tasksService.REMOTE_COMPONENT };
		Set<String> niceKeysSet = new HashSet<String>(Arrays.asList(niceKeys));
		assertEquals(niceKeysSet, shown.keySet());

		map1 = new HashMap<String, Property>(datatypeMap2);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		shown = tasksService.shownMap(map1);
		String[] niceKeys2 = { "TestField1", "TestField2", "TestField3",
				"ProfileName", "_os", "_group", tasksService.REMOTE_COMPONENT,"_credentials" };
		niceKeysSet = new HashSet<String>(Arrays.asList(niceKeys2));
		assertEquals(niceKeysSet, shown.keySet());

	}

	@Test
	public final void testShownRemoteMap() {
		Map<String, Property> map1 = new HashMap<String, Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		map1.put(tasksService.CREDENTIALS, null);
		map1.put(tasksService.EXECUTABLE_URL, null);
		map1.put(tasksService.GROUP, null);
		map1.put(tasksService.OS, null);
		Map<String, Property> shown = tasksService.shownRemoteMap(map1);
		String[] niceKeys = { "TestField1", "TestField2" };
		Set<String> niceKeysSet = new HashSet<String>(Arrays.asList(niceKeys));
		assertEquals(niceKeysSet, shown.keySet());

		map1 = new HashMap<String, Property>(datatypeMap2);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		map1.put(tasksService.CREDENTIALS, null);
		map1.put(tasksService.EXECUTABLE_URL, null);
		map1.put(tasksService.GROUP, null);

		shown = tasksService.shownRemoteMap(map1);
		String[] niceKeys2 = { "TestField1", "TestField2", "TestField3" };
		niceKeysSet = new HashSet<String>(Arrays.asList(niceKeys2));
		assertEquals(niceKeysSet, shown.keySet());

	}

	@Test
	public final void testIsRemoteServiceComponent() {
		Map<String, Property> map1 = new HashMap<String, Property>(datatypeMap1);
		map1.put(tasksService.REMOTE_COMPONENT, propertyFalse);
		assertFalse(tasksService.isRemoteServiceComponent(map1));
		assertFalse(tasksService.isRemoteServiceComponent(datatypeMap1));
		Map<String, Property> map2 = new HashMap<String, Property>(datatypeMap2);
		map2.put(tasksService.REMOTE_COMPONENT, propertyTrue);
		assertTrue(tasksService.isRemoteServiceComponent(map2));
		assertFalse(tasksService.isRemoteServiceComponent(datatypeMap2));

	}

	@Resource
	Map<String,String> parameters1;
	@Resource
	Map<String,String> parameters2;
	
	//TODO no multipart file upload test yet.  
	//Not sure how to test writing a file because not to sure how to get definite directory
	@Test
	public final void testUpdateDataMap() {
		MockParameterMap parameterMap=new MockParameterMap();
		for (Map.Entry<String,String> entry:parameters1.entrySet()){
			parameterMap.put(entry.getKey(),entry.getValue());
		}
		Map<String,Property> data=new HashMap<String,Property>(datatypeMap1);
		tasksService.updateDataMap(parameterMap, data);
		assertEquals(parameters1.get("property1"), data.get("testField1").getValue());
		assertEquals(parameters1.get("property2"), data.get("TestField2").getValue());
		assertFalse(data.containsKey("property3"));
		
		parameterMap=new MockParameterMap();
		for (Map.Entry<String,String> entry:parameters2.entrySet()){
			parameterMap.put(entry.getKey(),entry.getValue());
		}
		data=new HashMap<String,Property>(datatypeMap1);
		tasksService.updateDataMap(parameterMap, data);
		assertEquals(parameters2.get("property1"), data.get("testField1").getValue());
		assertEquals(parameters1.get("property2"), data.get("TestField2").getValue());
		assertFalse(data.containsKey("property3"));	
		
		
	}

	@Resource
	User user;
	
	@SuppressWarnings("unchecked")
	@Test
	public final void testRun() {
		try{
		final String name="job name";
		final String description="job description";
		context.checking(new Expectations() {
			{
				oneOf(flowService).createNewFlow(with(same(credentials)),with(aNonNull(Flow.class)), 
						(HashMap<String,String>)with(any(HashMap.class)), with(same(flow1.getUri())), 
						with(same(user.getId()))); will(returnValue(flow2));
				oneOf(flowService).executeJob(with(aNonNull(String.class)),
						with(same(name)),with(same(description)),with(same(flow2.getId())),
								with(same(user.getId())),with(same(user.getEmail())));
			}
		});
		 tasksService.run(flow1,datatypeMaps,name,description);
		}catch (MeandreServerException e) {
			logger.error(e,e);
		}
	}

}

package org.imirsel.nema.contentrepository.client;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.zip.ZipException;


import javax.jcr.Repository;
import javax.jcr.SimpleCredentials;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.NemaContentRepositoryFile;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;
import org.imirsel.nema.model.Flow.FlowType;
import org.imirsel.nema.model.ResultType;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class RemoteContentRepositoryServiceTest extends BaseManagerTestCase{
	

	private SimpleCredentials nemaCredentials;
	private Repository repository = null;
	private ClientRepositoryFactory factory = new ClientRepositoryFactory();
	private static String RMI_URL = "rmi://nema.lis.uiuc.edu:2099/jackrabbit.repository";
	
	@Before
	public void setUp() throws Exception {
		repository = factory.getRepository(RMI_URL);
		nemaCredentials = new SimpleCredentials("admin", "b2cebd873228d3e6753d9b39195730694e3d1bbc".toCharArray());
	}


	@After
	public void tearDown() throws Exception {
	}
	
	
	
	@Ignore
	@Test
	public void testImirselNodes() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		boolean isValid=crs.validateNodeTypes(nemaCredentials);
		assertEquals(isValid,true);
	}
	
	@Ignore
	@Test
	public void testSaveContentRepositoryFile() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		String tmpDir = System.getProperty("java.io.tmpdir");
		File resDir = new File(tmpDir,"one");
		resDir.mkdir();
		File file = new File(resDir,"test.data");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(new Date().toString());
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		NemaContentRepositoryFile nemaResult = null;
		try {
			nemaResult = createNemaContentRepositoryFile(file, tmpDir,"org.model.tmp");
		} catch (IOException e) {
			fail("Error creating the nema result file");
		}
		ResourcePath rpath=crs.saveResultFile(nemaCredentials, nemaResult);
		System.out.println(rpath.getURIAsString());
		
	}
	
	
	
	@Ignore
	@Test
	public void testSaveContentRepositoryDirectory() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		String tmpDir = System.getProperty("java.io.tmpdir");
		File resDir0 = new File(tmpDir,"zero");
		File resDir = new File(resDir0,"one");
		resDir.mkdirs();
		File file = new File(resDir,"test.data");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(new Date().toString());
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File file1 = new File(resDir0,"test0.data");
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(new Date().toString());
			writer.flush();
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		NemaContentRepositoryFile nemaResult = null;
		try {
			nemaResult = createNemaContentRepositoryFile(resDir0, tmpDir,"org.model.tmp");
		} catch (IOException e) {
			fail("Error creating the nema result file");
		}
		ResourcePath rpath=crs.saveResultFile(nemaCredentials, nemaResult);
		System.out.println(rpath.getURIAsString());
		
	}
	
	
	private NemaContentRepositoryFile createNemaContentRepositoryFile(File file, String relativeLoc, String model) throws IOException {
		NemaContentRepositoryFile nemaResult = new NemaContentRepositoryFile();
		String path = file.getCanonicalPath();
		byte[] fileContent= null;
		CompressionUtils cutils= CompressionUtils.getInstanceOf();
		fileContent = cutils.compress(path,relativeLoc);
		
		if(fileContent==null){
			//throw new RuntimeException("file byte contents size is null " + path);
		}
		
		nemaResult.setExecutionId(System.currentTimeMillis()+"");
		nemaResult.setFileContent(fileContent);
		String parentPath=file.getParent() + File.separator;
		nemaResult.setModelClass(model);
		nemaResult.setName(file.getName());
		nemaResult.setFileName(path);
		if(file.isDirectory()){
			nemaResult.setResultType(ResultType.DIR);
		}else{
			nemaResult.setResultType(ResultType.FILE);
		}
		String resultLoc =parentPath;
		int loc=resultLoc.indexOf(relativeLoc);
		
		if(loc!=-1){
			if(relativeLoc!=null){
				parentPath = resultLoc.substring(loc+relativeLoc.length());
			}
		}
		nemaResult.setPath(parentPath);
		return nemaResult;
	}
	
	@Test
	public void testGetResult() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		RepositoryResourcePath rrp = new RepositoryResourcePath("jcr", "default", "/users/admin/flows/1278016989084/results/zero");
		NemaContentRepositoryFile nresult=crs.getNemaContentRepositoryFile(nemaCredentials, rrp);
		System.out.println(nresult.getExecutionId()+  " " + nresult.getFileName() + " "+ nresult.getModelClass() + " " + nresult.getName() + " " + nresult.getFileContent().length);
	}
	


	
	@Ignore
	@Test
	public void testSaveFlow() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		byte[] flowContent = ContentRepositoryTestUtil.getFlowContent();
		String flowInstanceId = "flowInstance1";
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
		assertEquals(rp.getPath(),"/users/user/flows/flowInstance/flowInstance1");
	}
	
	@Ignore
	@Test
	public void testFileSystemPath() throws ContentRepositoryServiceException{
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		RepositoryResourcePath resourcePath = new RepositoryResourcePath("jcr","default","/users/user/flows/executables/testFlow/test.zip");
		String fileSystemPath=crs.getExecutableBundleFSPath(nemaCredentials, resourcePath);
		System.out.println("FS Path is: " + fileSystemPath);
	}
	
	
	public void testHttpUrlForResource(){
		
	}
	
	@Ignore
	@Test
	public void testPrintLargeDataSaveExecutableBundle() throws ContentRepositoryServiceException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getPrintLargeDataExecutableBundle(ContentRepositoryTestUtil.unixOs);
		ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,"testFlow", bundle);
		System.out.println(rp.getPath());
		assertEquals(rp.getPath(), "/users/user/flows/executables/testFlow/printlarge.zip");
	}
	
	@Ignore
	@Test
	public void testCSaveExecutableBundle() throws ContentRepositoryServiceException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getC1ExecutableBundle(ContentRepositoryTestUtil.unixOs);
		ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,"testFlowC", bundle);
		System.out.println(rp.getPath());
		assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowC/c1.zip");
	}
	

	@Ignore
	@Test
	public void testJavaSaveExecutableBundle() throws ContentRepositoryServiceException, ZipException, InvalidCommandLineFlagException, IOException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getJavaExecutableBundle(ContentRepositoryTestUtil.unixOs);
		ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,"testFlowJava", bundle);
		System.out.println(rp.getPath());
		assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowJava/java1.zip");
	}
	
	@Ignore
	@Test
	public void testSaveJarExecutableBundle() throws ContentRepositoryServiceException, InvalidCommandLineFlagException {
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		ExecutableBundle bundle = ContentRepositoryTestUtil.getJarExecutableBundle( ContentRepositoryTestUtil.unixOs);
		ResourcePath rp = crs.saveExecutableBundle(nemaCredentials,"testFlowJar", bundle);
		System.out.println(rp.getPath());
		assertEquals(rp.getPath(), "/users/user/flows/executables/testFlowJar/exechello.jar");
	}

	
	

    @Ignore
	@Test
	public void testGetExecutableBundle() {
		String resourcePath ="/users/user/flows/executables/testFlowJar/exechello.jar";
		RepositoryResourcePath rrp = new RepositoryResourcePath("jcr","default",resourcePath);
		ContentRepositoryService crs = new ContentRepositoryService();
		crs.setRepository(repository);
		try {
			ExecutableBundle bundle=crs.getExecutableBundle(nemaCredentials, rrp);
			System.out.println(bundle.getCommandLineFlags());
			assertEquals(bundle.getFileName(),"exechello.jar");
		} catch (ContentRepositoryServiceException e) {
			fail(e.getMessage());
		}
	}
	
	
  @Ignore
   @Test
	public void testRemoveExecutableBundle(){
		String resourcePath ="/users/user/flows/executables/testFlowJava/java1.zip";
		RepositoryResourcePath rrp = new RepositoryResourcePath("jcr","default",resourcePath);
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

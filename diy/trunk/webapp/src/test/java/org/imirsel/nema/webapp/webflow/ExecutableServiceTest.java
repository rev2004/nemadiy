package org.imirsel.nema.webapp.webflow;


import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.Property;
import org.imirsel.nema.model.SysProperty;
import org.imirsel.nema.model.VanillaPredefinedCommandTemplate;
import org.imirsel.nema.webapp.model.DiyJavaTemplate;
import org.imirsel.nema.webapp.model.NiceParams;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.webflow.test.MockParameterMap;
import org.jmock.lib.legacy.ClassImposteriser;


public class ExecutableServiceTest {
	private Mockery context = new JUnit4Mockery() {{
        setImposteriser(ClassImposteriser.INSTANCE);
    }};
		
	final String[] keys={"a","b","c"};
	final String[] values={"1","2","3"};
	final String[] files1={"file1","file2"};
	final String[] files2={ "type1","type2","type3"};

	static private Log logger = LogFactory.getLog(ExecutableServiceTest.class);
	ExecutableServiceImpl service = new ExecutableServiceImpl();
	@Before
	public void setUp() throws Exception {
	}
	
	@Test
	public final void testSetCommonTempl() {
		
		MockParameterMap parameters=new MockParameterMap();
		parameters.put("variable", keys);
		parameters.put("value", values);
		parameters.put("input", files1);
		parameters.put("output", files2);
		parameters.put("other", keys);
		VanillaPredefinedCommandTemplate template=new DiyJavaTemplate();
		service.setCommonTemplate(parameters, template);
		
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("a", "1"); 
		maps.put("b", "2");
		maps.put("c","3");
		
		
		assertEquals(maps,template.getEnvironmentMap());
		
		NiceParams nice=new NiceParams(template.getParams());
		assertEquals(Arrays.asList(files1),nice.getInputs());
		assertEquals(Arrays.asList(files2),nice.getOutputs());
		assertEquals(Arrays.asList(keys),nice.getOthers());
	}
	 UploadedExecutableBundle bundle=context.mock(UploadedExecutableBundle.class);
	@Test
	public final void testSetJavaTemplate(){
		
		final Path path1=new Path("path1");
		final Path path2=new Path("path2");
		final List<Path> paths=Arrays.asList(path1,path2);
		context.checking(new Expectations() {
			{
				oneOf(bundle).getJarPaths(); will(returnValue(paths));
				
			}
		});
		DiyJavaTemplate template=new DiyJavaTemplate();
		MockParameterMap parameters=new MockParameterMap();
		parameters.put("sysVar", keys);
		parameters.put("sysValue", values);
		
		Map<String,String> maps=new HashMap<String,String>();
		maps.put("a", "1"); 
		maps.put("b", "2");
		maps.put("c","3");
	
		service.setJavaTemplate(parameters, bundle,template);
		assertEquals(paths, template.getClasspath());
		Map<String,String> sys=new HashMap<String,String>();
		for (SysProperty prop:template.getProperties()){
			sys.put(prop.getName(),prop.getValue());
		}
		assertEquals(maps,sys);
		
		context.assertIsSatisfied();

	}
}	

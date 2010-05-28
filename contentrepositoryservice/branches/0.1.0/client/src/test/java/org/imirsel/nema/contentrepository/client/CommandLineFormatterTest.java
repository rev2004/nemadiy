package org.imirsel.nema.contentrepository.client;

import static org.junit.Assert.*;

import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.Path;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class CommandLineFormatterTest {
	



	@Before
	public void setUp() throws Exception {
		
	
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetCommandLineStringForgiving() {
		boolean filterInvalidOptions = true;
		final JavaPredefinedCommandTemplate pct1 = getJavaPredefinedCommanTemplate1();
		final JavaPredefinedCommandTemplate pct2 = getJavaPredefinedCommanTemplate2();
		final JavaPredefinedCommandTemplate pct3 = getJavaPredefinedCommanTemplate3();
		final JavaPredefinedCommandTemplate pct4 = getJavaPredefinedCommanTemplate4();
		final JavaPredefinedCommandTemplate pct5 = getJavaPredefinedCommanTemplate5();
		CommandLineFormatter clf = new CommandLineFormatter();
		try {
			String val=clf.getCommandLineString(pct1, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-classpath .:one.jar:two.jar -Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		try {
			String val=clf.getCommandLineString(pct2,  ContentRepositoryTestUtil.unixOs,filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa  -jar test.jar",val);
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct3, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct4,  ContentRepositoryTestUtil.unixOs,filterInvalidOptions);
			System.out.println(val);
			assertEquals("-classpath .:one.jar:two.jar -Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct5, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa  -jar test.jar",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		
	}
	
	@Test
	public void testGetCommandLineStringStrict() {
		boolean filterInvalidOptions = false;
		final JavaPredefinedCommandTemplate pct1 = getJavaPredefinedCommanTemplate1();
		final JavaPredefinedCommandTemplate pct2 = getJavaPredefinedCommanTemplate2();
		final JavaPredefinedCommandTemplate pct3 = getJavaPredefinedCommanTemplate3();
		final JavaPredefinedCommandTemplate pct4 = getJavaPredefinedCommanTemplate4();
		final JavaPredefinedCommandTemplate pct5 = getJavaPredefinedCommanTemplate5();
		
		
		CommandLineFormatter clf = new CommandLineFormatter();
		try {
			String val=clf.getCommandLineString(pct1, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-classpath .:one.jar:two.jar -Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		try {
			String val=clf.getCommandLineString(pct2,  ContentRepositoryTestUtil.unixOs,filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa  -jar test.jar",val);
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct3, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct4,  ContentRepositoryTestUtil.unixOs,filterInvalidOptions);
			System.out.println(val);
			assertEquals("-classpath .:one.jar:two.jar -Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa org.imirse.test.Main",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		
		try {
			String val=clf.getCommandLineString(pct5, ContentRepositoryTestUtil.unixOs, filterInvalidOptions);
			System.out.println(val);
			assertEquals("-Xmx2048m -Xms1024m  -verbose:gc  -verbose:jni  -verbose:class  -esa  -jar test.jar",val.trim());
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}

		
	}

	
	@Test(expected=InvalidCommandLineFlagException.class)
	public void testMissingJarFile() throws InvalidCommandLineFlagException{
		final JavaPredefinedCommandTemplate pct6 = getJavaPredefinedCommanTemplate6();
		CommandLineFormatter clf = new CommandLineFormatter();
		String val=null;
		val = clf.getCommandLineString(pct6, ContentRepositoryTestUtil.unixOs, false);
	}
	
	@Test
	public void testInvalidProperty(){
		final JavaPredefinedCommandTemplate pct7 = getJavaPredefinedCommanTemplate7();
		CommandLineFormatter clf = new CommandLineFormatter();
		String val=null;
		try {
			val = clf.getCommandLineString(pct7, ContentRepositoryTestUtil.unixOs, false);
		} catch (InvalidCommandLineFlagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testInvalidPropertyForgiving(){
		final JavaPredefinedCommandTemplate pct7 = getJavaPredefinedCommanTemplate7();
		CommandLineFormatter clf = new CommandLineFormatter();
		String val=null;
		try {
			val = clf.getCommandLineString(pct7, ContentRepositoryTestUtil.unixOs, true);
		} catch (InvalidCommandLineFlagException e) {
			fail(e.getMessage());
		}
		System.out.println(val);
	}
	
	
	
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate1() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(false);
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		pct.addClasspath(new Path("one.jar"));
		pct.addClasspath(new Path("two.jar"));
		return pct;
	}
	
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate2() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(true);
		pct.setJarFile("test.jar");
		
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		return pct;
	}
	
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate3() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(false);
		pct.setJarFile("test.jar");
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		return pct;
	}
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate4() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(false);
		pct.setJarFile("test.jar");
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		pct.addClasspath(new Path("one.jar"));
		pct.addClasspath(new Path("two.jar"));
		return pct;
	}
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate5() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(true);
		pct.setJarFile("test.jar");
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		pct.addClasspath(new Path("one.jar"));
		pct.addClasspath(new Path("two.jar"));
		return pct;
	}
	
	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate6() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(true);
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		pct.addClasspath(new Path("one.jar"));
		pct.addClasspath(new Path("two.jar"));
		return pct;
	}

	private JavaPredefinedCommandTemplate getJavaPredefinedCommanTemplate7() {
		JavaPredefinedCommandTemplate pct = new JavaPredefinedCommandTemplate();
		pct.setEnableAssertionPackages("org.imirsel.test");
		pct.setDisableAssertionPackages("org.imirsel.test.dontassert");
		pct.setEnableSystemAssertions(true);
		pct.setJarExecutable(false);
		pct.setMainClass("org.imirse.test.Main");
		pct.setMaxMemory("-Xmx2048m");
		pct.setMinMemory("-Xms1024m");
		pct.setVerboseExecutionClass(true);
		pct.setVerboseExecutionGC(true);
		pct.setVerboseExecutionJNI(true);
		pct.addClasspath(new Path("one.jar"));
		pct.addClasspath(new Path("two.jar"));
		pct.addEnvironmentVariable("java.security.policy","all.policy");
		pct.addEnvironmentVariable("tmpProperty","propertyValue");
		
		
		return pct;
	}
	
}

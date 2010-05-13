package org.imirsel.nema.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NemaZipFileTest extends BaseManagerTestCase {

   ZipFile zipFile;
   NemaZipFile nemaZipFile;
   
   JarFile jarFile;
   NemaZipFile nemaJarFile;
   
   String zipFileLocation = "core/src/test/resources";
   String zipFileName = "testZipFile.zip";
   String jarFileName = "testJarFile.jar";
   String fileSep = System.getProperty("file.separator");
   
   @BeforeClass
   public static void setUpBeforeClass() throws Exception {
   }

   @AfterClass
   public static void tearDownAfterClass() throws Exception {
   }

   @Before
   public void setUp() throws Exception {
      String workingDir = System.getProperty("user.dir"); 
      zipFile = new ZipFile(workingDir + fileSep + zipFileLocation + fileSep + zipFileName);
      nemaZipFile = new NemaZipFile(zipFile);
      nemaZipFile.open();
      
      jarFile = new JarFile(workingDir + fileSep + zipFileLocation + fileSep + jarFileName);
      nemaJarFile = new NemaZipFile(jarFile);
      nemaJarFile.open();
      
      System.out.println(nemaJarFile.getSourceZipContentDir());
   }

   @After
   public void tearDown() throws Exception {
      nemaZipFile.close();
      nemaJarFile.close();
   }
   
   @Test(expected=java.lang.IllegalArgumentException.class)
   public void testIllegalUnzipDir() throws IOException {
      NemaZipFile testZipFile = new NemaZipFile(zipFile,"/a/path/that/does/not/exist");
   }
   
  
   @Test
   public void testContainsFile() {
      assertFalse(nemaZipFile.containsFile("struts-console-3.4/aFakeFileName.txt"));
      assertTrue(nemaZipFile.containsFile("struts-console-3.4/bin/console.sh"));
      assertTrue(nemaZipFile.containsFile("struts-console-3.4/org.apache.struts.console/lib/console.jar"));
      
      assertFalse(nemaJarFile.containsFile("some/bullshit/class/name/Class.class"));
      assertTrue(nemaJarFile.containsFile("LICENSE.txt"));
      assertTrue(nemaJarFile.containsFile("org/apache/struts/config/ActionConfig.class"));
   }

   @Test
   public void testContainsClass() {
      assertFalse(nemaZipFile.containsClass("org.nobody.fake.FooBar"));
      assertFalse(nemaZipFile.containsClass("org.apache.struts.console.ChildNode.class"));
      assertTrue(nemaZipFile.containsClass("org.apache.struts.console.ChildNode"));

      assertFalse(nemaJarFile.containsClass("org.apache.struts.config.Bullshit"));
      assertFalse(nemaJarFile.containsClass("org.apache.struts.config.ActionConfig.class"));
      assertTrue(nemaJarFile.containsClass("org.apache.struts.config.ActionConfig"));
   }
   
   @Test
   public void testContainsJars() {
      assertTrue(nemaZipFile.containsJars());
      assertFalse(nemaJarFile.containsJars());
   }
   
   @Test
   public void testGetSourceZipName() {
      assertTrue(nemaZipFile.getSourceZipName().equals(zipFileName));
   }
   
   @Test
   public void testGetJarList(){
	   List<String> jarList=nemaZipFile.getSourceJarPaths();
	   assertTrue(jarList.size()==4);
   }
}

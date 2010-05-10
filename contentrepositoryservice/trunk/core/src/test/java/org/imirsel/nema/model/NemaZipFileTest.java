package org.imirsel.nema.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.zip.ZipFile;

import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class NemaZipFileTest extends BaseManagerTestCase {

   NemaZipFile nemaZipFile;
   
   String zipFileLocation = "core/src/test/resources";
   String zipFileName = "testZipFile.zip";
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
      ZipFile zipFile = new ZipFile(workingDir + fileSep + zipFileLocation + fileSep + zipFileName);
      nemaZipFile = new NemaZipFile(zipFile);
      nemaZipFile.open();
   }

   @After
   public void tearDown() throws Exception {
      nemaZipFile.close();
   }
   
   @Test
   public void testContainsFile() {
      assertFalse(nemaZipFile.containsFile("struts-console-3.4/aFakeFileName.txt"));
      assertTrue(nemaZipFile.containsFile("struts-console-3.4/bin/console.sh"));
      assertTrue(nemaZipFile.containsFile("struts-console-3.4/org.apache.struts.console/lib/console.jar"));
   }

   @Test
   public void testContainsClass() {
      assertFalse(nemaZipFile.containsClass("org.nobody.fake.FooBar"));
      assertFalse(nemaZipFile.containsClass("org.apache.struts.console.ChildNode.class"));
      assertTrue(nemaZipFile.containsClass("org.apache.struts.console.ChildNode"));
   }
   
   @Test
   public void testContainsJars() {
      assertTrue(nemaZipFile.containsJars());
   }
   
   @Test
   public void testGetSourceZipName() {
      assertTrue(nemaZipFile.getSourceZipName().equals(zipFileName));
   }
}

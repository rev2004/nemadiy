package org.imirsel.nema.analysis.util.io;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import org.imirsel.nema.analytics.util.io.IOUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IOUtilTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWriteStringToFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testMakeRelative() {
		String tmpDir=System.getProperty("java.io.tmpdir");
		File base = new File(tmpDir);
		File file = new File(tmpDir,"delme"+ System.currentTimeMillis());
		file.mkdir();
		File f1 = new File(file,"anotherdelme");
		
		String val=IOUtil.makeRelative(f1, base);
		
		System.out.println(val);
		
	}

	@Test
	public void testTarAndGzipFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testTarAndGzipFileStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testTarAndGzipFileFileStringArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testWriteObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetFilteredPathStrings() {
		fail("Not yet implemented");
	}

	@Test
	public void testReadFileList() {
		fail("Not yet implemented");
	}

	@Test
	public void testListFiles() {
		String tmpFolder = System.getProperty("java.io.tmpdir")+ System.getProperty("java.file.pathseparator")+ System.currentTimeMillis();
		File f1 = new File(tmpFolder,"1.dd");
		File f2 = new File(tmpFolder,"2.dd");
		try {
			f1.createNewFile();
			f2.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		StringWriter swriter = new StringWriter();
		swriter.append(f1.getAbsolutePath()+System.getProperty("line.separator"));
		swriter.append(f2.getAbsolutePath()+System.getProperty("line.separator"));
		
		
		File outFile = new File(tmpFolder,"delme");
		String extension = "dd";
		IOUtil.listFiles(new File(tmpFolder), outFile, "dd");
		
		
		
	}

}

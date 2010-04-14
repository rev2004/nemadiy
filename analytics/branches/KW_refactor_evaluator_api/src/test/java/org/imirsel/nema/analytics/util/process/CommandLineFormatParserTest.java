package org.imirsel.nema.analytics.util.process;


import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class CommandLineFormatParserTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSimpleMelodyFormatParsing() throws Exception {
		String configString1 = "$i1{org.imirsel.nema.analytics.util.io.RawAudioFile} $o1{org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile}";
		String path1 = "/dummy/path1";
		String path2 = "/dummy/path2";
		String formatString1 = "/dummy/path1 /dummy/path2";
		
		CommandLineFormatParser parser = new CommandLineFormatParser(configString1);
		
		assert(parser.arguments != null);
		
		//check IO types
		assert(parser.getInputType(1).equals(org.imirsel.nema.analytics.util.io.RawAudioFile.class));
		assert(parser.getOutputType(1).equals(org.imirsel.nema.analytics.evaluation.melody.MelodyTextFile.class));
		
		//check properties
		//TODO implement properties check
		
		//check config string
		assert(parser.toConfigString().equals(configString1));
		
		//check formatted string
		parser.setPreparedPathForInput(1, path1);
		parser.setPreparedPathForOutput(1, path2);
		assert(parser.toFormattedString().equals(formatString1));
		
		
	}
}

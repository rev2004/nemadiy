package org.imirsel.nema.analysis.util.io;
import static org.junit.Assert.*;

import org.imirsel.nema.analytics.util.io.DeliminatedTextFileUtilities;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.Test;


public class DeliminatedTextFileUtilitiesTest extends BaseManagerTestCase{

	

	@Test
	public void testParseCommaDelimTextLine() {

		String delim = ",";
		String[] valsShouldBe = new String[]{"col_a","col_b","  col_c"};
		String testLine = "\"" + valsShouldBe[0] + "\"" + delim + valsShouldBe[1] + delim + valsShouldBe[2];
		
		String[] out = DeliminatedTextFileUtilities.parseDelimTextLine(testLine, delim);
		
		for (int i = 0; i < out.length; i++) {
			if (!out[i].equals(valsShouldBe[i])){
				fail("Values produced by parser don't match true values.\nParsed: '" + out[i] + "'" + "\nActual: '" + valsShouldBe[i] + "'");
			}
		}
	}
	

	@Test
	public void testParseTabDelimTextLine() {
		String delim = "\t";
		String[] valsShouldBe = new String[]{"col_a","col_b","  col_c"};
		String testLine = "\"" + valsShouldBe[0] + "\"" + delim + valsShouldBe[1] + delim + valsShouldBe[2];
		
		String[] out = DeliminatedTextFileUtilities.parseDelimTextLine(testLine, delim);
		
		for (int i = 0; i < out.length; i++) {
			if (!out[i].equals(valsShouldBe[i])){
				fail("Values produced by parser don't match true values.\nParsed: '" + out[i] + "'" + "\nActual: '" + valsShouldBe[i] + "'");
			}
		}
	}
	
	@Test
	public void testParseWhitespaceDelimTextLine() {
		String delim = "\\s+";
		String whitespace1 = "   ";
		String whitespace2 = " ";
		String tab = "\t";
		String[] valsShouldBe = new String[]{"col_a","col_b","col_c","col_d"};
		String testLine = "\"" + valsShouldBe[0] + "\"" + whitespace1 + valsShouldBe[1] + whitespace2 + valsShouldBe[2] + tab + valsShouldBe[3];
		
		try{
			String[] out = DeliminatedTextFileUtilities.parseDelimTextLine(testLine, delim);
			
			for (int i = 0; i < out.length; i++) {
				if (!out[i].equals(valsShouldBe[i])){
					fail("Values produced by parser don't match true values.\nParsed: '" + out[i] + "'" + "\nActual: '" + valsShouldBe[i] + "'");
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		}
		
	}

}

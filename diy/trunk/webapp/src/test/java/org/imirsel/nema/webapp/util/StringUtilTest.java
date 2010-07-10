package org.imirsel.nema.webapp.util;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class StringUtilTest {

	//StringUtil stringUtil=new StringUtil();
	@Ignore
	@Test
	public final void testDisplayNameConvert() {
		assertEquals("A Good Dog32", StringUtil.displayNameConvert("aGoodDog32"));
		assertEquals("A Good Dog ID32", StringUtil.displayNameConvert("aGoodDogID32"));
		assertEquals("A Good Dog Ww32", StringUtil.displayNameConvert("aGoodDogWw32"));		
	}
	
	
	@Test
	public void testReverseLines(){
		String lines = "Amit is here\nComing from the past\nDido";
		String reverse=getReverseLines(lines);
		System.out.println(reverse);
	}
	
	private String getReverseLines(String text) {
		String[] splits = text.split(System.getProperty("line.separator"));
		StringBuilder sbuilder = new StringBuilder();
		for(int i= splits.length-1; i<=0;i--){
			sbuilder.append(splits[i]+System.getProperty("line.separator"));
		}
		return sbuilder.toString().trim();
		
	}
	
	

}

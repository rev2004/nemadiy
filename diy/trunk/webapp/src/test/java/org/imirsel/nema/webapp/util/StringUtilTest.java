package org.imirsel.nema.webapp.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	//StringUtil stringUtil=new StringUtil();
	@Test
	public final void testDisplayNameConvert() {
		assertEquals("A Good Dog32", StringUtil.displayNameConvert("aGoodDog32"));
		assertEquals("A Good Dog ID32", StringUtil.displayNameConvert("aGoodDogID32"));
		assertEquals("A Good Dog Ww32", StringUtil.displayNameConvert("aGoodDogWw32"));		
	}

}

package org.imirsel.nema.test.matchers;

import java.io.File;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class NemaMatchers {

		  @Factory
		  public static <T> Matcher<Object> between(double start, double end) {
		    return new Between(start, end);
		  }
		  
		  @Factory
		  public static <T> Matcher<Object> fileContentContains(String content) {
			    return new FileContentContains(content);
		  }
		
		  @Factory
		  public static <T> Matcher<Object> fileContentContainsIgnoreCase(String content) {
			    return new FileContentContainsIgnoreCase(content);
		  }
		
		  @Factory
		  public static <T> Matcher<File> fileContentEquals(File file) {
			    return new FileContentEquals<File>(file);
		  }
		  
		
}
package org.imirsel.nema.test.matchers;

import org.hamcrest.Factory;
import org.hamcrest.Matcher;

public class NemaMatchers {

		  @Factory
		  public static <T> Matcher<String> between(double start, double end) {
		    return new Between(start, end);
		  }
}
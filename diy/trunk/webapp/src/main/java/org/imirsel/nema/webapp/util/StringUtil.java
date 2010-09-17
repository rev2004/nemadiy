/**
 * 
 */
package org.imirsel.nema.webapp.util;

/**
 * Helper class for String converting
 * 
 * @author gzhu1
 * 
 */
public class StringUtil {

	/**
	 * Convert a java variable name style string into a more display friendly
	 * string. For example: "redDogHTML"->"Red Dog HTML"
	 * 
	 */
	static public String displayNameConvert(String key) {
		assert !key.isEmpty() : "key is empty";
		StringBuilder newkey = new StringBuilder(key.substring(0, 1)
				.toUpperCase());
		for (int i = 1; i < key.length(); i++) {
			boolean currentCharIsUpper = Character.isUpperCase(key.charAt(i));
			boolean previousCharIsNotUpper = !Character.isUpperCase(key
					.charAt(i - 1));
			boolean previousCharIsNotSpace = key.charAt(i - 1) != ' ';
			if (currentCharIsUpper && previousCharIsNotUpper
					&& previousCharIsNotSpace) {
				newkey.append(" ");
			}
			newkey.append(key.charAt(i));
		}

		return newkey.toString();
	}
}

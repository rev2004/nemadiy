package org.imirsel.nema.util;

/**
 * Helper class for String converting
 * 
 * @author gzhu1
 * 
 */
public class StringUtil {

	/**
	 * An enhanced comparing method for String, return true, if they are same or both null.  
	 * @param str1
	 * @param str2
	 * @return
	 */
	static public boolean same(String str1,String str2){
		if (str1==null){
			if (str2==null) return true;
			else return false;
		}else return str1.equals(str2);
	}
	
	/**
	 * 
	 * @param str
	 * @return "" is str==null, itself otherwise
	 */
	static public String nonNull(String str){
		if (str==null) return "";
		else return str;
	}
	
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
	
	/**
	 * @return true if str==null or ""
	 * 
	 */
	public static boolean isEmpty(String str){
		return (str==null)||("".equals(str));
	}
	
	/**
	 * Return a short version of the str, only first "length", or the full string.  
	 * A null string will return "".  
	 * @param str
	 * @param length
	 * @return
	 */
	public static String shorten(String str,Integer length){
		str=nonNull(str);
		if (str.length()>length){
			return str.substring(0, length)+"...";
		}else {
			return str;
		}
	}
}

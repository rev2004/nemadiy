package org.imirsel.nema.model;

/** Thrown by the command line formatter class if command line options are
 * invalid
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class InvalidCommandLineFlagException extends Exception {

	/**version of this class
	 * 
	 */
	private static final long serialVersionUID = 3237847002310532421L;
	
	public InvalidCommandLineFlagException(String message) {
		super(message);
	}


}

package org.imirsel.nema.model;

/** Thrown if a user tries to insert the same parameter
 * at the same sequence in the command line
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public class ParamAlreadyExistsException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6476596092159634538L;
	
	
	public ParamAlreadyExistsException(String message){
		super(message);
	}
	
	

}

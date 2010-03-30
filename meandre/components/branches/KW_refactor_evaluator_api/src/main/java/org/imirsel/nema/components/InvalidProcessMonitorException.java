package org.imirsel.nema.components;

/**Denotes invalid process monitor
 * 
 * @author kumaramit01
 * @since 0.2.0
 */
public class InvalidProcessMonitorException extends Exception {

	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidProcessMonitorException(Exception e){
		super(e);
	}
	
	public InvalidProcessMonitorException(String message){
		super(message);
	}
	
	public InvalidProcessMonitorException(){
		super();
	}
	
	
	public InvalidProcessMonitorException(Throwable throwable){
		super(throwable);
	}
	
	
	public InvalidProcessMonitorException(String message,Throwable throwable){
		super(message,throwable);
	}
	
	
	

}

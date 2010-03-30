package org.imirsel.nema.components;

/**Denotes invalid process template
 * 
 * @author kumaramit01
 * @since 0.2.0
 */
public class InvalidProcessTemplateException extends Exception {

	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidProcessTemplateException(Exception e){
		super(e);
	}
	
	public InvalidProcessTemplateException(String message){
		super(message);
	}
	
	public InvalidProcessTemplateException(){
		super();
	}
	
	
	public InvalidProcessTemplateException(Throwable throwable){
		super(throwable);
	}
	
	
	public InvalidProcessTemplateException(String message,Throwable throwable){
		super(message,throwable);
	}
	
	
	

}

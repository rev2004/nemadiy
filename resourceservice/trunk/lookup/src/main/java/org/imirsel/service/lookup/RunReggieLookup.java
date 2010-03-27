package org.imirsel.service.lookup;

import com.sun.jini.start.ServiceStarter;

/**This Class starts Reggie. Requires a reggie.policy security policy to be setup
 * using -Djava.security.policy=reggie.policy. Also requires a reggie configuration
 * file.
 * 
 * 
 * @author kumaramit01
 * @since 0.1.0
 */
public class RunReggieLookup {
	
	public static void main(String[] args){
		ServiceStarter.main(args);
	}

}

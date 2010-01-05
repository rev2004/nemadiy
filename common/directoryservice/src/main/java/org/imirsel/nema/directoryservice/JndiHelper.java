package org.imirsel.nema.directoryservice;

/*
 * @(#) JndiHelper.java @VERSION@
 * 
 * Copyright (c) 2009+ Amit Kumar
 * 
 * The software is released under ASL 2.0, Please
 * read License.txt
 *
 */


import java.util.Hashtable;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**This class manages Jndi resources
 * 
 * @author Amit Kumar
 *
 */
public class JndiHelper {
	private static Logger logger = Logger.getAnonymousLogger();
	

	/**Returns the DataSource for the job status database -used by the probe
	 * 
	 * @return
	 * @throws Exception
	 */
	public static DataSource getJobStatusDataSource() throws Exception{
		return getDataSource(PluginConstants.JOB_JNDI_SERVICE);
	}



	private static DataSource getDataSource(String service) throws Exception {
		Context ctx;
		try{
			Hashtable env = new Hashtable();
			env.put(Context.INITIAL_CONTEXT_FACTORY,"org.mortbay.naming.InitialContextFactory");
			env.put(Context.PROVIDER_URL,"localhost:1099");
			 ctx = new InitialContext(env);
		}catch (Exception e){
			logger.severe("Error configuring initial context "+e);
			throw e;
		}
		Object obj;//object to return
		try{
			obj = ctx.lookup(service); //perform lookup
		}catch (NamingException e) {
			logger.warning("Problem looking up Object "+service+" in the java:comp/env/jdbc " +
					"JNDI namespase. Is the server namespace configured?: " + e +":"+ e.getMessage());
			throw new Exception(e);
		}
		if(obj==null){
			throw new Exception("could not find "+service);
		}else{
			return (DataSource)obj;
			
		}
	}


}

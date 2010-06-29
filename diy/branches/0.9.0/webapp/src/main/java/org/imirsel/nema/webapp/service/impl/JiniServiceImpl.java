package org.imirsel.nema.webapp.service.impl;

import java.util.List;

import org.imirsel.nema.webapp.service.JiniService;

/**Implemenation of the JiniService
 * 
 * @author kumaramit01
 * @since 0.8.0
 */
public class JiniServiceImpl implements JiniService {
	
	private String host;
	
	public JiniServiceImpl(String host){
		this.host = host;
	}
	

	@Override
	public List<String> getExecutorServiceIdList() {
		throw new RuntimeException("Not Implemented");
	}

	@Override
	public String getLookupServiceHost() {
		return host;
	}

}

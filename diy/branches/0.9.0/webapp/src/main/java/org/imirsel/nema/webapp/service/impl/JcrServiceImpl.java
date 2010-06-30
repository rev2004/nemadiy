package org.imirsel.nema.webapp.service.impl;

import org.imirsel.nema.webapp.service.JcrService;

/** JcrService Implementation
 * 
 * @author kumaramit01
 * @since 0.8.0
 */
public class JcrServiceImpl implements JcrService {
	
	private String contentRepositoryUri;
	
	public JcrServiceImpl(String contentRepositoryUri){
		this.contentRepositoryUri = contentRepositoryUri;
	}

	@Override
	public String getContentRepositoryUri() {
		return contentRepositoryUri;
	}

}

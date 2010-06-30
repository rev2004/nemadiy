package org.imirsel.nema.webapp.service;

import java.util.List;

/**Query Service for Lookup Service
 * 
 * @author kumaramit01
 * @since 0.8.0
 */
public interface JiniService {
	public String getLookupServiceHost();
	public List<String> getExecutorServiceIdList();
}

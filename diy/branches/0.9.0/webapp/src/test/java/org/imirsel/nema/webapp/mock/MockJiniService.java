/**
 * 
 */
package org.imirsel.nema.webapp.mock;

import java.util.Arrays;
import java.util.List;

import org.imirsel.nema.webapp.service.JiniService;

/**
 * @author gzhu1
 *
 */
public class MockJiniService implements JiniService {

	/* (non-Javadoc)
	 * @see org.imirsel.nema.webapp.service.JiniService#getExecutorServiceIdList()
	 */
	@Override
	public List<String> getExecutorServiceIdList() {
		
		return Arrays.asList("mockExecutorServiceId1","mockExecutorServiceId2");
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.webapp.service.JiniService#getLookupServiceHost()
	 */
	@Override
	public String getLookupServiceHost() {
	
		return "mockLookupServiceHost";
	}

}

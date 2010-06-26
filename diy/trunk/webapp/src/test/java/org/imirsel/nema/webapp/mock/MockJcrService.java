/**
 * 
 */
package org.imirsel.nema.webapp.mock;

import org.imirsel.nema.webapp.service.JcrService;

/**
 * @author gzhu1
 *
 */
public class MockJcrService implements JcrService {

	/* (non-Javadoc)
	 * @see org.imirsel.nema.webapp.service.JcrService#getContentRepositoryUri()
	 */
	@Override
	public String getContentRepositoryUri() {
		return "mockContentRepositoryUri";
	}

}

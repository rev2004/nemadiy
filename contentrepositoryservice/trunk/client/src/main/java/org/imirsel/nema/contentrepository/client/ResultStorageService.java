package org.imirsel.nema.contentrepository.client;

import javax.jcr.SimpleCredentials;
import org.imirsel.nema.model.NemaResult;
import org.imirsel.nema.model.ResourcePath;

/**
 * @author kumaramit01
 *
 */
public interface ResultStorageService {
	
	
	/**
	 * Saves the result file and returns the repository resource path.
	 * 
	 * @param credentials
	 * @param byteData
	 * @return Repository Resource Path {@code RepositoryResourcePath}
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveResultFile(final SimpleCredentials credentials,final NemaResult nemaResultFile) 
		   throws ContentRepositoryServiceException;

}

package org.imirsel.nema.contentrepository.client;

import javax.jcr.SimpleCredentials;
import org.imirsel.nema.model.NemaContentRepositoryFile;
import org.imirsel.nema.model.ResourcePath;

/**
 * @author kumaramit01
 *
 */
public interface ResultStorageService {
	
	
	/** Save the file to the content repository
	 * @param credentials
	 * @param file {@code NemaContentRepositoryFile}
	 * @return Repository Resource Path {@code RepositoryResourcePath}
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveFile(final SimpleCredentials credentials,final NemaContentRepositoryFile file)  throws ContentRepositoryServiceException;
		
		
	/**
	 * Saves the result file and returns the repository resource path.
	 * 
	 * @param credentials
	 * @param nema content repository file 
	 * @return Repository Resource Path {@code RepositoryResourcePath}
	 * @throws ContentRepositoryServiceException
	 */
	public ResourcePath saveResultFile(final SimpleCredentials credentials,final NemaContentRepositoryFile nemaResultFile) 
		   throws ContentRepositoryServiceException;
	
	
	/**
	 * Returns the NemaContentRepositoryFile
	 * @param credentials
	 * @param resourcePath
	 * @return nema result {@code NemaResult}
	 * @throws ContentRepositoryServiceException
	 */
	public NemaContentRepositoryFile getNemaContentRepositoryFile(final SimpleCredentials credentials, final ResourcePath resourcePath) 
			throws ContentRepositoryServiceException;
	

}

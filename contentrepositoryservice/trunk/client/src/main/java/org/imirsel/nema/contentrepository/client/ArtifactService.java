package org.imirsel.nema.contentrepository.client;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;


import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.ResourcePath;

/**Allows users to persist the binary code -jar file/executable zip
 * etc into the Nema content repository.
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public interface ArtifactService {
	
	/**Saves the execution bundle to the content repository
	 * 
	 * @param credentials The credentials of the the user
	 * @param instanceId The instanceId
	 * @param bundle The execution bundle
	 * @return The path location of the bundle
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public ResourcePath saveExecutableBundle(final SimpleCredentials credentials,
			final String instanceId, final ExecutableBundle bundle) throws ContentRepositoryServiceException;
	
	/**Saves the flow -the flowContent and relevant metadata
	 * 
	 * @param credentials
	 * @param flow
	 * @param flowInstanceId
	 * @param flowContent
	 * @return the path location of the flow
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public ResourcePath saveFlow(final SimpleCredentials credentials, final Flow flow, 
			final String flowInstanceId, byte[] flowContent) throws ContentRepositoryServiceException;
	
	/** Returns the executable bundle -the byte array contains the the content.
	 * 
	 * @param credentials The credentials to access the bundle
	 * @param url The url of the bundle
	 * @return the executable bundle
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public ExecutableBundle getExecutableBundle(final SimpleCredentials credentials, 
			final ResourcePath path) throws ContentRepositoryServiceException;
	
	
	/**Returns the metadata associated with a ExecutableBundle.
	 * 
	 * @param credentials
	 * @param path
	 * @return
	 * @throws RepositoryException 
	 * @throws LoginException 
	 */
	public ExecutableMetadata getBundleMetadata(final SimpleCredentials credentials, 
			final ResourcePath path) throws ContentRepositoryServiceException;
	
	
	
	
}

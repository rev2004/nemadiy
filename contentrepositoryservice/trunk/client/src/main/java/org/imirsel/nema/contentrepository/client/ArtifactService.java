package org.imirsel.nema.contentrepository.client;

import java.net.URL;

import org.imirsel.nema.model.Credentials;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;

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
	 * @return The URL location of the bundle
	 */
	public URL saveExecutionBundle(final Credentials credentials,
			final String instanceId, final ExecutableBundle bundle);
	
	/**
	 * 
	 * @param credentials
	 * @param flow
	 * @param flowInstanceId
	 * @param flowContent
	 * @return
	 */
	public URL saveFlow(final Credentials credentials, final Flow flow, 
			final String flowInstanceId, byte[] flowContent);
	
}

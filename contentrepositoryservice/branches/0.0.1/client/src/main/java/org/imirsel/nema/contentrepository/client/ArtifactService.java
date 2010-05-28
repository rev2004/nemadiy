package org.imirsel.nema.contentrepository.client;

import javax.jcr.LoginException;
import javax.jcr.RepositoryException;
import javax.jcr.SimpleCredentials;

import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.RepositoryResourcePath;
import org.imirsel.nema.model.ResourcePath;

/**
 * Allows users to persist the binary code -jar file/executable zip etc into the
 * Nema content repository.
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public interface ArtifactService {

	/**
	 * Saves the execution bundle to the content repository
	 * 
	 * @param credentials
	 *            The credentials of the the user
	 * @param instanceId
	 *            The instanceId
	 * @param bundle
	 *            The execution bundle
	 * @return The path location of the bundle
	 * @throws RepositoryException
	 * @throws LoginException
	 */
	public ResourcePath saveExecutableBundle(
			final SimpleCredentials credentials, final String instanceId,
			final ExecutableBundle bundle)
			throws ContentRepositoryServiceException;

	/**
	 * Saves the flow -the flowContent and relevant metadata
	 * 
	 * @param credentials
	 * @param flow
	 * @param flowInstanceId
	 * @param flowContent
	 * @return the path location of the flow
	 * @throws RepositoryException
	 * @throws LoginException
	 */
	public ResourcePath saveFlow(final SimpleCredentials credentials,
			final Flow flow, final String flowInstanceId, byte[] flowContent)
			throws ContentRepositoryServiceException;

	/**
	 * Returns the executable bundle -the byte array contains the the content.
	 * 
	 * @param credentials
	 *            The credentials to access the bundle
	 * @param url
	 *            The url of the bundle
	 * @return the executable bundle
	 * @throws RepositoryException
	 * @throws LoginException
	 */
	public ExecutableBundle getExecutableBundle(
			final SimpleCredentials credentials, final ResourcePath path)
			throws ContentRepositoryServiceException;

	/**
	 * Returns the metadata associated with a ExecutableBundle.
	 * 
	 * @param credentials
	 * @param path
	 * @return
	 * @throws RepositoryException
	 * @throws LoginException
	 */
	public ExecutableMetadata getBundleMetadata(
			final SimpleCredentials credentials, final ResourcePath path)
			throws ContentRepositoryServiceException;

	/**
	 * Removes an executable bundle
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return
	 * @throws ContentRepositoryServiceException
	 * @throws LoginException
	 * @throws RepositoryException
	 */
	public boolean removeExecutableBundle(final SimpleCredentials credentials,
			final ResourcePath resourcePath)
			throws ContentRepositoryServiceException;

	/**
	 * Checks if a resource path exists or not.
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return true or false
	 * @throws ContentRepositoryServiceException
	 */
	public boolean exists(final SimpleCredentials credentials,
			final ResourcePath resourcePath)
			throws ContentRepositoryServiceException;

	/**
	 * Validates the node types that are required to be present in the content
	 * repository. flow:file and exec:file nodes that extend the nt:file
	 * datatype
	 * 
	 * @param credentials
	 * @return boolean true/false
	 * @throws ContentRepositoryServiceException
	 */
	public boolean validateNodeTypes(final SimpleCredentials credentials)
			throws ContentRepositoryServiceException;

	/**
	 * Returns the file system path for the resource
	 * 
	 * @param credentials
	 * @param resourcePath
	 * @return
	 * @throws ContentRepositoryServiceException
	 */
	public String getExecutableBundleFSPath(
			final SimpleCredentials credentials,
			final ResourcePath resourcePath)
			throws ContentRepositoryServiceException;

}

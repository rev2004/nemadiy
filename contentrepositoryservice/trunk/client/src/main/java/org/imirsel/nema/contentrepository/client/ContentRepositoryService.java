package org.imirsel.nema.contentrepository.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.AccessDeniedException;
import javax.jcr.InvalidItemStateException;
import javax.jcr.ItemExistsException;

import javax.jcr.ReferentialIntegrityException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.version.VersionException;
import javax.jcr.LoginException;

import org.apache.jackrabbit.rmi.client.ClientRepositoryFactory;
import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.NemaCredentials;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.ResourcePath;

public class ContentRepositoryService implements ArtifactService {
	
	private ClientRepositoryFactory factory;
	private String rmiURL;
	private NemaCredentials credentials;
	private Repository repository;
	
	
	public void init(String rmiURL) throws MalformedURLException, ClassCastException, RemoteException, NotBoundException{
		this.rmiURL=rmiURL;
		repository = factory.getRepository(this.rmiURL);
	}

	public ResourcePath saveExecutableBundle(NemaCredentials credentials, String instanceId,
			ExecutableBundle bundle) throws ContentRepositoryServiceException {
		try {
			Session session = null;
			session = repository.login(credentials);
			session.save();
			session.logout();
		}catch (LoginException e){
			throw new ContentRepositoryServiceException(e);
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}
		return null;
	}

	public ResourcePath saveFlow(NemaCredentials credentials, Flow flow,
			String flowInstanceId, byte[] flowContent) throws ContentRepositoryServiceException {
		try {
			Session session = null;
			session = repository.login(credentials);
			session.save();
			session.logout();
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}
		 
		  return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials, 
			String path) throws ContentRepositoryServiceException {
	
		try {
			Session session = null;
			session = repository.login(credentials);
			session.save();
			session.logout();
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}
		 
	
		return null;
	}

	public ExecutableBundle getExecutableBundle(NemaCredentials credentials,
			ResourcePath path) throws ContentRepositoryServiceException {
		try {
			Session session = null;
			session = repository.login(credentials);
			session.save();
			session.logout();
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}
		 
	
		return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials,
			ResourcePath path) throws ContentRepositoryServiceException{
		try {
			Session session = null;
			session = repository.login(credentials);
			session.save();
			session.logout();
		} catch (AccessDeniedException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ItemExistsException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ReferentialIntegrityException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (ConstraintViolationException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (InvalidItemStateException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (VersionException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (LockException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (NoSuchNodeTypeException e) {
			throw new ContentRepositoryServiceException(e);
		} catch (RepositoryException e) {
			throw new ContentRepositoryServiceException(e);
		}
		 
		  return null;
	
	}

	public void setCredentials(NemaCredentials credentials) {
		this.credentials = credentials;
	}

	public NemaCredentials getCredentials() {
		return credentials;
	}


}

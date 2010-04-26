package org.imirsel.nema.contentrepository.client;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.LoginException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

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
			ExecutableBundle bundle) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourcePath saveFlow(NemaCredentials credentials, Flow flow,
			String flowInstanceId, byte[] flowContent) throws LoginException, RepositoryException {
		  Session session = repository.login(credentials);
		  session.save();
		  session.logout();
		  return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials, String path) throws LoginException, RepositoryException {
		  Session session = repository.login(credentials);
		  session.save();
		  session.logout();
	
		return null;
	}

	public ExecutableBundle getExecutableBundle(NemaCredentials credentials,
			ResourcePath path) throws LoginException, RepositoryException {
		  Session session = repository.login(credentials);
		  session.save();
		  session.logout();
	
		return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials,
			ResourcePath path) throws LoginException, RepositoryException {
		  Session session = repository.login(credentials);
		  session.save();
		  session.logout();
		  return null;
	
	}

	public void setCredentials(NemaCredentials credentials) {
		this.credentials = credentials;
	}

	public NemaCredentials getCredentials() {
		return credentials;
	}


}

package org.imirsel.nema.contentrepository.client;

import org.imirsel.nema.model.ExecutableMetadata;
import org.imirsel.nema.model.NemaCredentials;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.ResourcePath;

public class ContentRepositoryService implements ArtifactService {

	public ResourcePath saveExecutableBundle(NemaCredentials credentials, String instanceId,
			ExecutableBundle bundle) {
		// TODO Auto-generated method stub
		return null;
	}

	public ResourcePath saveFlow(NemaCredentials credentials, Flow flow,
			String flowInstanceId, byte[] flowContent) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials, String path) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExecutableBundle getExecutableBundle(NemaCredentials credentials,
			ResourcePath path) {
		// TODO Auto-generated method stub
		return null;
	}

	public ExecutableMetadata getBundleMetadata(NemaCredentials credentials,
			ResourcePath path) {
		// TODO Auto-generated method stub
		return null;
	}


}

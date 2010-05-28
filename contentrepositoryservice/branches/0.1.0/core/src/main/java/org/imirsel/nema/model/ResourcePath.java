package org.imirsel.nema.model;

import java.io.Serializable;

/** Implemented by the RepositoryResourcePath
 * 
 * @author kumaramit01
 * @since 0.0.1
 */
public interface ResourcePath extends Serializable {
	
	// returns the node path.
	public String getPath();
	// returns the workspace of the resource
	public String getWorkspace();
	
	// returns the protocol jcr/http etc.
	public String getProtocol();


}

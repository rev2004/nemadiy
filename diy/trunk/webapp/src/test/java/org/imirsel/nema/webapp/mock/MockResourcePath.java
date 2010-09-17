/**
 * 
 */
package org.imirsel.nema.webapp.mock;

import org.imirsel.nema.model.ResourcePath;

/**
 * @author gzhu1
 *
 */
public class MockResourcePath implements ResourcePath {

	/**
	 * 
	 */
	private static final long serialVersionUID = -779257993404882450L;
	private String path;
	private String protocol;
	private String workspace;
	/* (non-Javadoc)
	 * @see org.imirsel.nema.model.ResourcePath#getPath()
	 */
	@Override
	public String getPath() {
		
		return path;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.model.ResourcePath#getProtocol()
	 */
	@Override
	public String getProtocol() {
		return protocol;
	}

	/* (non-Javadoc)
	 * @see org.imirsel.nema.model.ResourcePath#getWorkspace()
	 */
	@Override
	public String getWorkspace() {
		return workspace;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	@Override
	public String getURIAsString() {
		
		return getProtocol()+":"+ getWorkspace() +"://"+getPath();
	}

	

}

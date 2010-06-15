package org.imirsel.nema.model;

import java.io.Serializable;

/**Stores the resource location.
 * 
 * @author kumaramit01
 * @since 0.0.1 
 */
public class RepositoryResourcePath implements Serializable, ResourcePath{
	/**
	 * Version of this class.
	 */
	private static final long serialVersionUID = -8696944504877708390L;
	private String path;
	private String workspace;
	private String protocol;
	
	/**
	 * Creates a new Repository Resource Path object with the protocol, workspace
	 * and path information
	 * 
	 * @param protocol "jcr" is the supported protocol
	 * @param workspace "default" is the supported workspace
	 * @param path
	 */
	public RepositoryResourcePath(String protocol, String workspace,String path){
		this.path = path;
		if(workspace==null){
			throw new IllegalArgumentException("Workspace cannot be null");
		}
		if(protocol==null){
			throw new IllegalArgumentException("Protocol cannot be null");
		}
		this.workspace = workspace;
		this.protocol = protocol;
		
	}

	public String getPath() {
		return path;
	}

	public void setWorkspace(String workspace) {
		this.workspace = workspace;
	}

	public String getWorkspace() {
		return workspace;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}



	public String getProtocol() {
		return protocol;
	}
	
	public String getURIAsString() {
		return this.getProtocol()+":"+ this.getWorkspace()+"://"+ this.getPath();
	}

	



	public boolean equals(Object object){
		if(!(object instanceof RepositoryResourcePath)){
			return false;
		}
		if(this==object){
			return true;
		}
		RepositoryResourcePath other = (RepositoryResourcePath) object;
		if(other.path==null && this.path==null){
			return true;
		}
		if(other.path!=null){
			return this.path.equals(other.path) && this.workspace.equals(other.workspace);
		}
		if(this.path!=null){
			return this.path.equals(other.path) &&  this.workspace.equals(other.workspace);
		}
		return false;
	}
	
	public int hashCode() { 
	    int hash = 1;
	    hash = hash * 31 + this.path.hashCode() + this.workspace.hashCode() + this.protocol.hashCode();
	    return hash;
	 }


	

}

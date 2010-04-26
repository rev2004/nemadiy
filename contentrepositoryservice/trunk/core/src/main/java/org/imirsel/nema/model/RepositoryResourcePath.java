package org.imirsel.nema.model;

/**Stores the resource location.
 * 
 * @author kumaramit01
 * @since 0.0.1 
 */
public class RepositoryResourcePath implements ResourcePath{
	private String path;

	public RepositoryResourcePath(String path){
		this.path = path;
	}

	public String getPath() {
		return path;
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
			return this.path.equals(other.path);
		}
		if(this.path!=null){
			return this.path.equals(other.path);
		}
		return false;
	}
	
	public int hashCode() { 
	    int hash = 1;
	    hash = hash * 31 + this.path.hashCode();
	    return hash;
	 }

	
	

}

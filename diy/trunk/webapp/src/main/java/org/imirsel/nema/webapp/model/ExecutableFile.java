package org.imirsel.nema.webapp.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * Model class for executable file web flow
 * @author gzhu1
 * 
 */
public class ExecutableFile {
	private transient MultipartFile file;

	public void processFile() {
		
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	public enum Type{JAR,ZIP,BIN};
	
	private Type type;
	private String args;

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public MultipartFile getFile() {
		return file;
	}
	
		
}

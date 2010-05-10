package org.imirsel.nema.webapp.model;

import java.io.InvalidObjectException;
import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.web.multipart.MultipartFile;


/**
 * Model class for executable file web flow
 * 
 * @author gzhu1
 * 
 */
public class ExecutableFile implements Serializable {
	static private Log logger = LogFactory.getLog(ExecutableFile.class);
	private static final long serialVersionUID = 1509804396923336290L;

	
	public enum FileType{
		JAR("jar file or zip of jar files", 0), MATLAB("matlab programs", 1), PLAIN(
				"plain c, c++ or shell", 2);
		private String label;
		private int code;

		FileType(final String label, int code) {
			this.label = label;
			this.code = code;
		}
		public int getCode(){
			return this.code;
		}
		
		public String getLabel() {
			return label;
		}
		public String toString(){
			return label;			
		}
		public static FileType valueOf(int code) throws InvalidObjectException{
			switch (code){
			case 0:return FileType.JAR; 
			case 1:return FileType.MATLAB;
			case 2:return FileType.PLAIN;
			default:throw new InvalidObjectException("can't deserialize FileType  for "+code );
			}
		}
	}
	
	public static FileType[] getTypeSet(){
		return new FileType[]{FileType.JAR,FileType.MATLAB,FileType.PLAIN};
	}

	private  MultipartFile file;

	private FileType fileType=FileType.PLAIN;

	private String args;;

	private String environment;
	

	public String getArgs() {
		return args;
	}

	public String getEnvironment() {
		return environment;
	}

	public MultipartFile getFile() {
		return file;
	}

	public int getType() {
		return fileType.getCode();
	}
	

	public void processFile() {

	}

	public void setArgs(String args) {
		this.args = args;
	}

	public void setEnvironment(String environment) {
		this.environment = environment;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public void setType(int typeCode)  {
		
		try {
			this.fileType = FileType.valueOf(typeCode);
		} catch (InvalidObjectException e) {
			logger.error(e,e);
		}
	}

	public void setFileType(FileType fileType) {
		this.fileType = fileType;
	}

	public FileType getFileType() {
		return fileType;
	}

}
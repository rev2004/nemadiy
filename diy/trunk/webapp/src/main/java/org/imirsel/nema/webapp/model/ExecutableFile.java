package org.imirsel.nema.webapp.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.ResourceTypeService;
import org.imirsel.nema.contentrepository.client.ResourceTypeServiceImpl;
import org.imirsel.nema.model.PredefinedCommandTemplate;

import org.springframework.web.multipart.MultipartFile;


/**
 * Model class for executable file web flow
 * 
 * @author gzhu1
 * 
 */
public class ExecutableFile implements Serializable {
	private static final Log logger = LogFactory.getLog(ExecutableFile.class.getName());
	private static final long serialVersionUID = 1509804396923336290L;

	protected ResourceTypeService resourceTypeService=new ResourceTypeServiceImpl();
	
	public enum ExecutableType{
		JAVA("Java", 0), MATLAB("MATLAB", 1), C("C/C++",2), SHELL("Shell",3);
		private String label;
		private int code;

		ExecutableType(final String label, int code) {
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
		public static ExecutableType valueOf(int code) throws IllegalArgumentException{
			switch (code){
			case 0:return ExecutableType.JAVA; 
			case 1:return ExecutableType.MATLAB;
			case 2:return ExecutableType.C;
			case 3:return ExecutableType.SHELL;
			default:throw new IllegalArgumentException("Unknown ExecutableType code: " + code );
			}
		}
	}
	
	public ExecutableType[] getTypeOptions(){
		return new ExecutableType[]{ExecutableType.JAVA,ExecutableType.MATLAB,ExecutableType.C,ExecutableType.SHELL};
	}

	private transient MultipartFile file;

	private String fileName;
	private ExecutableType type = ExecutableType.JAVA;
	private String args;
	private String environment;	
	private PredefinedCommandTemplate template;
	private String group;
	protected String os;

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getArgs() {
		return args;
	}

	public String getEnvironment() {
		return environment;
	}

	public MultipartFile getFile() {
		return file;
	}

	public int getTypeCode() {
		return type.getCode();
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
	   this.fileName = file.getOriginalFilename();
	}

	public void setTypeCode(int typeCode)  {
		try {
			this.type = ExecutableType.valueOf(typeCode);
		} catch (IllegalArgumentException e) {
			logger.error(e,e);
		}
	}

	public void setType(ExecutableType fileType) {
		this.type = fileType;
	}

	public ExecutableType getType() {
		return type;
	}

	public void setTemplate(PredefinedCommandTemplate template) {
		this.template = template;
	}

	public PredefinedCommandTemplate getTemplate() {
		return template;
	}

	public void generateCommandline(){
		
	}

	public String getFileName() {
	   return fileName;
	}
}

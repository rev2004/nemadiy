package org.imirsel.nema.model;

import java.io.Serializable;
/**
 * @author kumaramit01
 *
 */
public class FileDataType  implements Serializable{
	/** Version of this class
	 * 
	 */
	private static final long serialVersionUID = -4037324168549003152L;
	private String name;
	private String value;

	/**public constructor
	 * 
	 * @param name
	 * @param value
	 */
	public FileDataType(String name, String value) {
		this.name= name;
		this.value=value;
	}

	/**Set name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**Get name
	 * 
	 * @return name of the file data type
	 */
	public String getName() {
		return name;
	}

	/**Set value
	 * 
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**Return value
	 * 
	 * @return value of the file data type
	 */
	public String getValue() {
		return value;
	}
	
	public int hashCode(){
		int hash = 31;
		hash=hash+ this.name.hashCode() + this.value.hashCode();
		return hash;
	}
	
	public boolean equals(Object object){
		if(!(object instanceof FileDataType)){
			return false;
		}
		FileDataType fdt = (FileDataType) object;
		return this.getName().equals(fdt.name) && this.getValue().equals(fdt.value);
	}

}

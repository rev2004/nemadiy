/**
 * 
 */
package org.imirsel.nema.analytics.util.process;

import java.util.Iterator;
import java.util.Map;

import org.imirsel.nema.analytics.util.io.NemaFileType;

class FileCommandArgument implements CommandArgument{
	private boolean isOutput;
	private String preparedPath;
	private Class<? extends NemaFileType> fileType;
	private Map<String,String> properties;
	boolean followedBySpace;
	int ioIndex;
	
	public FileCommandArgument(boolean isOutput, 
			Class<? extends NemaFileType> fileType,
			Map<String,String> properties,
			boolean followedBySpace,
			int ioIndex) {
		this.isOutput = isOutput;
		this.fileType = fileType;
		this.properties = properties;
		this.followedBySpace = followedBySpace;
		this.ioIndex = ioIndex;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}

	public int getIoIndex() {
		return ioIndex;
	}

	public void setIoIndex(int ioIndex) {
		this.ioIndex = ioIndex;
	}

	public void setFollowedBySpace(boolean followedBySpace) {
		this.followedBySpace = followedBySpace;
	}

	public Class<? extends NemaFileType> getFileType() {
		return fileType;
	}

	public void setFileType(Class<? extends NemaFileType> fileType) {
		this.fileType = fileType;
	}

	public String getPreparedPath() {
		return preparedPath;
	}

	public void setPreparedPath(String path) {
		preparedPath = path;
	}
	
	public void clearPreparedPath() {
		preparedPath = null;
	}
	
	public boolean isOutput() {
		return isOutput;
	}

	public void setOutput(boolean isOutput) {
		this.isOutput = isOutput;
	}


	private static String producePropertiesString(Map<String,String> map) {
		String out = "";
		for (Iterator<String> iterator = map.keySet().iterator(); iterator
				.hasNext();) {
			String key = iterator.next();
			String val = map.get(key);
			out += key + "=" + val;
			if(iterator.hasNext()) {
				out += ",";
			}
		}
		return out;
	}
	
	public String toConfigString() {
		String out = "$";
		if (isOutput) {
			out += "o";
		}else {
			out += "i";
		}
		out += ioIndex + "{";
		out += this.fileType.getName();
		if (this.properties != null) {
			out += "(" + producePropertiesString(this.properties) + ")";
		}
		out += "}";
		
		return out;
	}
	
	public String toFormattedString() {
		if(preparedPath == null) {
			throw new IllegalArgumentException("No path prepared for InputFileComandComponent. A path must be set before the formatted String can be returned.");
		}
		return preparedPath;
	}
	
	public boolean followedBySpace() {
		return followedBySpace;
	}
}
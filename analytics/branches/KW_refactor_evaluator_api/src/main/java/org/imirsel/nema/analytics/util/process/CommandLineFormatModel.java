package org.imirsel.nema.analytics.util.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.imirsel.nema.analytics.util.io.NemaFileType;

public class CommandLineFormatModel {

	Logger _logger;
	List<CommandComponent> components = null;
	Map<Integer,FileCommandComponent> inputs = null;
	Map<Integer,FileCommandComponent> outputs = null;
	
	/**
	 * @param components
	 * @param inputs
	 * @param outputs
	 */
	public CommandLineFormatModel(List<CommandComponent> components,
			Map<Integer, FileCommandComponent> inputs,
			Map<Integer, FileCommandComponent> outputs) {
		this.components = components;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public Logger getLogger() {
		return _logger;
	}
	
	public CommandLineFormatModel(String commandFormatString) throws
			IllegalArgumentException{
		components = new ArrayList<CommandComponent>();
		inputs = new HashMap<Integer,FileCommandComponent>();
		outputs = new HashMap<Integer,FileCommandComponent>();
		
		
		int idx = 0;
		int lastIdx = 0;
		
		while(idx<commandFormatString.length()) {
			if (commandFormatString.charAt(idx) == ' ') {
				//ending a string component
				components.add(new StringCommandComponent(commandFormatString.substring(lastIdx, idx),true));
				idx++;
				lastIdx = idx;
			}else if(commandFormatString.charAt(idx) == '$') {
				//inside a file component
				//clear up any trailing strings
				if(idx != lastIdx) {
					components.add(new StringCommandComponent(commandFormatString.substring(lastIdx, idx),false));
				}
				idx++;
				
				//input or output?
				boolean isOutput;
				if(commandFormatString.charAt(idx) == 'i') {
					isOutput = false;
				}else if(commandFormatString.charAt(idx) == 'o') {
					isOutput = true;
				}else {
					throw new IllegalArgumentException("Unable to determine whether File argument is input or output at position " + idx + " in: " + commandFormatString);
				}
				idx++;
				lastIdx = idx;
				
				//get index (search forward for { then parse)
				try {
					while(commandFormatString.charAt(idx) != '{'){
						idx++;
					}
				}catch(IndexOutOfBoundsException e) {
					throw new IllegalArgumentException("End of string reached while seeking type argument opening { in: " + commandFormatString);
				}
				
				int ioIndex;
				try {
					ioIndex = Integer.parseInt(commandFormatString.substring(lastIdx,idx));
				}catch (NumberFormatException e) {
					throw new IllegalArgumentException("Failed to parse IO index (" + commandFormatString.substring(lastIdx,idx) + ") in: " + commandFormatString,e);
				}
				idx++;
				lastIdx = idx;
				
				//get file format
				//search forward for close } or arguments (
				try {
					while(commandFormatString.charAt(idx) != '}' && commandFormatString.charAt(idx) != '('){
						idx++;
					}
				}catch(IndexOutOfBoundsException e) {
					throw new IllegalArgumentException("End of string reached while seeking type argument close } or properties opening ( in: " + commandFormatString);
				}
				String typeStr = commandFormatString.substring(lastIdx,idx);
				Class<? extends NemaFileType> typeClass;
				try {
					typeClass = (Class<? extends NemaFileType>)Class.forName(typeStr);
				}catch(Exception e) {
					throw new IllegalArgumentException("Failed to interpret valid file type from '" + typeStr + "' in: " + commandFormatString,e);
				}
				
				//grab any properties
				Map<String,String> props = null;
				if (commandFormatString.charAt(idx) == '(') {
					idx++;
					lastIdx = idx;
					
					try {
						while(commandFormatString.charAt(idx) != ')'){
							idx++;
						}
					}catch(IndexOutOfBoundsException e) {
						throw new IllegalArgumentException("End of string reached while seeking properties closing ) in: " + commandFormatString);
					}
					String propertiesStr = commandFormatString.substring(lastIdx,idx);
					props = parsePropertiesString(propertiesStr);	
					idx++;
				}
				
				//last char should be }
				if (commandFormatString.charAt(idx) != '}') {
					throw new IllegalArgumentException("Expected closing } at position " + idx + " in: " + commandFormatString);
				}
				idx++;
				
				//check for trailing space
				boolean followedBySpace = false;
				if (idx < commandFormatString.length() && commandFormatString.charAt(idx) == ' ') {
					followedBySpace = true;
				}
				
				FileCommandComponent fileComp = new FileCommandComponent(isOutput, typeClass, props, followedBySpace, ioIndex);
				components.add(fileComp);
				if (isOutput) {
					outputs.put(ioIndex,fileComp);
				}else {
					inputs.put(ioIndex,fileComp);
				}
			}else {
				idx++;
			}
		}
	}
	
	public static Map<String,String> parsePropertiesString(String propsString) throws IllegalArgumentException{
		Map<String,String> map = new HashMap<String,String>();
		if (propsString.trim().equals("")) {
			return map;
		}
		
		String[] comps = propsString.split(",");
		for (int i = 0; i < comps.length; i++) {
			String[] keyValPair = comps[i].split("=");
			if(keyValPair.length != 2) {
				throw new IllegalArgumentException("Wrong number of components for properties component '" + comps[i] + "' of properties String: " + propsString);
			}
			map.put(keyValPair[0], keyValPair[1]);
		}
		return map;
	}
	
	public static String producePropertiesString(Map<String,String> map) {
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
		String out = "";
		for (Iterator<CommandComponent> iterator = components.iterator(); iterator
				.hasNext();) {
			CommandComponent comp = iterator.next();
			out += comp.toConfigString();
			if(comp.followedBySpace()) {
				out += " ";
			}
		}
		return out;
	}
	
	public String toFormattedString() throws IllegalArgumentException{
		String out = "";
		for (Iterator<CommandComponent> iterator = components.iterator(); iterator
				.hasNext();) {
			CommandComponent comp = iterator.next();
			out += comp.toFormattedString();
			if(comp.followedBySpace()) {
				out += " ";
			}
		}
		return out;
	}
	
	public Map<Integer,FileCommandComponent> getInputs() {
		return inputs;
	}

	public Map<Integer,FileCommandComponent> getOutputs() {
		return outputs;
	}

	
	
	
	
	interface CommandComponent{
		public String toConfigString();
		public String toFormattedString();
		public boolean followedBySpace();
	}
	
	class StringCommandComponent implements CommandComponent{
		private String string;
		boolean followedBySpace;
		
		public StringCommandComponent(String string, boolean followedBySpace) {
			this.string = string;
			this.followedBySpace = followedBySpace;
		}
		
		public String getString() {
			return string;
		}

		public void setString(String string) {
			this.string = string;
		}

		public String toConfigString() {
			return string;
		}
		
		public String toFormattedString() {
			return string;
		}
		
		public boolean followedBySpace() {
			return followedBySpace;
		}
	}

	class FileCommandComponent implements CommandComponent{
		private boolean isOutput;
		private String preparedPath;
		private Class<? extends NemaFileType> fileType;
		private Map<String,String> properties;
		boolean followedBySpace;
		int ioIndex;
		
		public FileCommandComponent(boolean isOutput, 
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
		
		public boolean isOutput() {
			return isOutput;
		}

		public void setOutput(boolean isOutput) {
			this.isOutput = isOutput;
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
	
	
}

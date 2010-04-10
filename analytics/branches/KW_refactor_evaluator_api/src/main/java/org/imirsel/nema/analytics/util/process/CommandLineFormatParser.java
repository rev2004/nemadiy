package org.imirsel.nema.analytics.util.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.imirsel.nema.analytics.util.io.NemaFileType;

/**
 * 
 * @author kris.west@gmail.com
 * @since 0.2.0
 */
public class CommandLineFormatParser {

	List<CommandArgument> arguments = null;
	private Map<Integer,FileCommandArgument> inputs = null;
	private Map<Integer,FileCommandArgument> outputs = null;
	
//	Map<Integer,Class<? extends NemaFileType>> inputTypes = null;
//	Map<Integer,Class<? extends NemaFileType>> outputTypes = null;
//	Map<Integer,Map<String,String>> inputProperties = null;
//	Map<Integer,Map<String,String>> outputProperties = null;
	

	public CommandLineFormatParser(List<CommandArgument> components,
			Map<Integer, Class<? extends NemaFileType>> inputTypes,
			Map<Integer, Class<? extends NemaFileType>> outputTypes,
			Map<Integer,Map<String,String>> inputProperties,
			Map<Integer,Map<String,String>> outputProperties,
			Map<Integer,Boolean> inputArgumentFollowedBySpace,
			Map<Integer,Boolean> outputArgumentFollowedBySpace) {
		this.arguments = components;
		this.inputs = new HashMap<Integer,FileCommandArgument>();
		this.outputs = new HashMap<Integer,FileCommandArgument>();
		for (Iterator<Integer> iterator = inputTypes.keySet().iterator(); iterator.hasNext();) {
			Integer ioIndex = iterator.next();
			this.inputs.put(ioIndex, new FileCommandArgument(false, inputTypes.get(ioIndex), inputProperties.get(ioIndex), inputArgumentFollowedBySpace.get(ioIndex), ioIndex));
		}
		
		for (Iterator<Integer> iterator = outputTypes.keySet().iterator(); iterator.hasNext();) {
			Integer ioIndex = iterator.next();
			this.outputs.put(ioIndex, new FileCommandArgument(true, outputTypes.get(ioIndex), outputProperties.get(ioIndex), outputArgumentFollowedBySpace.get(ioIndex), ioIndex));
		}
		
//		this.inputTypes = inputs;
//		this.outputTypes = outputs;
//		this.inputProperties = inputProperties;
//		this.outputProperties = outputProperties;
	}

	public CommandLineFormatParser(String commandFormatString) throws
			IllegalArgumentException{
		this.arguments = new ArrayList<CommandArgument>();
		
		this.inputs = new HashMap<Integer,FileCommandArgument>();
		this.outputs = new HashMap<Integer,FileCommandArgument>();
		
//		this.inputTypes = new HashMap<Integer,Class<? extends NemaFileType>>();
//		this.outputTypes = new HashMap<Integer,Class<? extends NemaFileType>>();
//		this.inputProperties = new HashMap<Integer,Map<String,String>>();
//		this.outputProperties = new HashMap<Integer,Map<String,String>>();
		
		int idx = 0;
		int lastIdx = 0;
		
		while(idx<commandFormatString.length()) {
			if (commandFormatString.charAt(idx) == ' ') {
				//ending a string component
				arguments.add(new StringCommandArgument(commandFormatString.substring(lastIdx, idx),true));
				idx++;
				lastIdx = idx;
			}else if(commandFormatString.charAt(idx) == '$') {
				//inside a file component
				//clear up any trailing strings
				if(idx != lastIdx) {
					arguments.add(new StringCommandArgument(commandFormatString.substring(lastIdx, idx),false));
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
				
				FileCommandArgument fileComp = new FileCommandArgument(isOutput, typeClass, props, followedBySpace, ioIndex);
				arguments.add(fileComp);
				if (isOutput) {
					outputs.put(ioIndex,fileComp);
//					outputTypes.put(ioIndex,typeClass);
//					outputProperties.put(ioIndex, props);
				}else {
					inputs.put(ioIndex,fileComp);
//					inputTypes.put(ioIndex,typeClass);
//					inputProperties.put(ioIndex, props);
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
				throw new IllegalArgumentException("Wrong number of arguments for properties component '" + comps[i] + "' of properties String: " + propsString);
			}
			map.put(keyValPair[0], keyValPair[1]);
		}
		return map;
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
		String out = "";
		for (Iterator<CommandArgument> iterator = arguments.iterator(); iterator
				.hasNext();) {
			CommandArgument comp = iterator.next();
			out += comp.toConfigString();
			if(comp.followedBySpace()) {
				out += " ";
			}
		}
		return out;
	}
	
	public void setPreparedPathForInput(int ioIndex, String path) {
		inputs.get(ioIndex).setPreparedPath(path);
	}
	
	public void setPreparedPathForOutput(int ioIndex, String path) {
		outputs.get(ioIndex).setPreparedPath(path);
	}
	
	public void clearPreparedPaths() {
		for (Iterator<FileCommandArgument> iterator = inputs.values().iterator(); iterator.hasNext();) {
			iterator.next().clearPreparedPath();
		}
		for (Iterator<FileCommandArgument> iterator = outputs.values().iterator(); iterator.hasNext();) {
			iterator.next().clearPreparedPath();
		}
	}
	
	public String toFormattedString() throws IllegalArgumentException{
		String out = "";
		for (Iterator<CommandArgument> iterator = arguments.iterator(); iterator
				.hasNext();) {
			CommandArgument comp = iterator.next();
			out += comp.toFormattedString();
			if(comp.followedBySpace()) {
				out += " ";
			}
		}
		return out;
	}
		
	
	
	
//	public Map<Integer, Class<? extends NemaFileType>> getInputs() {
//		return inputTypes;
//	}
//
//	public void setInputs(Map<Integer, Class<? extends NemaFileType>> inputs) {
//		this.inputTypes = inputs;
//	}
//
//	public Map<Integer, Class<? extends NemaFileType>> getOutputs() {
//		return outputTypes;
//	}
//
//	public void setOutputs(Map<Integer, Class<? extends NemaFileType>> outputs) {
//		this.outputTypes = outputs;
//	}
//
//	public Map<Integer, Map<String, String>> getInputProperties() {
//		return inputProperties;
//	}
//
//	public void setInputProperties(Map<Integer, Map<String, String>> inputProperties) {
//		this.inputProperties = inputProperties;
//	}
//
//	public Map<Integer, Map<String, String>> getOutputProperties() {
//		return outputProperties;
//	}
//
//	public void setOutputProperties(
//			Map<Integer, Map<String, String>> outputProperties) {
//		this.outputProperties = outputProperties;
//	}





	public Map<Integer, FileCommandArgument> getInputs() {
		return inputs;
	}

	public void setInputs(Map<Integer, FileCommandArgument> inputs) {
		this.inputs = inputs;
	}

	public Map<Integer, FileCommandArgument> getOutputs() {
		return outputs;
	}

	public void setOutputs(Map<Integer, FileCommandArgument> outputs) {
		this.outputs = outputs;
	}

	public Class<? extends NemaFileType> getInputType(int inputIdx){
		FileCommandArgument arg = inputs.get(inputIdx);
		if(arg == null) {
			return null;
		}
		return arg.getFileType();
	}

	public Class<? extends NemaFileType> getOutputType(int outputIdx){
		FileCommandArgument arg = outputs.get(outputIdx);
		if(arg == null) {
			return null;
		}
		return arg.getFileType();
	}
	
	public Map<String,String> getInputProperties(int inputIdx){
		FileCommandArgument arg = inputs.get(inputIdx);
		if(arg == null) {
			return null;
		}
		return arg.getProperties();
	}

	public Map<String,String> getOutputProperties(int outputIdx){
		FileCommandArgument arg = outputs.get(outputIdx);
		if(arg == null) {
			return null;
		}
		return arg.getProperties();
	}



	interface CommandArgument{
		public String toConfigString();
		public String toFormattedString();
		public boolean followedBySpace();
	}
	
	class StringCommandArgument implements CommandArgument{
		private String string;
		boolean followedBySpace;
		
		public StringCommandArgument(String string, boolean followedBySpace) {
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

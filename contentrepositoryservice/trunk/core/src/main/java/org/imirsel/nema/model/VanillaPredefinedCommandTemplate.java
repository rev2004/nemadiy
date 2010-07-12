package org.imirsel.nema.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** A predefined command template implementation that supports only 
 * environment variables setup for shell and c code.
 * 
 * @author kumaramit01
 * @since 0.0.1
 * @since 0.0.4 add FunctionCall field ( It covers input/output and maybe some other things.  )
 */
public class VanillaPredefinedCommandTemplate implements
		PredefinedCommandTemplate, Serializable{
	
	
	/**version of this class
	 * 
	 */
	private static final long serialVersionUID = -3307989303515424283L;
	private Map<String,String> environmentMap = new HashMap<String,String>();
	private List<Param> execParams = new ArrayList<Param>();
	private String executableName;
	private String functionCall;
	
	
	public void addEnvironmentVariable(String key, String value){
		this.environmentMap.put(key, value);
	}
	
	public Map<String,String> getEnvironmentMap(){
		return this.environmentMap;
	}
	
	
	public void addParam(Param param) throws ParamAlreadyExistsException{
		if(execParams.contains(param)){
			throw new ParamAlreadyExistsException("Param " + param.getValue() + " alread exists with the sequence number " + param.getSequence());
		}
		execParams.add(param);
	}

	public List<Param> getParams() {
		return execParams;
	}

	public String getExecutableName() {
		return executableName;
	}

	public void setExecutableName(String executableName) {
		this.executableName = executableName;
	}

	public List<Param> getExecParams() {
		return execParams;
	}

	public void setEnvironmentMap(Map<String, String> environmentMap) {
		this.environmentMap = environmentMap;
	}

	public void setParams(List<Param> execParams) {
		this.execParams = execParams;
	}
	

	public String getFunctionCall() {
		return functionCall;
	}
	
	
	public void setFunctionCall(String functionCall) {
		this.functionCall = functionCall;
	}
	
	
	
}

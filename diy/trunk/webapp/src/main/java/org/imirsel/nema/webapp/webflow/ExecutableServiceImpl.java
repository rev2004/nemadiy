package org.imirsel.nema.webapp.webflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.CommandLineFormatter;
import org.imirsel.nema.contentrepository.client.ResourceTypeService;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.Param;
import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.SysProperty;
import org.imirsel.nema.model.VanillaPredefinedCommandTemplate;
import org.imirsel.nema.webapp.model.DiyJavaTemplate;
import org.imirsel.nema.webapp.model.DiyMatlabTemplate;
import org.imirsel.nema.webapp.model.NiceParams;
import org.imirsel.nema.webapp.model.UploadedExecutableBundle;
import org.springframework.webflow.core.collection.ParameterMap;

public class ExecutableServiceImpl {
	static private Log logger = LogFactory.getLog(ExecutableServiceImpl.class);
	private CommandLineFormatter commandLineFormatter;
	private ResourceTypeService resourceServiceType;


	/**
	 * return the PredefinedCommandTemplate according to the type String
	 * 
	 * @param type
	 * @return
	 */
	public VanillaPredefinedCommandTemplate getEmptyTemplate(
			ExecutableBundle.ExecutableType type) {
		switch (type) {
		case SHELL:
		case C:
			return new VanillaPredefinedCommandTemplate();
		case JAVA:
			return new DiyJavaTemplate();
		case MATLAB:
			return new DiyMatlabTemplate();
		default:
			return null;
		}
	}

	public NiceParams getNiceParams(List<Param> params){
		return new NiceParams(params);
	}


	public void generateExecutableBundle(
			VanillaPredefinedCommandTemplate template,
			UploadedExecutableBundle executable) {

		executable.setId(UUID.randomUUID().toString());
		try {
			String command = commandLineFormatter.getCommandLineString(
					template, resourceServiceType.getOsDataType(executable
							.getPreferredOs()), false);
			executable.setCommandLineFlags(command);
			logger.debug("generated command flag:" + command);
		} catch (InvalidCommandLineFlagException e) {
			logger.error(e, e);
		}
		executable.setEnvironmentVariables(template.getEnvironmentMap());
		logger.debug("set the environment variables (size:"
				+ executable.getEnvironmentVariables().size() + ")");

	}

	public void setJavaTemplate(ParameterMap httpParam,
			UploadedExecutableBundle executable,
			JavaPredefinedCommandTemplate template) {

	   List<Path> jarPaths = executable.getJarPaths();
	   for(Path path:jarPaths) {
	      template.addClasspath(path);
	   }
		String[] keys = getArray(httpParam, "sysVar");
		String[] values = getArray(httpParam, "sysValue");
		List<SysProperty> properties = new ArrayList<SysProperty>();
		if ((keys != null) && (values != null)) {
			int length = keys.length;
			for (int i = 0; i < length; i++) {
				if ((keys[i] != null) && (keys[i].length() > 0))
					properties.add(new SysProperty(keys[i], values[i]));
			}
			template.setProperties(properties);
			logger.debug("generate map of size " + length);
		}

	}

	public UploadedExecutableBundle setExecutable(UploadedExecutableBundle a){
		if (a==null) return new UploadedExecutableBundle();
		else return a;
	}
	
	/**
	 * 
	 * @param keys
	 * @param values
	 * @param inputs
	 * @param outputs
	 * @param others
	 * @return
	 */
	public void setCommonTemplate(String[] keys, String[] values,
			String[] inputs, String[] outputs, String[] others,
			VanillaPredefinedCommandTemplate template) {
		if (template == null)
			throw new NullPointerException("template variable cannot be null");
		logger.debug("start to populate command template " + output(keys)
				+ " variable, " + output(inputs) + " inputs, "
				+ output(outputs) + " outputs, " + output(others) + " other");
		List<Param> params = new ArrayList<Param>();
		int i = 0;
		if (inputs != null)
			for (String entry : inputs) {
				if ((entry != null) && (entry.length() != 0))
					params.add(new Param(entry, true, Param.ParamType.INPUT
							.getCode(), i++));
			}
		if (outputs != null) {
			i = 0;

			for (String entry : outputs) {
				if ((entry != null) && (entry.length() != 0))
					params.add(new Param(entry, true, Param.ParamType.OUTPUT
							.getCode(), i++));
			}
		}
		if (others != null) {
			i = 0;
			for (String entry : others) {
				if ((entry != null) && (entry.length() != 0))
					params.add(new Param(entry, false, Param.ParamType.OTHER
							.getCode(), i++));
			}
		}
		template.setParams(params);
		Map<String, String> map = new HashMap<String, String>();

		if ((keys != null) && (values != null)) {
			int length = keys.length;
			for (i = 0; i < length; i++) {
				if ((keys[i] != null) && (keys[i].length() > 0))
					map.put(keys[i], values[i]);
			}
			logger.debug("generate map of environment variables size "
					+ template.getEnvironmentMap().size());
		}
		template.setEnvironmentMap(map);

	}
	
	private String output(String[] array) {
		return ((array == null) ? "null" : String.valueOf(array.length));
	}
	
	public void setCommonTemplate(ParameterMap httpParam,
			VanillaPredefinedCommandTemplate template) {
		String[] keys = getArray(httpParam, "variable");
		String[] values = getArray(httpParam, "value");
		String[] inputs = getArray(httpParam, "input");
		String[] others = getArray(httpParam, "other");
		String[] outputs = getArray(httpParam, "output");
		setCommonTemplate(keys, values, inputs, outputs, others, template);
	}

	private String[] getArray(ParameterMap httpParam, String key) {
		if (httpParam.contains(key)) {
			String[] values = httpParam.getArray(key);
			logger.debug("parameter " + key + " map size " + values.length);
			if (values.length == 0) {
				values = new String[1];
				values[0] = httpParam.get(key);
				logger.debug("find an 1 value parameter " + values[0]);
			}
			return values;
		} else {
			return null;
		}
	}
	
	public void setCommandLineFormatter(
			CommandLineFormatter commandLineFormatter) {
		this.commandLineFormatter = commandLineFormatter;
	}
	
	public CommandLineFormatter getCommandLineFormatter() {
		return commandLineFormatter;
	}
	public ResourceTypeService getResourceServiceType() {
		return resourceServiceType;
	}

	public void setResourceServiceType(ResourceTypeService resourceServiceType) {
		this.resourceServiceType = resourceServiceType;
	}

}

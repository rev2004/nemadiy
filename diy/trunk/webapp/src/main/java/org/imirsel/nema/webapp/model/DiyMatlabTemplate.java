/**
 * Model class for the Matlab type executable submission web flow
 * 
 */
package org.imirsel.nema.webapp.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.contentrepository.client.CommandLineFormatter;
import org.imirsel.nema.model.InvalidCommandLineFlagException;
import org.imirsel.nema.model.JavaPredefinedCommandTemplate;
import org.imirsel.nema.model.MatlabPredefinedCommandTemplate;
import org.imirsel.nema.model.Path;
import org.imirsel.nema.model.SysProperty;

/**
 * @author gzhu1
 *
 */
public class DiyMatlabTemplate extends MatlabPredefinedCommandTemplate {

//	static private Log logger = LogFactory.getLog(MatlabExecutableFile.class);

	private static final long serialVersionUID = 1L;

//	@Override
//	public void generateCommandline(){
//	
//		
//		MatlabPredefinedCommandTemplate command=new MatlabPredefinedCommandTemplate();
//		command.setDebug(debug);
//		command.setDisplay(display);
//		command.setJvm(jvm);
//		command.setLog(log);
//		command.setLogfile(logfile);
//		command.setSplash(splash);
//		command.setTiming(timing);
//		
//		CommandLineFormatter formatter=new CommandLineFormatter();
//		try {
//			setEnvironment(formatter.getCommandLineString(command, resourceTypeService.getOsDataType(os),true));
//		} catch (InvalidCommandLineFlagException e) {
//			logger.error(e,e);
//		}
//	}
}

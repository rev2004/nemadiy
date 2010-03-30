package org.imirsel.service.lookup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RMISecurityManager;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import com.sun.jini.start.ServiceStarter;
import com.sun.jini.tool.ClassServer;

/**This Class starts the class server and Reggie. Requires a reggie.policy 
 * security policy to be setup using -Djava.security.policy=reggie.policy. 
 * Requires a reggie configuration file.
 * 
 * 
 * @author kumaramit01
 * @since 0.1.0
 */
public class RunReggieLookup {
	Logger logger = Logger.getLogger(RunReggieLookup.class.getName());
	
	private static final ExecutorService executorService = Executors.newFixedThreadPool(2);
	
	public static void main(String[] args){
		System.setSecurityManager(new RMISecurityManager());
		try {
			startWebServerThread();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(2);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(2);
		}
		if(args.length!=1){
			System.out.println("Expects the reggie configuration file.");
			System.exit(2);
		}
		File file = new File(args[0]);
		if(!file.exists()){
			throw new IllegalArgumentException("file " + file.getAbsolutePath() + " does not exist.");
		}
		ServiceStarter.main(args);
	}

	@SuppressWarnings("unchecked")
	private static void startWebServerThread() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		InputStream is=RunReggieLookup.class.getResourceAsStream("ClassServer.Properties");
		if(is==null){
			System.out.println("ERROR.... input stream from the classpath is null.\nTrying to read from" +
					" the filesystem.");
			is = new FileInputStream("ClassServer.properties");
			if(is==null){
				System.out.println("Error input stream from the file system is null");
			}
		}
		properties.load(is);
		String port =(String)properties.get("port");
		String webDirectory=(String)properties.get("webDirectory");
		webDirectory = System.getProperty("user.dir") + System.getProperty("file.separator") + webDirectory;
		
		System.out.println("Web Directory: "+ webDirectory);
		if(!new File(webDirectory).exists()){
			System.out.println("web directory "+ webDirectory + " does not exist");
			System.exit(2);
		}
		String[] classServerArgs= new String[]{"-port",port,"-dir",webDirectory,"-verbose"};
		ClassServerThread classServerThread = new ClassServerThread(classServerArgs);
		Future<Void> value=executorService.submit(classServerThread);
		try {
			value.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

class ClassServerThread implements Callable<Void>{
	private String[] classServerArgs;
	
	public ClassServerThread(String[] classServerArgs){
		this.classServerArgs = classServerArgs;
	}

	public Void call() throws Exception {
		ClassServer.main(classServerArgs);
		return null;
	}
	
}
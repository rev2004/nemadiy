package org.imirsel.nema.contentrepository.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.jcr.Repository;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.ExecutableBundle.ExecutableType;

public class ContentRepositoryTestUtil {
	
	static ExecutableBundle getExecutableBundle() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("ENV1", "VALUE1");
		map.put("ENV2", "VALUE2");

		ExecutableBundle bundle = new ExecutableBundle();

		bundle.setExecutableName("Matlab");
		bundle.setCommandLineFlags("-o -pp ${FileOne.class}");
		bundle.setId("bundle-id");
		bundle.setTypeName(ExecutableType.MATLAB);
		bundle.setEnvironmentVariables(map);
		byte[] fileContent = null;

		try {
			fileContent = readFileContent("client/src/test/resources/test.zip");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bundle.setBundleContent(fileContent);
		bundle.setFileName("test.zip");

		return bundle;

	}
	
	public static byte[] getFlowContent() {
		byte[] fileContent = null;
		try {
			fileContent = readFileContent("client/src/test/resources/test.nt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileContent;
	}


	private static byte[] readFileContent(String filename) throws IOException {
		File file = new File(filename);
		InputStream is = new FileInputStream(file);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "+ file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
	

	/**
	 * Creates a Repository instance to be used by the test
	 * 
	 * @return repository instance
	 * @throws Exception
	 *             on errors
	 */
	static Repository getRepository(String configFile,
			String repositoryHome) throws Exception {
		Hashtable env = new Hashtable();
		env
				.put(Context.INITIAL_CONTEXT_FACTORY,
						"org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
		env.put(Context.PROVIDER_URL, "localhost");
		InitialContext ctx = new InitialContext(env);

		RegistryHelper.registerRepository(ctx, "repo", configFile,
				repositoryHome, true);
		return (Repository) ctx.lookup("repo");
	}
	
	
	public static Repository getProductionRepository() throws Exception{
		String configFile = "/Users/amitku/projects/contentrepository/data/repository.xml";
		String repHomeDir="/Users/amitku/projects/contentrepository/data";
		
		if(!new File(configFile).exists()){
			throw new IOException("File: " + configFile +"  does not exist");
		}
		if(!new File(repHomeDir).exists()){
			throw new IOException("File: " + repHomeDir +"  does not exist");
		}
		return getRepository(configFile,repHomeDir);
	}

	/**
	 * Returns repository object
	 * 
	 * @return a repository object that gets reset for each test
	 * @throws Exception
	 */
	public static Repository getTempRepository() throws Exception {
		String tmpFolder = System.getProperty("java.io.tmpdir");
		File file = new File(tmpFolder, "data");
		if (file.exists()) {
			boolean success = deleteDirectory(file);
			System.out.println("repository deleted: " + success);
		}
		file.mkdir();
		String configFile = copyFile("client/src/test/resources/repository.xml",
				file.getAbsolutePath());
		String repHomeDir = file.getAbsolutePath();
		
		return getRepository(configFile,repHomeDir);
	}

	
	/**Copies file
	 * 
	 * @param configFile
	 * @param dirPath
	 * @return file path
	 * @throws IOException
	 */
	public static String copyFile(String configFile, String dirPath)
			throws IOException {
		File src = new File(configFile);
		if (!src.exists()) {
			throw new IOException("Source does not exist "
					+ src.getAbsolutePath());
		}
		File dst = new File(dirPath, src.getName());
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst); // Transfer bytes from in
		// to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		return dst.getAbsolutePath();
	}

	/**Deletes a directory
	 * 
	 * @param path
	 * @return boolean true or false
	 */
	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}


}

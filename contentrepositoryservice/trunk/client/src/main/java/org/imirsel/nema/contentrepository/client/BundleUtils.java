package org.imirsel.nema.contentrepository.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.ValueFormatException;

import org.imirsel.nema.model.ExecutableBundle;
import org.imirsel.nema.model.InvalidBundleException;
import org.imirsel.nema.model.NemaResult;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * Util class for bundle related functions
 * 
 * @author kumaramit01
 * @since 0.1.0
 */
public class BundleUtils {

	protected static byte[] getPropertyFileAsBytes(ExecutableBundle bundle) throws InvalidBundleException {
		Properties properties = new Properties();
		
		if(bundle.getTypeName()==null){
			throw new InvalidBundleException("Missing bundle type");
		}
		
		if(bundle.getId()==null){
			throw new InvalidBundleException("Missing id");
		}
		
		
		if(bundle.getExecutableName()!=null){
			properties.setProperty("executableName",  bundle.getExecutableName());
		}
		
		
		properties.setProperty("typeName", bundle.getTypeName());
		
		properties.setProperty("execId", bundle.getId());
		
		
		if(bundle.getCommandLineFlags()!=null)
		properties.setProperty("commandLineFlags", bundle.getCommandLineFlags());
		if(bundle.getEnvironmentVariables()!=null){
			properties.setProperty("environmentVariables", convertToJSONString(bundle.getEnvironmentVariables()));
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, "serialized properties for name" );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return baos.toByteArray();
	}
	
	protected static byte[] getPropertyFileAsBytes(NemaResult nemaResult) {
		Properties properties = new Properties();
		System.out.println("14-1");
		if(nemaResult.getResultType()!=null)
		properties.setProperty("typeName",nemaResult.getResultType().toString());
		System.out.println("14-2");
		if(nemaResult.getName()!=null)
		properties.setProperty("name",nemaResult.getName());
		System.out.println("14-3");
		if(nemaResult.getFileName()!=null)
		properties.setProperty("fileName",nemaResult.getFileName());
		
		System.out.println("14-4");
		if(nemaResult.getExecutionId()!=null)
		properties.setProperty("execId", nemaResult.getExecutionId());
		
		
		System.out.println("14-5");
		if(nemaResult.getModelClass()!=null){
			properties.setProperty("modelClass", nemaResult.getModelClass());
		}
		System.out.println("14-6");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			properties.storeToXML(baos, "serialized properties for name" );
			System.out.println("14-7");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("14-8");
		return baos.toByteArray();
	}

	
	
	protected static String convertToJSONString(Map<String, String> environmentVariables) {
		 XStream xstream = new XStream(new JettisonMappedXmlDriver());
		 xstream.setMode(XStream.NO_REFERENCES);
		 return xstream.toXML(environmentVariables);
	}


	protected static String[] getKeyValuePairs(final Map<String, String> environmentVariables) {
		String[] values = new String[environmentVariables.size()];
		int count=0;
		for(String key:environmentVariables.keySet()){
			values[count] = key+"="+environmentVariables.get(key);
		}
		return values;
	}
	protected static Map<String,String> getMapfromKeyValuePairs(final Value[] values) 
	throws ValueFormatException, IllegalStateException, RepositoryException {
		Map<String,String> hmap = new HashMap<String,String>();
		for(Value value:values){
			String keyValue = value.getString();
			String[] kv=keyValue.split("=");
			if(kv.length==2){
				hmap.put(kv[0], kv[1]);
			}
		}
		return hmap;
	}

	protected static byte[] readByteDataFromStream(final InputStream is, final long length) throws IOException {
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
			throw new IOException("Could not completely read the node data");
		}
		// Close the input stream and return bytes
		is.close();
		return bytes;
	}
	

	protected static void validateBundle(ExecutableBundle bundle) throws ContentRepositoryServiceException {
		if(bundle.getBundleContent().length==0){
			throw new ContentRepositoryServiceException("The content of the bundle is 0 bytes");
		}else if(bundle.getFileName()==null){
			throw new ContentRepositoryServiceException("The bundle filename is null");
		}else if(bundle.getId()==null){
			throw new ContentRepositoryServiceException("The bundle id is null");
		}else if(bundle.getTypeName()==null){
			throw new ContentRepositoryServiceException("The bundle type name is null");
		}
		
	}
	
	protected static void validateResult(NemaResult nemaResult) throws ContentRepositoryServiceException{
		if(nemaResult.getExecutionId()==null){
			throw new ContentRepositoryServiceException("The result file's execution id is null");
		}
		if(nemaResult.getName() == null){
			throw new ContentRepositoryServiceException("The result file name is null");
		}
		if(nemaResult.getFileContent() == null){
			throw new ContentRepositoryServiceException("The content of the result file is null");	
		}
		if(nemaResult.getFileContent().length==0){
			throw new ContentRepositoryServiceException("The content of the result file is 0 bytes");
		}
		
		
	}





}

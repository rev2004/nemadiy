package org.imirsel.nema.contentrepository.client;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Compression specific functions
 * 
 * @author kumaramit01
 * 
 */
public class CompressionUtils {

	private static Logger logger = Logger.getLogger(CompressionUtils.class.getName());
	private final String tempLocation;
	
	static{
		logger.setLevel(Level.ALL);
	}

	/** Constructor that takes temporary file location for creating
	 * zip files
	 * @param tempLocation
	 */
	private CompressionUtils(final String tempLocation) {
		this.tempLocation = tempLocation;
	}

	/** default constructor
	 * 
	 */
	private CompressionUtils() {
		this.tempLocation = System.getProperty("java.io.tmpdir");
	}
	
	private static  CompressionUtils instance = new CompressionUtils();

	/**
	 * @return the static CompressionUtils class
	 */
	public static CompressionUtils getInstanceOf(){
		return instance;
	}
	
	
	
	/**
	 * @param fileContents
	 * @param decompressLocation
	 * @param fileName
	 * @return List of file names
	 * @throws ZipException
	 * @throws IOException
	 */
	public List<String> decompress(byte[] fileContents, final String decompressLocation, final String fileName) throws ZipException, IOException{
		String tmpLoc = System.getProperty("java.io.tmpdir");
		List<String> fileList = new ArrayList<String>();
		File tempFile = new File(tmpLoc,fileName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(tempFile);
			fos.write(fileContents);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(fos!=null){
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		}
		ZipFile zipFile=null;
		try{
		zipFile = new ZipFile(tempFile);
		Enumeration<? extends ZipEntry> entries= zipFile.entries();
		  while(entries.hasMoreElements()) {
		        ZipEntry entry = (ZipEntry)entries.nextElement();
		        String processedName = decompressLocation + entry.getName();
		        if(entry.isDirectory()) {
		          // Assume directories are stored parents first then children.
		          logger.fine("Extracting directory: " + processedName);
		          // This is not robust, just for demonstration purposes.
		          File directoryFile = new File(processedName);
		          boolean success=directoryFile.mkdirs();
		          if(!success){
		        	  logger.severe("Error -creating new directory " + directoryFile.getCanonicalPath());
		          }
		          continue;
		        }

		       logger.info("Extracting file: " +processedName);
		       copyInputStream(zipFile.getInputStream(entry),processedName);
		         
		         fileList.add(processedName);
		      }
		}finally{
			if(zipFile!=null){
		      zipFile.close();
			}
			
			boolean successDelete=tempFile.delete();
			if(!successDelete){
				logger.severe("Error -could not delete the temp file: " + tempFile);
			}
			
		}
	
		return fileList;
	}
	
	 private static final void copyInputStream(InputStream in, String fileName) throws FileNotFoundException{
	    File file = new File(fileName);
	    File parentFile = file.getParentFile();
	    parentFile.mkdirs();
	    System.out.println("Creating parent directory... " + parentFile.getAbsolutePath());
		byte[] buffer = new byte[1024];
	    FileOutputStream fos=new FileOutputStream(file);
	    BufferedOutputStream out= new BufferedOutputStream(fos);
	    int len;
	    try{
	    while((len = in.read(buffer)) >= 0){
	      out.write(buffer, 0, len);
	    }
	    }catch(IOException ex){
	    	
	    }finally{
	       try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  	   try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  	    	
	    }
	  }


	/**Returns the byte array of the compressed contents
	 * 
	 * @param fileLocation -location of the file
	 * @param relativeTo -The paths in the zip file are relative to this
	 * @return byte array of zipped fileLocation
	 * @throws IOException
	 */
	public byte[] compress(final String fileLocation, final String relativeTo) throws IOException {
		List<String> fileList = new ArrayList<String>();
		File sourceFile = new File(fileLocation);
		getFileList(fileLocation, fileList);
		logger.info("Files: " + fileList.size());
		byte[] buffer = new byte[1024];
		String zipfileName = sourceFile.getName() + ".zip";
		File zipFilePath = new File(tempLocation, zipfileName);
		ZipOutputStream zos=null;
		try {
			FileOutputStream fos = new FileOutputStream(zipFilePath);
			zos = new ZipOutputStream(fos);
			logger.info("Output to Zip : " + zipFilePath);
			for (String file : fileList) {
				File currentFile = new File(file);
				String entryName = currentFile.getAbsolutePath();
				if(relativeTo!=null){
					int indexRelativePath = entryName.indexOf(relativeTo);
					if(indexRelativePath!=-1){
						entryName= entryName.substring(indexRelativePath+relativeTo.length());
					}
				}
				logger.info("entry name is: " + entryName);
				if (currentFile.isDirectory()) {
					logger.fine("File Added : " + file);
					ZipEntry ze = new ZipEntry(entryName + "/");
					zos.putNextEntry(ze);
				} else if (currentFile.isFile()) {
					logger.fine("File Added : " + file);
					ZipEntry ze = new ZipEntry(entryName);
					zos.putNextEntry(ze);
					FileInputStream in = new FileInputStream(file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
					in.close();
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}finally{
			if(zos!=null){
			zos.closeEntry();
			zos.close();
			}
			
		}
		return readFile(zipFilePath);
	}

	private byte[] readFile(File file) throws IOException {
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
		boolean success=file.delete();
		if(!success){
			System.out.println("Error could not delete file: " + file.getAbsolutePath());
		}
		return bytes;
	}

	/** For testing...
	 * @param args
	 * @throws IOException
	 */
	public static void main(String args[]) throws IOException {
		CompressionUtils cu = new CompressionUtils();
		byte[] fcontent=cu.compress("/tmp/testme","/tmp/");

		FileOutputStream fos = new FileOutputStream(new File("one.zip"));
		fos.write(fcontent);
		fos.flush();
		fos.close();
		
		
		//cu.decompress(fcontent,"/Users/amitku/tmp","tmp");
		
		
	}

	private void getFileList(String fileLocation, List<String> fileList)
			throws IOException {
		File file = new File(fileLocation);
		if ((file.isFile())) {
			fileList.add(file.getCanonicalPath());
			return;
		}
		// add the location of the directory
		fileList.add(fileLocation);
		File dir = new File(fileLocation);
		File[] dirFiles = dir.listFiles();
		if (dirFiles != null) {
			for (File file1 : dirFiles) {
				String cpath = file1.getCanonicalPath();
				if (!fileList.contains(cpath)) {
					getFileList(cpath, fileList);
				}
			}
		}
	}

}

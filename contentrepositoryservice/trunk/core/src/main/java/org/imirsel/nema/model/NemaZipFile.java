package org.imirsel.nema.model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Encapsulates a zip or jar file uploaded by the Nema User.
 * 
 * @author shirk
 * @since 0.0.1
 */
public class NemaZipFile {

	private static final String FILE_CLOSED_ERR_MSG = "NemaZipFile has "
			+ "already been closed. A closed file cannot be reopened.";
	private static final String FILE_NOT_OPEN_ERR_MSG = "NemaZipFile must be "
			+ "opened before it can be used. Call open() after instantiation.";

	private enum FileState {
		NEW, OPENED, CLOSED
	}

	private String fileSeparator = System.getProperty("file.separator");
	private String systemUnzipDir = System.getProperty("java.io.tmpdir");

	private ZipFile sourceZip;
	private String sourceZipName;
	private File sourceZipContentDir;

	private FileState state = FileState.NEW;

	/**
	 * Create a new {@code NemaZipFile} based on the specified Zip file. The
	 * {@code open()} method must be called immediately after this instance is
	 * constructed.
	 * 
	 * @param zipFile
	 * @param location where the zip file would be unzipped 
	 *            Zip file to base this file upon.
	 */
	public NemaZipFile(ZipFile zipFile, String systemUnzipDir) {
		sourceZip = zipFile;
		int lastIdxOfFileSep = zipFile.getName().lastIndexOf(fileSeparator);
		sourceZipName = zipFile.getName().substring(lastIdxOfFileSep + 1);
		this.systemUnzipDir = systemUnzipDir;
		System.out.println(systemUnzipDir);
	}

	/**
	 * Create a new {@code NemaZipFile} based on the specified Zip file. The
	 * {@code open()} method must be called immediately after this instance is
	 * constructed.
	 * 
	 * @param zipFile
	 *            Zip file to base this file upon.
	 */
	public NemaZipFile(ZipFile zipFile) {
		sourceZip = zipFile;
		int lastIdxOfFileSep = zipFile.getName().lastIndexOf(fileSeparator);
		sourceZipName = zipFile.getName().substring(lastIdxOfFileSep + 1);
		System.out.println(systemUnzipDir);
	}

	/**
	 * Open this {@code NemaZipFile}. Must be called before other methods can be
	 * used.
	 * 
	 * @throws IOException
	 *             if a problem occurs while opening the file.
	 */
	public void open() throws IOException {
		if (state == FileState.CLOSED) {
			throw new IllegalStateException(FILE_CLOSED_ERR_MSG);
		}
		makeTempDirForZipContents();
		expandZipIntoTmpDir();
		state = FileState.OPENED;
	}

	/**
	 * Close the Zip file and release used resources. After calling this method,
	 * this instance is no longer usable.
	 * 
	 * @throws IOException
	 *             if a problem occurs while opening the file.
	 */
	public void close() throws IOException {
		deleteDir(sourceZipContentDir);
		state = FileState.CLOSED;
	}

	/**
	 * Test for the presence of the specified file within this Zip. The
	 * specified file must include the full path.
	 * 
	 * @param fileName
	 *            The full path and name of the file to search for.
	 * @return True if the file exists.
	 */
	public boolean containsFile(String fileName) {
		validateOpened();
		return null != sourceZip.getEntry(fileName);
	}

	/**
	 * Test for the presence of the specified class file within the Jar files
	 * that are present in this Zip. The class name must include all package
	 * names (be fully qualified). For example, org.moo.foo.FooBar.
	 * 
	 * @param className
	 *            Fully qualified class name.
	 * @return True if the class exists.
	 */
	public boolean containsClass(String className) {
		validateOpened();
		boolean containsClass = false;
		String classFilePath = className.replace('.', '/') + ".class";
		try {
			List<JarFile> jarFiles = jarFiles();
			for (JarFile jarFile : jarFiles) {
				if (jarFile.getEntry(classFilePath) != null) {
					containsClass = true;
					break;
				}
			}
			closeJars(jarFiles);
		} catch (IOException e) {
			throw new RuntimeException("An error occured while searching for "
					+ "class " + className + " in file " + getSourceZipName()
					+ ".", e);
		}
		return containsClass;
	}

	/**
	 * Test for the presence of Jar files within this Zip file.
	 * 
	 * @return True if one or more Jar files are present in the Zip file.
	 */
	public boolean containsJars() {
		validateOpened();
		return jarEntries().size() > 0;
	}

	/**
	 * Return the name of source Zip file without path information.
	 * 
	 * @return Name of the source Zip file.
	 */
	public String getSourceZipName() {
		validateOpened();
		return sourceZipName;
	}

	/**
	 * Delete the specified directory and all of its contents.
	 * 
	 * @param dir
	 *            The directory to delete.
	 */
	private void deleteDir(File dir) {
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				deleteDir(files[i]);
			} else {
				files[i].delete();
			}
		}
		dir.delete();
	}

	/**
	 * Close the list of Jar files.
	 * 
	 * @param jarFiles
	 *            Files to close.
	 * @throws IOException
	 *             if an error occurs while closing the files.
	 */
	private void closeJars(List<JarFile> jarFiles) throws IOException {
		for (JarFile file : jarFiles) {
			file.close();
		}
	}

	/**
	 * Return entries in this Zip file that are Jar files.
	 * 
	 * @return List of ZipEntries that are Jars.
	 */
	private List<ZipEntry> jarEntries() {
		List<ZipEntry> jarEntries = new ArrayList<ZipEntry>();
		Enumeration<? extends ZipEntry> fileEnumeration = sourceZip.entries();
		while (fileEnumeration.hasMoreElements()) {
			ZipEntry entry = fileEnumeration.nextElement();
			if (entry.getName().endsWith(".jar")
					|| entry.getName().endsWith(".JAR")) {
				jarEntries.add(entry);
			}
		}
		return jarEntries;
	}

	/**
	 * Return the Jar files contained in this archive.
	 * 
	 * @return List Jar files in this Zip file.
	 * @throws IOException
	 *             if an error occurs while loading the Jar files.
	 */
	private List<JarFile> jarFiles() throws IOException {
		List<JarFile> jarFiles = new ArrayList<JarFile>(8);
		for (ZipEntry jarEntry : jarEntries()) {
			String filePath = sourceZipContentDir.getPath() + fileSeparator
					+ jarEntry.getName();
			jarFiles.add(new JarFile(filePath));
		}
		return jarFiles;
	}

	/**
	 * Expand the source Zip file contents into the temp directory.
	 * 
	 * @throws IOException
	 *             if a problem occurs while expanding the file.
	 */
	private void expandZipIntoTmpDir() throws IOException {
		int buffSize = 1024;
		BufferedOutputStream outStream = null;
		BufferedInputStream inStream = null;
		Enumeration<? extends ZipEntry> e = sourceZip.entries();
		while (e.hasMoreElements()) {
			ZipEntry entry = e.nextElement();
			inStream = new BufferedInputStream(sourceZip.getInputStream(entry));
			int count;
			byte data[] = new byte[buffSize];
			String fileName = sourceZipContentDir + fileSeparator
					+ entry.getName();
			File outFile = new File(fileName);
			if (entry.isDirectory()) {
				outFile.mkdirs();
			} else {
				FileOutputStream fos = new FileOutputStream(outFile);
				outStream = new BufferedOutputStream(fos, buffSize);
				while ((count = inStream.read(data, 0, buffSize)) != -1) {
					outStream.write(data, 0, count);
				}
				outStream.flush();
				outStream.close();
				inStream.close();
			}
		}
	}

	/**
	 * Create a temporary directory in which the source Zip file will be
	 * expanded.
	 */
	private void makeTempDirForZipContents() {
		sourceZipContentDir = new File(systemUnzipDir + fileSeparator
				+ UUID.randomUUID().toString());
		sourceZipContentDir.mkdir();
	}

	/**
	 * Helper method to test if this file is opened. Throws an
	 * IllegalStateException if the file is not opened.
	 */
	private void validateOpened() {
		if (state != FileState.OPENED) {
			throw new IllegalStateException(FILE_NOT_OPEN_ERR_MSG);
		}
	}

	/**
	 * Method that returns the location of the zip file. The file
	 * 	maynot have been unzipped.
	 * @return the location of the zip content directory.
	 */
	public File getSourceZipContentDir() {
		return sourceZipContentDir;
	}

}

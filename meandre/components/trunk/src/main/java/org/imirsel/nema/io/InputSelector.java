package org.imirsel.nema.io;

//import jAudioFeatureExtractor.ACE.DataTypes.SegmentedClassification;
import jAudioFeatureExtractor.ACE.DataTypes.SegmentedClassification;

import java.util.StringTokenizer;
import java.util.logging.Logger; //import java.net.*;
import java.io.*;

import org.meandre.core.ComponentContext;
import org.meandre.core.ComponentContextException;
import org.meandre.core.ComponentContextProperties;
import org.meandre.core.ComponentExecutionException;
import org.meandre.core.ExecutableComponent;
import org.meandre.annotations.Component;
import org.meandre.annotations.ComponentOutput;
import org.meandre.annotations.ComponentProperty;
import org.imirsel.nema.renderers.FileRenderer;
import org.imirsel.nema.util.FileDownload;
import org.imirsel.service.ArtifactManagerImpl;
import org.imirsel.nema.annotations.*;

@Component(creator = "Mert Bay", description = "Reads an XML or CSV file from a local directory or an URL, with File location and  class " +
		" metadata. Output is a 2D String array that holds the fileLocation  in the first column and its " +
		"class metadata in the second column. If the fileLocation is a URL, it will be downloaded  to a local path. " +
		"The inputListfiles should be  properly formed. If individual file field is not empty, the file list" +
		" will be ignored.", name = "InputSelector", tags = "input, file, URL,file download, CSV reader, XML reader",
		 firingPolicy = Component.FiringPolicy.all)
public class InputSelector implements ExecutableComponent {
	
	@ComponentOutput(description = "String[][] that holds the fileLocation for the audio in the first column and its class metadata in the second column", name = "inputFiles")
	public final static String DATA_OUTPUT = "inputFiles";
	
	@StringDataType()
	@ComponentProperty(defaultValue = "http://nema.lis.uiuc.edu/example_wavs/mirex05FileList.xml", description = "A file with a list of audio files with  \"fileLocation\" and  class metadata, indicating the "
			+ "location of the corresponding audio file and also its class", name = "listFilePath")
	final static String DATA_PROPERTY_1 = "listFilePath";

	@StringDataType()
	@ComponentProperty(defaultValue = "", description = "A URL or local path of an individual file instead of the file list. If a URL is entered, the file will be downloaded. If this property is filled, the input file list will be ignored. ", name = "individualFile")
	final static String DATA_PROPERTY_2 = "individualFile";

	@StringDataType(renderer=FileRenderer.class)
	@ComponentProperty(defaultValue = "", description = "Upload a file. If this is chosen, the input file list will or the individual file property will be ignored. ", name = "uploadFile")
	final static String DATA_PROPERTY_3 = "uploadFile";	
	
	private String listFilePath = "http://nema.lis.uiuc.edu/example_wavs/mirex05FileList.xml";
	private String individualFile = "";
	private String uploadFile = "";	
	private String localListFilePath;
	private Logger logger = null;
	private java.io.PrintStream out;
	private String processWorkingDir;
	private String commonStorageDir;
	public void initialize(ComponentContextProperties ccp)
			throws ComponentExecutionException {
		out = ccp.getOutputConsole();
		logger = ccp.getLogger();
		try {
			processWorkingDir = ArtifactManagerImpl.getInstance()
					.getProcessWorkingDirectory(
							ccp.getFlowExecutionInstanceID());
			commonStorageDir=ArtifactManagerImpl.getInstance().getCommonStorageLocation() + File.separator + "inputSelectorDownloadedFiles";
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			throw new ComponentExecutionException(e1);
		}
	}

	public void dispose(ComponentContextProperties ccp) {
		// TODO Auto-generated method stub
		out.close();
	}

	public void execute(ComponentContext ccp)
			throws ComponentExecutionException, ComponentContextException {
		listFilePath = String.valueOf(ccp.getProperty(DATA_PROPERTY_1));
		individualFile = String.valueOf(ccp.getProperty(DATA_PROPERTY_2));
		uploadFile = String.valueOf(ccp.getProperty(DATA_PROPERTY_3));

		if (!uploadFile.contentEquals("")) {
			String[][] inputFiles = new String[1][2];
			out.println("File upload is selected");
			if (uploadFile.contains("http")
					|| uploadFile.contains("ftp")) {
				 //String workingDirName = processWorkingDir;
				String workingDirName = commonStorageDir;
				
				inputFiles[0][0] = downloadFiles(uploadFile, workingDirName);
			} else {
				inputFiles[0][0] = uploadFile;
			}
			inputFiles[0][1] = "";
			out.println("no 1:\tFileName="
					+ uploadFile.subSequence(uploadFile
							.lastIndexOf("/") + 1, uploadFile.length())
					+ "\t\tClassName= " + "\t\t added to output");
			ccp.pushDataComponentToOutput(DATA_OUTPUT, inputFiles);
		} 
		else if (!individualFile.contentEquals("")) {
			String[][] inputFiles = new String[1][2];
			out.println("Individual file is selected");
			if (individualFile.contains("http")
					|| individualFile.contains("ftp")) {
				 //String workingDirName = processWorkingDir;
				String workingDirName = commonStorageDir;
				inputFiles[0][0] = downloadFiles(individualFile, workingDirName);
			} else {
				inputFiles[0][0] = individualFile;
			}
			inputFiles[0][1] = "";
			out.println("no 1:\tFileName="
					+ individualFile.subSequence(individualFile
							.lastIndexOf("/") + 1, individualFile.length())
					+ "\t\tClassName= " + "\t\t added to output");
			ccp.pushDataComponentToOutput(DATA_OUTPUT, inputFiles);			
		}
		else {
			BufferedReader textBuffer;
			int noLines = 0;
			try {
				// Check if it is a URL
				if (listFilePath.contains("http")
						|| listFilePath.contains("ftp")) {
					// localListFilePath = ccp.getPublicResourcesDirectory()+
					// "/inputSelectorDownloadedFiles/"+listFilePath.substring(listFilePath.lastIndexOf('/')+1,
					// listFilePath.length());
					
					
					String workingDirName = commonStorageDir;
					localListFilePath = downloadFiles(listFilePath,
							workingDirName);
					textBuffer = new BufferedReader(new FileReader(
							localListFilePath));
					noLines = org.imirsel.nema.util.LineCounter
							.count(textBuffer) - 1;
					textBuffer.close();
					textBuffer = new BufferedReader(new FileReader(
							localListFilePath));
				} else {
					textBuffer = new BufferedReader(
							new FileReader(listFilePath));
					noLines = org.imirsel.nema.util.LineCounter
							.count(textBuffer) - 1;
					textBuffer.close();
					textBuffer = new BufferedReader(
							new FileReader(listFilePath));
				}
				// If it is a CSV file
				if (listFilePath.substring(listFilePath.length() - 3,
						listFilePath.length()).equalsIgnoreCase("csv")) {
					out.println("The collection format is "
							+ listFilePath.substring(listFilePath.length() - 3,
									listFilePath.length()));
					out.println("There are " + noLines
							+ " records in the collection");
					// Read the header info from the file
					String Header = textBuffer.readLine();
					String[][] inputFiles = new String[noLines][2];
					for (int i = 0; i < noLines; i++) {
						String line = textBuffer.readLine();
						StringTokenizer str = new StringTokenizer(line, ",");
						String fileLocation;
						String classname;
						if (str.countTokens() == 1) {
							fileLocation = str.nextToken();
							classname = "";
							out.println("There is no class label");
						} else if (str.countTokens() == 2) {
							fileLocation = str.nextToken();
							classname = str.nextToken();
						} else {
							out.println("Mistake in Record no " + i
									+ ". Continuing...");
							continue;
						}
						if (fileLocation.contains("http")
								|| fileLocation.contains("ftp")) {
							String workingDirName = commonStorageDir;
							inputFiles[i][0] = downloadFiles(fileLocation,
									workingDirName);
						} else {
							inputFiles[i][0] = fileLocation;
						}
						out.println("no "
								+ (i + 1)
								+ ":\tFileName="
								+ fileLocation.subSequence(fileLocation
										.lastIndexOf("/") + 1, fileLocation
										.length()) + "\t\tClassName="
								+ classname + "\t\t added to output");
						inputFiles[i][1] = classname;
					}
					ccp.pushDataComponentToOutput(DATA_OUTPUT, inputFiles);
				}
				// If it is a XML file
				else if (listFilePath.substring(listFilePath.length() - 3,
						listFilePath.length()).equalsIgnoreCase("xml")) {
					// To be implemented
					SegmentedClassification[] inputXML = SegmentedClassification
							.parseClassificationsFile(localListFilePath);
					out.println("The collection format is xml");
					out.println("There are " + inputXML.length
							+ " records in the collection.");
					String[][] inputFiles = new String[inputXML.length][2];
					for (int i = 0; i < inputXML.length; i++) {
						String[] classes = inputXML[i].classifications;
						String fileLocation = inputXML[i].identifier;
						String classname;
						if (classes != null) {
							classname = classes[0];
						} else {
							classname = "";
							out.println("There is no class label");
						}
						if (fileLocation.contains("http")
								|| fileLocation.contains("ftp")) {
							String workingDirName = commonStorageDir;
							inputFiles[i][0] = downloadFiles(fileLocation,
									workingDirName);
						} else {
							inputFiles[i][0] = fileLocation;
						}
					//	inputFiles[i][0] = dir_prefix+ inputFiles[i][0].subSequence(inputFiles[i][0].lastIndexOf("/") + 1, inputFiles[i][0]						.length());
						out.println("no "
								+ (i + 1)
								+ ":\tFileName="
								+ inputFiles[i][0].subSequence(inputFiles[i][0]
										.lastIndexOf("/") + 1, inputFiles[i][0]
										.length()) + "\t\tClassName="
								+ classname + "\t\t  added to output.");
						out.println("Debuggin no "
								+ (i + 1)
								+ ":\tFileName="
								+ inputFiles[i][0] + "\t\tClassName="
								+ classname + "\t\t  added to output.");
						
					}
					ccp.pushDataComponentToOutput(DATA_OUTPUT, inputFiles);
				} else {
					out.println("Unsupported input file format "
							+ listFilePath.substring(listFilePath.length() - 3,
									listFilePath.length()));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String downloadFiles(String fileLocation, String workingDirName) {

		out.println("\nDownloading file from " + fileLocation);

		File workingDir = new File(workingDirName);
		if (!workingDir.exists()) {
			boolean done = workingDir.mkdirs();
			if (!done) {
				throw new RuntimeException(
						"Could not create the output directory");
			}
		} else if (!workingDir.isDirectory()) {
			throw new RuntimeException(workingDir + " No such Parent directory");
		}
		String localFileName = workingDirName
				+ File.separator
				+ fileLocation.substring(listFilePath.indexOf("//")+2,fileLocation.length()).replaceAll("/", "_");
		
		File localFile = new File(localFileName);
		if (localFile.exists()) {
			out.println("The file " + fileLocation
					+ " alredy exists locally, avoiding re-download.");
		} else {
			FileDownload.download(fileLocation, localFileName);
			out.println("File downloaded to local path." );
		}
		return localFileName;
	}

}

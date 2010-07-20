package org.imirsel.nema.meandre.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;

public class ConsoleUtil {

	static protected Log logger = LogFactory.getLog(ConsoleUtil.class);

	private MeandreConsoleDao dao;
	private String folder;

	public void setDao(MeandreConsoleDao dao) {
		this.dao = dao;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public String getFolder() {
		return folder;
	}

	public void dumpConsoleToFile(Job job) {

		if (job.isDone()&&hasEffectiveId(job)) {

			try {

				String filename = generateFileName(job);

				File file = new File(filename);
				if (!file.exists()) {
					List<ConsoleEntry> entries = dao.getEntry(job);
					FileWriter fstream = new FileWriter(file);
					BufferedWriter out = new BufferedWriter(fstream);
					out.append("Job ").append(job.getName());
					out.newLine();
					out.append("ID:").append(job.getId().toString());
					out.newLine();
					out.append("Description:").append(job.getDescription());
					out.newLine();
					out.append("URI:").append(job.getExecutionInstanceId());
					out.newLine();
					out.append("Token:").append(job.getToken());
					out.newLine();
					out.append("Transferred from database at:").append(
							(new Date()).toString());
					out.newLine();
					out.append("*******************************");
					out.newLine();

					for (ConsoleEntry entry : entries) {
						out.append("[").append(entry.getTimeStamp().toString())
								.append("]"); 
						out.newLine();
						out.append(entry.getLine());
						out.newLine();
					}

					out.close();

					dao.clearConsole(job);
					logger.info("Dump the console of Job ("
							+ job.getFlow().getUri() + ") into file "
							+ filename);
				} else {
					logger.error("job " + job.getFlow().getUri()
							+ " has already been dumped to " + filename);
					throw new IllegalArgumentException("job "
							+ job.getFlow().getUri()
							+ " has already been dumped to " + filename);

				}
			} catch (IOException e) {
				logger.error(e, e);
			}
		}
	}

	public String findConsole(Job job) {
		if (job.isDone()) {
			String filename = generateFileName(job);

			StringBuilder text = new StringBuilder();
			String NL = System.getProperty("line.separator");
			Scanner scanner;
			try {
				scanner = new Scanner(new File(filename));

				try {
					while (scanner.hasNextLine()) {
						text.append(scanner.nextLine() + NL);
					}
				} catch (NoSuchElementException e) {
					text.append("ERROR WHILE READING CONSOLE FILES" + NL);
					text.append(e.toString());
					logger.error(e, e);
				} finally {
					scanner.close();
				}
			} catch (FileNotFoundException e) {
				text.append("file " + filename + " does not exist");
				logger.error(e, e);
			}
			return text.toString();
		} else {
			return null;
		}
	}

	private String generateFileName(Job job) {
		
//		String uniqueStr = extractJobStr(job.getExecutionInstanceId());
		String uniqueStr=job.getToken();
		return folder + File.separator + uniqueStr + ".log";

	}

	private String extractJobStr(String fullUri) {
		String[] subStrs = fullUri.split("/");
		if ((subStrs != null) && (subStrs.length > 0)) {
			return subStrs[subStrs.length-1];
		} else {
			return null;

		}
	}
	private Boolean hasEffectiveId(Job job){
		return (job.getExecutionInstanceId()!=null)&&(job.getExecutionInstanceId()!="");
	}

}

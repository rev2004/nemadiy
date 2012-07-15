package org.imirsel.nema.melodyevaluator;

import java.io.File;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;

public class RetrieveSubmissionProperties {
	
	public RetrieveSubmissionProperties() {

	}
	
	public static void main(String[] args) throws Exception{
		File outputDirectory;
		String subCode;
		NemaSubmission sub;
		
		subCode = args[0];
		outputDirectory = new File(args[1]);
		
		RepositoryClientInterface client = null;
		
		try{
			client = new RepositoryClientImpl();
			System.out.println("Retrieving submission details for '" + subCode + "'");
			sub = client.getSubmissionDetails(subCode);
			if (sub == null){
				System.out.println("\n\nERROR:  Failed to retrieve submission details for '" + subCode + "'"); 
			}
		}finally{
			if (client != null){
				client.close();
			}
		}
		
		String outputFileName = subCode.trim() + ".properties";
		File outputFile = new File(outputDirectory, outputFileName);
		sub.toPropertiesFile(outputFile);
		
	}

}

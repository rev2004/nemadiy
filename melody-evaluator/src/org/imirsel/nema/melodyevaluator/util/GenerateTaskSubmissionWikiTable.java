package org.imirsel.nema.melodyevaluator.util;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import org.imirsel.nema.model.NemaContributor;
import org.imirsel.nema.model.NemaSubmission;
import org.imirsel.nema.repository.RepositoryClientImpl;

public class GenerateTaskSubmissionWikiTable {

	/**
	 * Util to dump wiki table legends for manual submissions.
	 * @param args
	 */
	public static void main(String[] args) throws SQLException{
		RepositoryClientImpl client = null;
		try{
			
			client = new RepositoryClientImpl();
			
			while(true){
				System.out.print("\n\nEnter task code (or exit to quit): ");
				Scanner in = new Scanner(System.in);
				String taskcode = in.nextLine();
				if (taskcode.equalsIgnoreCase("exit")){
					break;
				}
				
				System.out.println("\nGenerating wiki table for task code: " + taskcode);
				
				
				String table = "== General Legend ==\n" +
						"{| border=\"1\" cellspacing=\"0\" style=\"text-align: left; width: 800px;\"\n" +
						"\t|- style=\"background: yellow;\"\n" +
						"\t! width=\"80\" | Sub code \n" +
						"\t! width=\"200\" | Submission name \n" +
						"\t! width=\"80\" style=\"text-align: center;\" | Abstract \n" +
						"\t! width=\"440\" | Contributors\n" +
						"\t|-\n";
				
				
				List<String> subs = client.getSubmissionsForTaskCode(taskcode);
				for(Iterator<String> it = subs.iterator();it.hasNext();){
					String subcode = it.next();
					NemaSubmission details = client.getSubmissionDetails(subcode);
					
					String contrib = "";
		    		for(Iterator<NemaContributor> personIt = details.getContributors().iterator(); personIt.hasNext();){
		    			NemaContributor person = personIt.next();
		    			contrib += "[" + person.getAffiliationUrl() + " " + 
		    			person.getFirstName() + " " + person.getLastName() + "]";
		    			if(personIt.hasNext()){
		    				contrib += ", ";
		    			}
		    		}
		    		
		    		table += "\t! " + subcode + "\n" +
		    				"\t| " + details.getSubmissionName() + 
		    				" ||  style=\"text-align: center;\" | [" + details.getAbstractUrl() + " PDF]" +
		    				" || " + contrib + "\n";
		    		
		    		if (it.hasNext()){
		    			table += "\t|-\n";
		    		}else{
		    			table += "\t|}\n";
		    		}
					
				}
				System.out.println("\n" + table);
				
				
			}
			
		}finally{
			if (client != null){
				client.close();
			}
		}
		System.out.println("--bye--");
        
//		{| class="wikitable" style="text-align: center; width: 200px; height: 200px;"
//			|+ Multiplication table
//			|-
//			! × !! 1 !! 2 !! 3
//			|-
//			! 1
//			| 1 || 2 || 3
//			|-
//			! 2
//			| 2 || 4 || 6
//			|-
//			! 3
//			| 3 || 6 || 9
//			|-
//			! 4
//			| 4 || 8 || 12
//			|-
//			! 5
//			| 5 || 10 || 15
//			|}



		
	}

}

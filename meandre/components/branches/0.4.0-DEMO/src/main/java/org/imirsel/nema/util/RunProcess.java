package org.imirsel.nema.util;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class RunProcess {
	
	public static void main(String[] args) throws IOException{
		String file_path = "/home/mertbay/programming/meandre_sndan/Meandre-1.4.5/meandre-instance/published_resources/data/ctpt03.pv.an";
	//	String file_path = "ctpt03.wav" ;
		String command="/home/mertbay/programming/eclipse_workspace/utils/src/com/mertbay/utils/monan " + file_path + " pp"+" ctpt033.eps";
	//	String[] MainCommand = {"/home/mertbay/programming/eclipse_workspace/utils/src/com/mertbay/utils/monan",file_path, "pp","ctpt03.eps"};
		String[] MainCommand = {"/bin/tcsh","-c", command};		
		String fid = "/home/mertbay/sndan/snd_files/tmp";	       
		File outDir = new File(fid);     
		String[] envp={"SNDANDIR", "/home/mertbay/sndan"};
		executeProcessBuilder(MainCommand,outDir,envp);

		
	}
	
	public static void executeProcessBuilder(String[] MainCommand, File  outDir,String[] envp) throws IOException{
			
		 ProcessBuilder pb = new ProcessBuilder(MainCommand);
		 Map<String, String> env = pb.environment();
		 env.put(envp[0],envp[1]);
		 pb.directory(outDir);
		 pb.redirectErrorStream(true);
		 Process process = pb.start();	
	       InputStream is = process.getInputStream();
	       InputStreamReader isr = new InputStreamReader(is);
	       BufferedReader br = new BufferedReader(isr);
	       String line;
	//       System.out.printf("Output of running %s is:\n", Arrays.toString(MainCommand));

	       while ((line = br.readLine()) != null) {
	         System.out.println(line);
	       }       
	       
	}
	
	public static void executeRuntimeExec(String MainCommand,String[] envp, File  outDir) throws IOException{
		String s;
		try{
			
		//Process p = Runtime.getRuntime().exec(MainCommand);			
		Process p = Runtime.getRuntime().exec(MainCommand,envp,outDir);
		//Runtime.getRuntime().e
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        BufferedReader stdErr = new BufferedReader(new InputStreamReader(p.getErrorStream()));
       System.out.println("The command to be executed: " + MainCommand);
        while((s=stdInput.readLine()) !=null){
        	  System.out.print("The stdout of :" + MainCommand + " ");	
        	  System.out.println(s);
        }	        		        	        		        
       while((s=stdErr.readLine()) !=null){
    	   System.out.println("The stderr of :" + MainCommand +" ");	
    	   System.out.println(s);
       }	      		        
       stdErr.close();
       stdInput.close();
       }
		catch(Exception e){
			e.printStackTrace();	
		}				
	}
		
	
	
	
}

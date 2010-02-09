package org.imirsel.nema.components.util;
import java.io.*;
import java.lang.Math;
import java.util.*;


public class ExtractFileFormatMetadata {
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2){
			System.out.println("Usage: ExtractFileFormatMetadata IDsfilename outputfilename");
			
		}
		else{
	        try { extract(args[0],args[1]); }
	        catch (Exception e) {
	            System.err.println(e.getMessage());
	        }   		
		}
 
	}
	
	public static void extract(String idsfilename, String outfilename) throws IOException{
		
		try{
			  BufferedReader ids = new BufferedReader(new FileReader( idsfilename));        
			  int length = 0;     
	          while(ids.readLine()!=null){
	              length++;
	          }
	          System.out.println("The number of IDS is  " + length +"\n");   
	          ids.close();			
	          BufferedReader allids = new BufferedReader(new FileReader( idsfilename));    
	          BufferedWriter out = new BufferedWriter(new  FileWriter(outfilename));
	             
			  for (int i=0;i<length;i++){
				  String id = allids.readLine();
				  out.write("\necho \""+id+":\";	"+"find /data/raid3/audio/ -name " + id + "*;	echo \"end\"");
			  }
			  allids.close();
			  out.close();
	          
	          
		}
        catch (IOException e){
            e.printStackTrace();
        }

          
         
	}

}

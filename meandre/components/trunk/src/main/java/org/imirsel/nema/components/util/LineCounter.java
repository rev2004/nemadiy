package org.imirsel.nema.components.util;
import java.io.BufferedReader;
import java.io.IOException;

public class LineCounter {
	public static int count(BufferedReader in) throws IOException{

		int linecount=0;
		try {
			while( (in.readLine() ) != null){
				linecount++;	
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return linecount;

	}
}

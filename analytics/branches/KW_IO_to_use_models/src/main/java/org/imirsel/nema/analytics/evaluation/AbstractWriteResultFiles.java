package org.imirsel.nema.analytics.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;

public class AbstractWriteResultFiles {

	/**
	 * Prepares a Table Object that encodes the description of the specified task. To be
	 * used to construct introduction pages for the results.
	 * 
	 * @param task The task description to encode.
	 * @return The prepared Table.
	 */
	public static Table prepTaskTable(NemaTask task, NemaDataset dataset) {
	    String[] colNames = new String[2];
	    colNames[0] = "Field";
	    colNames[1] = "Value";
	
	    DecimalFormat dec = new DecimalFormat();
	    dec.setMaximumFractionDigits(2);
	
	    List<String[]> rows = new ArrayList<String[]>();
	    
	    rows.add(new String[]{"Task ID",""+task.getId()});
	    rows.add(new String[]{"Task Name",""+task.getName()});
	    rows.add(new String[]{"Task Description",""+task.getDescription()});
	    rows.add(new String[]{"Subject Metadata ID",""+task.getSubjectTrackMetadataId()});
	    rows.add(new String[]{"Subject Metadata Name",""+task.getSubjectTrackMetadataName()});
	    
	    
	    rows.add(new String[]{"Dataset ID","" + dataset.getId()});
	    rows.add(new String[]{"Dataset Name",""+dataset.getName()});
	    rows.add(new String[]{"Dataset Description",""+dataset.getDescription()});
	    
	    return new Table(colNames, rows);
	}
	
	/**
	 * Writes a Table Object to a CSV file.
	 * @param table Table to write.
	 * @param outputFile File to write to.
	 * @throws IOException
	 */
    public static void writeTableToCsv(Table table, File outputFile) throws IOException{
		int cols = table.getColHeaders().length;
				
		String csv = "*";
		for (int i = 0; i < cols; i++) {
			csv += table.getColHeaders()[i];
		    if (i<cols-1){
		    	csv += ",";
		    }
		}
		csv += "\n";
		
		List<String[]> rows = table.getRows();
		for (Iterator<String[]> iterator = rows.iterator(); iterator.hasNext();) {
			String[] row = iterator.next();
			for (int i = 0; i < cols; i++) {
		    	csv += row[i];
		        if (i<cols-1){
		        	csv += ",";
		        }
		    }
		    csv += "\n";
		}
		
		BufferedWriter output = null;
		try {
		    output = new BufferedWriter(new FileWriter(outputFile));
		    output.write(csv);
		}finally{
			try {
		    	if(output != null){
		    		output.flush();
		    		output.close();
		    	}
			} catch (IOException ex) {}
		}
    }

}
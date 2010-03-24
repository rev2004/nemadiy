package org.imirsel.nema.analytics.evaluation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.imirsel.nema.analytics.evaluation.util.resultpages.Table;
import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;

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
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverTracksAndSystems(List<NemaTrackList> testSets, Map<String,Map<NemaTrackList,List<NemaData>>> jobIDToTrackEval, Map<String,String> jobIDToName, String metricKey) {
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});
    	
    	//set column names
    	int numCols = numAlgos + 2;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        colNames[1] = "Track";
        for (int i = 0; i < numAlgos; i++) {
            colNames[i+2] = jobIDandName[i][1];
        }

        //count number of rows to produce
        int numTracks = 0;
        String firstJob = jobIDandName[0][0];
        {
	        Map<NemaTrackList,List<NemaData>> firstResList = jobIDToTrackEval.get(firstJob);
	        for (Iterator<List<NemaData>> iterator = firstResList.values().iterator(); iterator.hasNext();) {
				List<NemaData> list = iterator.next();
				numTracks += list.size();
			}
        }
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int foldNum = 0;
        NemaTrackList foldList = testSets.get(foldNum);
        int foldTrackCount = 0;
        int actualRowCount = 0;
        String[] row;
        Map<NemaTrackList,List<NemaData>> firstResList;
        NemaData data;
        firstResList = jobIDToTrackEval.get(firstJob);
    	while(actualRowCount < numTracks){
        	if (foldTrackCount == firstResList.get(foldList).size()){
        		foldNum++;
        		foldList  = testSets.get(foldNum);
        	}
        	
        	row = new String[numCols];
        	row[0] = "" + foldList.getFoldNumber();
        	row[1] = firstResList.get(foldList).get(foldTrackCount).getId();
        	for(int i=0;i<numAlgos;i++){
	        		data = jobIDToTrackEval.get(jobIDandName[i][0]).get(foldList).get(foldTrackCount);

	        		if (!data.getId().equals(row[1])){
	        			throw new IllegalArgumentException("Results from job ID: " + jobIDandName[i][0] + " are not ordered the same as results from job ID: " + firstJob);
	        		}
	        		row[i+2] = "" + data.getDoubleMetadata(metricKey);
        	}
        	rows.add(row);

        	actualRowCount++;
        	foldTrackCount++;
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the systems are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverFoldsAndSystems(List<NemaTrackList> testSets, Map<String, Map<NemaTrackList,NemaData>> jobIDToFoldEval, Map<String,String> jobIDToName, String metricKey) {
    	//sort systems alphabetically
    	int numAlgos = jobIDToName.size();
    	String[][] jobIDandName = new String[numAlgos][];
    	int idx=0;
    	String id;
    	for(Iterator<String> it = jobIDToName.keySet().iterator();it.hasNext();){
    		id = it.next();
    		jobIDandName[idx++] = new String[]{id, jobIDToName.get(id)};
    	}
    	Arrays.sort(jobIDandName, new Comparator<String[]>(){
    		public int compare(String[] a, String[] b){
    			return a[1].compareTo(b[1]);
    		}
    	});
    	
    	//set column names
    	int numCols = numAlgos + 1;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        for (int i = 0; i < numAlgos; i++) {
            colNames[i+1] = jobIDandName[i][1];
        }

        //count number of rows to produce
        int numFolds = jobIDToFoldEval.get(jobIDandName[0][0]).size();
        
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int fold = 0;
        String[] row;
        NemaData data;
        for(int f=0;f<numFolds;f++){
        	NemaTrackList foldList = testSets.get(fold);
            row = new String[numCols];
        	row[0] = "" + fold;
        	for(int i=0;i<numAlgos;i++){
        		data = jobIDToFoldEval.get(jobIDandName[i][0]).get(foldList);
        		row[i+1] = "" + data.getDoubleMetadata(metricKey);
        	}
        	rows.add(row);
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the metrics are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverTracks(List<NemaTrackList> testSets, Map<NemaTrackList,List<NemaData>> trackEval, String[] metricKeys) {
    	//set column names
    	int numMetrics = metricKeys.length;
    	int numCols = numMetrics + 2;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        colNames[1] = "Track";
        for (int i = 0; i < numMetrics; i++) {
            colNames[i+2] = metricKeys[i];
        }

        //count number of rows to produce
        int numTracks = 0;
        for (Iterator<List<NemaData>> iterator = trackEval.values().iterator(); iterator.hasNext();) {
			List<NemaData> list = iterator.next();
			numTracks += list.size();
		}
        
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        int foldNum = 0;
        NemaTrackList foldList = testSets.get(foldNum);
        int foldTrackCount = 0;
        int actualRowCount = 0;
        String[] row;
        NemaData data;
        while(actualRowCount < numTracks){
        	if (foldTrackCount == trackEval.get(foldList).size()){
        		foldNum++;
        		foldList = testSets.get(foldNum);
        	}
        	row = new String[numCols];
        	row[0] = "" + foldList.getFoldNumber();
        	row[1] = trackEval.get(foldList).get(foldTrackCount).getId();
        	for(int i=0;i<numMetrics;i++){
        		data = trackEval.get(foldList).get(foldTrackCount);
        		row[i+2] = "" + data.getDoubleMetadata(metricKeys[i]);
        	}
        	rows.add(row);

        	actualRowCount++;
        	foldTrackCount++;
        }
        
        return new Table(colNames, rows);
    }
    
    /**
     * Prepares a Table Object representing the specified evaluation metadata, where the metrics are the columns
     * of the table and the rows are the different tracks in the evaluation.
     * 
     */
    public static Table prepTableDataOverFolds(List<NemaTrackList> testSets, Map<NemaTrackList, NemaData> foldEval, String[] metricKeys) {
    	//set column names
    	int numMetrics = metricKeys.length;
    	int numCols = numMetrics + 1;
        String[] colNames = new String[numCols];
        colNames[0] = "Fold";
        for (int i = 0; i < numMetrics; i++) {
            colNames[i+1] = metricKeys[i];
        }

        //count number of rows to produce
        int numFolds = foldEval.size();
        
        //produce rows (assume but check that results are ordered the same for each system)
        List<String[]> rows = new ArrayList<String[]>();
        String[] row;
        NemaData data;
        for(int i=0;i<numFolds;i++){
        	row = new String[numCols];
        	row[0] = "" + i;
        	for(int m=0;m<numMetrics;m++){
        		data = foldEval.get(testSets.get(i));
        		row[m+1] = "" + data.getDoubleMetadata(metricKeys[m]);
        	}
        	rows.add(row);
        }
        
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
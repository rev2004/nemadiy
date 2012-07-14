/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.imirsel.nema.melodyevaluator.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.fileTypes.TrackListTextFile;

/**
 * A utility for loading and storing properties for the JSTORAuctionCatalogues
 * community editing platform software. 
 * 
 * @author kriswest
 */
public class TaskAndDatasetProperties {

    public static final String TASK_ID = "task_id";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_DESC = "task_description";
    public static final String TASK_META_ID = "task_subjectTrackMetadataID";
    public static final String TASK_META_NAME = "task_subjectTrackMetadataName";

    public static final String DATASET_ID = "dataset_id";
    public static final String DATASET_NAME = "dataset_name";
    public static final String DATASET_DESC = "dataset_description";
    public static final String DATASET_NUMFOLDS = "dataset_numFolds";
    public static final String DATASET_META_ID = "dataset_subjectTrackMetadataId";
    public static final String DATASET_META_NAME = "dataset_subjectTrackMetadataName";
    public static final String DATASET_FILTER_ID = "dataset_filterTrackMetadataId";
    public static final String DATASET_FILTER_NAME = "dataset_filterTrackMetadataName";

    public static final String DATASET_SUBSET_ID = "dataset_subsetTrackListId";
    public static final String DATASET_SUBSET_PATH = "dataset_subsetTrackListPath";

    public static final String DATASET_TEST_IDS = "dataset_testSetIds";
    public static final String DATASET_TEST_FOLD_NUMBERS = "dataset_testSetFoldNumbers";
    public static final String DATASET_TEST_PATHS = "dataset_testSetPaths";

    public static final String DATASET_GT_PATH = "groundtruthPath";
    
    public static final String RESULT_DATA_PATHS = "resultDataPaths";

    private Properties props;
    public TaskAndDatasetProperties (File path) {
    	props = new Properties();
        
        try {
            InputStream iStream = new FileInputStream(path);
            if(iStream != null) {
                props.load(iStream);
            }else {
               throw new RuntimeException("File " + path.getAbsolutePath() + " not found");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public NemaTask getTask(){
    	return new NemaTask(
    			getIntProperty(TASK_ID), 
    			getProperty(TASK_NAME), 
    			getProperty(TASK_DESC),
    			getIntProperty(TASK_META_ID),
    			getProperty(TASK_META_NAME),
    			getIntProperty(DATASET_ID));
    }
    
    public NemaDataset getDataset(){
    	return new NemaDataset(
    			getIntProperty(DATASET_ID), 
    			getProperty(DATASET_NAME), 
    			getProperty(DATASET_DESC), 
    			getIntProperty(DATASET_SUBSET_ID), 
    			getIntProperty(DATASET_NUMFOLDS), 
    			1, //track lists per fold - we are not bothering to load training lists
    			null, 
    			null, 
    			getIntProperty(DATASET_META_ID), 
    			getProperty(DATASET_META_NAME), 
    			getIntProperty(DATASET_FILTER_ID), 
    			getProperty(DATASET_FILTER_NAME)
    		);
    }
    
    public List<NemaTrackList> getTestTrackLists() throws IOException{
    	int[] ids = getIntArrayProperty(DATASET_TEST_IDS);
    	int[] foldNumbers = getIntArrayProperty(DATASET_TEST_FOLD_NUMBERS);
    	String[] paths = getArrayProperty(DATASET_TEST_PATHS);
    	
    	int datasetId = getIntProperty(DATASET_ID);
    	
    	TrackListTextFile reader = new TrackListTextFile();
    	
    	List<NemaTrackList> out = new ArrayList<NemaTrackList>(ids.length);
    	for (int i = 0; i < ids.length; i++) {
    		File path = new File(paths[i]);
    		List<NemaData> trackData = reader.readFile(path);
    		List<NemaTrack> tracks = new ArrayList<NemaTrack>(trackData.size());
    		for(NemaData data:trackData){
    			tracks.add(new NemaTrack(data.getId()));
    		}
			out.add(new NemaTrackList(ids[i], datasetId, 3, "test", foldNumbers[i], tracks));
		}
    	return out;
    }
    
    public Map<String,File> getResultDataPaths(){
    	Map<String,File> out = new HashMap<String, File>();
    	String data = getProperty(RESULT_DATA_PATHS);
    	String[] dataComps = data.split(";");
    	for (int j = 0; j < dataComps.length; j++) {
    		String[] comps = dataComps[j].split(":");
    		if (comps.length != 2){
    			throw new RuntimeException("Failed to parse jobID:/path/to/dir pair: " + dataComps[j]);
    		}
    		
    		out.put(comps[0],new File(comps[1]));
		}
    	return out;
    }
    
    public File getGtFile(){
    	String path = getProperty(DATASET_GT_PATH);
    	if(path == null){
    		return null;
    	}else{
    		return new File(path);
    	}
    }
    
    private String getProperty(String name){
    	String val = props.getProperty(name);
    	if(val == null){
    		Logger.getLogger(TaskAndDatasetProperties.class.getName()).warning("No property value found for property: " + name);
    		return null;
    	}else{
    		return val;
    	}
    }
    
    private int getIntProperty(String name){
    	String val = props.getProperty(name);
    	if(val == null){
    		Logger.getLogger(TaskAndDatasetProperties.class.getName()).warning("No property value found for property: " + name);
    		return -1;
    	}else{
    		return Integer.parseInt(val);
    	}
    }
    
    private int[] getIntArrayProperty(String name){
    	String val = props.getProperty(name);
    	if(val == null){
    		Logger.getLogger(TaskAndDatasetProperties.class.getName()).warning("No property value found for property: " + name);
    		return null;
    	}else{
    		String[] comps = val.split(",");
    		int[] vals = new int[comps.length];
    		for (int i = 0; i < vals.length; i++) {
				vals[i] = Integer.parseInt(comps[i].trim());
			}
    		return vals;
    	}
    }
    
    private String[] getArrayProperty(String name){
    	String val = props.getProperty(name);
    	if(val == null){
    		Logger.getLogger(TaskAndDatasetProperties.class.getName()).warning("No property value found for property: " + name);
    		return null;
    	}else{
    		String[] comps = val.split(",");
    		for (int i = 0; i < comps.length; i++) {
				comps[i] = comps[i].trim();
			}
    		return comps;
    	}
    }
    
}


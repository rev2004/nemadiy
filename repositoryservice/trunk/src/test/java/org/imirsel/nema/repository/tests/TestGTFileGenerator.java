package org.imirsel.nema.repository.tests;


import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.imirsel.nema.model.NemaData;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTask;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.model.fileTypes.BeatTextFile;
import org.imirsel.nema.model.fileTypes.KeyTextFile;
import org.imirsel.nema.model.fileTypes.MelodyTextFile;
import org.imirsel.nema.model.fileTypes.OnsetTextFile;
import org.imirsel.nema.model.fileTypes.StructureTextFile;
import org.imirsel.nema.model.fileTypes.TempoTextFile;
import org.imirsel.nema.model.util.FileConversionUtil;
import org.imirsel.nema.repository.DatasetListFileGenerator;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.junit.Test;

public class TestGTFileGenerator {
	String odir = "C:\\mirexgt\\ame\\mrx09_m5db\\";
	static int taskID = 15;
	static int datasetID = 49;
	static String bitRate = "";
	static String channels = "1";
	static String clip_type = "full";
	static String encoding = "wav";
	static String sample_rate = "44100";
	static String delim = "\t";
	
	private static final Logger logger = Logger.getLogger(TestGTFileGenerator.class.getName());
	
	@Test
	public void testGenerator() throws IOException{
		NemaTask task = null;
		NemaDataset dataset = null;
		RepositoryClientInterface client = null;
		try {
			client = RepositoryClientConnectionPool.getInstance().getFromPool();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			task = client.getTask(taskID);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
			dataset = client.getDataset(task.getDatasetId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        List<NemaData> gtList = null;
        
        //produce Ground-truth list
        System.out.println("TaskSelector: Retrieving ground-truth data and file paths for track list: " + dataset.getSubsetTrackListId() + ", subject metadata id: " + task.getSubjectTrackMetadataId());
        
        List<String> tracks = null;
		try {
			tracks = client.getTrackIDs(dataset.getSubsetTrackListId());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("TaskSelector: Got " + tracks.size() + " tracks for set " + dataset.getSubsetTrackListId());
        
        Map<String, List<NemaMetadataEntry>> trackToMeta = null;
		try {
			trackToMeta = client.getTrackMetadataByID(tracks, task.getSubjectTrackMetadataId());
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        System.out.println("TaskSelector: Got ground-truth data for " + trackToMeta.size() + " tracks");
        
        try {
			gtList = FileConversionUtil.convertMetadataToGroundtruthModel(trackToMeta, task);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println("TaskSelector: Returning " + gtList.size() + " NemaData Objects representing the " + tracks.size() + " tracks");	
        
    
            
        for (Iterator<NemaData> it1 = gtList.iterator(); it1.hasNext();){
        	
        	NemaData gt = it1.next();
        	String gtid = gt.getId();
        	System.out.println(gtid);
        	String ofilen = odir + gtid + ".txt";
        	File oFile = new File(ofilen);
        	MelodyTextFile fw = new MelodyTextFile();
        	fw.writeFile(oFile, gt);
        	

        }
       
		System.out.println("===Done===");
	}
	
	
}

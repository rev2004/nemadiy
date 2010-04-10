package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

import org.imirsel.nema.model.NemaCollection;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaFile;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.RepositoryProperties;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.test.BaseManagerTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepositoryClientImplTest  extends BaseManagerTestCase{

	private RepositoryClientImpl clientImpl; 
	
	@Before
    public void setUp() throws Exception {
		clientImpl = new RepositoryClientImpl();
		this.getLogger().info(RepositoryProperties.DB_LOCATOR + "=" + RepositoryProperties.getProperty(RepositoryProperties.DB_LOCATOR));
		this.getLogger().info(RepositoryProperties.DB_NAME + "=" + RepositoryProperties.getProperty(RepositoryProperties.DB_NAME));
	}
	
	
	@After
	public void tearDown() throws Exception {
		clientImpl = null;
	}
	
	
	@Test
	public final  void testClientConnectionPool(){
		RepositoryClientConnectionPool rcp=RepositoryClientConnectionPool.getInstance();
		RepositoryClientInterface rci=rcp.getFromPool();
		rci.close();
	}

	
	@Test
	public final void testGetDatasets() {
		
		List<NemaDataset> dataset = null;
		try {
			dataset=clientImpl.getDatasets();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(dataset!=null);
		
		for(NemaDataset nd:dataset){
			this.getLogger().info(nd.getName());
		}
		
	}

	@Test
	public final void testGetDataset() {
		
		NemaDataset dataset = null;
		try {
			dataset=clientImpl.getDataset(10);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		assertTrue(dataset!=null);
		
		this.getLogger().info(dataset.toString());
		
	}
	
	@Test
	public final void testGetCollections() {
		
		List<NemaCollection>  collectionList = null;
		
		try {
			collectionList=clientImpl.getCollections();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		assertTrue(collectionList!=null);
		
		for(NemaCollection nc:collectionList){
			this.getLogger().info(nc.getName());
		}
		
	}
	


	@Test
	public final void testGetFile() throws SQLException{
	
		NemaFile nemaFile = null;
		//setup constraint
		HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
        constraint.add(new NemaMetadataEntry("encoding", "mp3"));
	    
        //try to get file
		nemaFile=clientImpl.getFile("b004001", constraint);
		
		//check we got the file
		assertTrue(nemaFile!=null);
		
		this.getLogger().info(nemaFile.toString());
		  
	}
	
	@Test
	public final void testGetTracks(){
		
		List<String> idTrackList = null;
		try {
			idTrackList=clientImpl.getTrackIDs(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(idTrackList!=null);
		
		for(String tl:idTrackList)
			this.getLogger().info(tl.toString());
		
	}


	@Test
	public final void testGetTrackMetadataName() {
		String trackMetaData = null;
		try {
			trackMetaData=clientImpl.getTrackMetadataName(4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(trackMetaData!=null);
		
		this.getLogger().info(trackMetaData);
	}

	@Test
	public final void testGetTrackMetadataID() {
		int trackMetaDataID=0;
		try {
			trackMetaDataID=clientImpl.getTrackMetadataID("Key");
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertTrue(trackMetaDataID != 0);
		
		this.getLogger().info("Metadata ID = " +trackMetaDataID);
	}



	@Test
	public final void testGetTrackMetadataNemaTrack() {
		
		NemaTrack nemaTrack = new NemaTrack("00010b0e1e0854b907ff6d88aff9b78b");
		List<NemaMetadataEntry> nemaMetaDataEntryList = null;
		try {
		nemaMetaDataEntryList= clientImpl.getTrackMetadata(nemaTrack);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(nemaMetaDataEntryList != null);
		
		for(NemaMetadataEntry nd:nemaMetaDataEntryList){
			this.getLogger().info(nd.getValue());
		}
		  
	}


	@Test
	public final void testGetTrackMetadataListOfNemaTrack() {
		
		HashMap<String,List<NemaMetadataEntry>> trackMetadata = new HashMap<String, List<NemaMetadataEntry>>();
		List<NemaTrack> trackList = new ArrayList<NemaTrack>();
		trackList.add(new NemaTrack("000150b65660c701e58e9d4cc97f7bdf"));

		
		try {
			trackMetadata = (HashMap<String, List<NemaMetadataEntry>>) clientImpl.getTrackMetadata(trackList);
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertTrue(trackMetadata != null);
			
			   for (Entry<String,List<NemaMetadataEntry>> entry:trackMetadata.entrySet()) {
		    	this.getLogger().info(entry.getKey().toString());
		    	
		    	
		    	for(NemaMetadataEntry nemaMetadata: entry.getValue()){
		    	this.getLogger().info(nemaMetadata.getType() + "="+ nemaMetadata.getValue());
		    	}
		    	
			   }
		
	}
	
	
	@Test
	public final void testGetTrackMetadataByIDListOfString() {

		HashMap<String,List<NemaMetadataEntry>> trackMetadata = new HashMap<String, List<NemaMetadataEntry>>();
		List<String> list = new ArrayList<String>();
		list.add("m000321");
		
		try {
			trackMetadata = (HashMap<String, List<NemaMetadataEntry>>) clientImpl.getTrackMetadataByID(list, 1);
			} catch (Exception e) {
				e.printStackTrace();
			}
			assertTrue(trackMetadata != null);
			
			   for (Entry<String,List<NemaMetadataEntry>> entry:trackMetadata.entrySet()) {
		    	this.getLogger().info(entry.getKey().toString());
		    	
		    	
		    	for(NemaMetadataEntry nemaMetadata: entry.getValue()){
		    	this.getLogger().info(nemaMetadata.getType() + ":"+ nemaMetadata.getValue());
		    	}
		    	
			   }
 }	
		

}

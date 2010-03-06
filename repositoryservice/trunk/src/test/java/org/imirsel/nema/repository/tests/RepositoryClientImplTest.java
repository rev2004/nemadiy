package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.model.NemaCollection;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaFile;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.imirsel.nema.repository.RepositoryProperties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class RepositoryClientImplTest {

	private RepositoryClientImpl clientImpl; 
	
	@Before
    public void setUp() throws Exception {
		clientImpl = new RepositoryClientImpl();
		System.out.println(RepositoryProperties.DB_LOCATOR + "=" + RepositoryProperties.getProperty(RepositoryProperties.DB_LOCATOR));
		System.out.println(RepositoryProperties.DB_NAME + "=" + RepositoryProperties.getProperty(RepositoryProperties.DB_NAME));
	}
	
	
	@After
	public void tearDown() throws Exception {
		clientImpl = null;
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
			System.out.println(nd.getName());
		}
		
	}

	@Test
	public final void testGetDataset() {
		
		NemaDataset dataset = null;
		try {
			dataset=clientImpl.getDataset(10);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(dataset!=null);
		
		  System.out.println(dataset);
				
	}
	
	@Test
	public final void testGetCollections() {
		
		List<NemaCollection>  collectionlist = null;
		
		try {
			collectionlist=clientImpl.getCollections();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(collectionlist!=null);
		
		for(NemaCollection nc:collectionlist){
			System.out.println(nc.getName());
		}
		
	}
	
	@Test
	public final void testGetDatasetSubset(){
		NemaTrackList tracklist = null;
		try {
			tracklist=clientImpl.getDatasetSubset(10);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(tracklist!=null);
		
		  System.out.println(tracklist);
	}
	

	@Test
	public final void testGetFile(){
	
		NemaFile filelist = null;
		 HashSet<NemaMetadataEntry> constraint = new HashSet<NemaMetadataEntry>();
	            constraint.add(new NemaMetadataEntry("encoding", "mp3"));
	        
		try {
			filelist=clientImpl.getFile("b004001", constraint);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(filelist!=null);
		
		  System.out.println(filelist);
		  
		  // quotes around ? 
		  // line 532
		  // change db name in query to dev db 
	}
	
//	@Test
//	public final void testGetTracks(){
//	//return list<NemaTrack>, int	
//		List<String> idtracklist = null;
//		try {
//			idtracklist=clientImpl.getTrackIDs(1);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertTrue(idtracklist!=null);
//		
//		  System.out.println(idtracklist);
//	}

//	@Test
//	public final void testGetTrackMetadataNameMap() {
//		fail("Not yet implemented"); // TODO
//	}

//	@Test
//	public final void testGetTrackMetadataName() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataID() {
//		fail("Not yet implemented"); // TODO
//	}



//	@Test
//	public final void testGetTrackMetadataNemaTrack() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataByIDString() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataListOfNemaTrack() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataByIDListOfString() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataByIDStringInt() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataNemaTrackInt() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataByIDListOfStringInt() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTrackMetadataListOfNemaTrackInt() {
//		fail("Not yet implemented"); // TODO
//	}


}

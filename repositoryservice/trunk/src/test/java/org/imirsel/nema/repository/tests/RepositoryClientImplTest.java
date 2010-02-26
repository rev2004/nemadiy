package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.model.NemaCollection;
import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.model.NemaMetadataEntry;
import org.imirsel.nema.model.NemaTrack;
import org.imirsel.nema.model.NemaTrackList;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class RepositoryClientImplTest {

	private RepositoryClientImpl clientImpl; 
	
	@Before
    public void setUp() throws Exception {
		clientImpl = new RepositoryClientImpl();
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
			// TODO Auto-generated catch block
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
	
//	@Test
//	public final void testGetCollectionSubset(){
//		NemaTrackList tracklist = null;
//		try {
//			tracklist=clientImpl.getCollectionSubset(10);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		assertTrue(tracklist!=null);
//		
//		  System.out.println(tracklist);
//	}
	

//	@Test
//	public final void testGetFile(NemaTrack track, Set<NemaMetadataEntry> constraint){
//	//return NemaFile	
//	}
//	
//	@Test
//	public final void testGetTracks(NemaTrackList set){
//	//return list<NemaTrack>	
//	}
//
//	@Test
//	public final void testGetTrackMetadataNameMap() {
//		fail("Not yet implemented"); // TODO
//	}
//
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

package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class RepositoryClientImplTest {

	private RepositoryClientImpl clientImpl; 
	
	@Before
    public void setUp() throws Exception {
		System.out.println("1");
		clientImpl = new RepositoryClientImpl();
	}
	
	
	@After
	public void tearDown() throws Exception {
		clientImpl = null;
	}

	
	@Test
	public final void testgetDatasets() {
		System.out.println("IN TEST GET DATASETS");
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
//
//	@Test
//	public final void testGetCollections() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetTracksInt() {
//		fail("Not yet implemented"); // TODO
//	}
//
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
//
//	@Test
//	public final void testGetFileNemaTrackSetOfNemaMetadataEntry() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetCollectionVersionsNemaCollection() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testGetCollectionVersionsInt() {
//		fail("Not yet implemented"); // TODO
//	}

}

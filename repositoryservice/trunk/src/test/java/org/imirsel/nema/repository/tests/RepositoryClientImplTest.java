package org.imirsel.nema.repository.tests;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.imirsel.nema.model.NemaDataset;
import org.imirsel.nema.repository.RepositoryClientImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class RepositoryClientImplTest {

	private RepositoryClientImpl clientimpl; 
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

    public void setUp() throws Exception {
		
//		String test_url = "gserve086.lis.illinois.edu";
//		String DB_PASS = "reduxer101";
//		String DB_USER = "nema_user";
//		String test_db = "nemadatarepository_dev";
	//	conn = new DatabaseConnector(test_url, test_db, test_username, test_password);
		
		clientimpl = new RepositoryClientImpl();
	}
	
	
	@After
	public void tearDown() throws Exception {
		clientimpl = null;
	}

	
	@Test
	public final void testgetDatasets() {
		
		List<NemaDataset> dataset = null;
		try {
			dataset=clientimpl.getDatasets();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(dataset!=null);
//		{
//		for(Iterator<NemaDataset> it= dataset.iterator(); it.hasNext();){
//			System.out.println(dataset.get(it.next()));
//		}
//		}
	}

	

	@Test
	public final void testGetTrackMetadataNameMap() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataName() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataID() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetCollections() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTracksInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataNemaTrack() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataByIDString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataListOfNemaTrack() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataByIDListOfString() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataByIDStringInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataNemaTrackInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataByIDListOfStringInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTrackMetadataListOfNemaTrackInt() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetFileNemaTrackSetOfNemaMetadataEntry() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetCollectionVersionsNemaCollection() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetCollectionVersionsInt() {
		fail("Not yet implemented"); // TODO
	}

}

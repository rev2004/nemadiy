/**
 * 
 */
package org.imirsel.nema.webapp.jobs;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.TreeSet;

import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.junit.Before;
import org.junit.Test;

/**
 * @author guojun
 * 
 */
public class DisplayResultSetTest {

	Set<JobResult> results;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	private void push(Set<JobResult> list, Long id, Job job, String type,
			String url) {
		JobResult result = new JobResult();
		result.setResultType(type);
		result.setId(id);
		result.setJob(job);
		result.setUrl(url);
		list.add(result);
	}

	/**
	 * Test method for
	 * {@link org.imirsel.nema.webapp.jobs.DisplayResultSet#DisplayResultSet(java.util.Set)}
	 * .
	 */
	@Test
	public void testDisplayResultSet() {
		
		//test for normal result set. 
		
		//set up result set
		final String PATH = "http://domain.com/test/tste1/";
		String[] nameList = { "test.png", "txt.txt", "test.JPG", "image/",
				"tes.jfdasjf.dslka.jkfjdsk.fsdfdsft.fhsdjfhdskjfh.txt" };
		results = new HashSet<JobResult>();

		push(results, 50L, new Job(), "dir", PATH);
		long id = 100L;
		for (String s : nameList) {
			push(results, id++, new Job(), "file", PATH + s);
		}

		Set<String> urlSetKey = new HashSet<String>();
		Set<String> displaySetKey = new HashSet<String>();
		for (String s : nameList) {
			urlSetKey.add(PATH + s);
		}
		displaySetKey.add("<img width='150' src='" + PATH + nameList[0]
				+ "' alt='" + nameList[0] + "'/>");
		displaySetKey.add( nameList[1]);
		displaySetKey.add("<img width='150' src='" + PATH + nameList[2]
				+ "' alt='" + nameList[2] + "'/>");
		displaySetKey.add("image");
		displaySetKey.add("tes.j...fh.txt");
		
		//test

		DisplayResultSet set = new DisplayResultSet(results);
		assertEquals(set.getRoot().getUrl(), "http://domain.com/test/tste1/");
		assertEquals(set.getRoot().getDisplayString(), "result directory");
		List<DisplayResult> children = set.getChildren();
		assertTrue(children.size()==5);
		
		Set<String> urlSet = new HashSet<String>();
		Set<String> displaySet = new HashSet<String>();

		for (DisplayResult dresult : children) {
			urlSet.add(dresult.getUrl());
			displaySet.add(dresult.getDisplayString());
		}
		assertEquals(urlSetKey,urlSet);
		assertEquals(displaySetKey,displaySet);
		
		
		//test for none list
		DisplayResultSet setNull = new DisplayResultSet(
				new HashSet<JobResult>());
		assertEquals("",setNull.getRoot().getUrl());
		assertEquals("no result",setNull.getRoot().getDisplayString());
		assertNotNull(setNull.getChildren());
		assertTrue(0==setNull.getChildren().size());
		
		
		//test for special index type 
		//with index.htm file
		push(results,1000L, new Job(),"file",PATH+"INDEX.htm");
		DisplayResultSet indexSet=new DisplayResultSet(results);
		assertEquals(PATH+"INDEX.htm",indexSet.getRoot().getUrl());
		assertEquals("results",indexSet.getRoot().getDisplayString());
		assertNotNull(indexSet.getChildren());
		assertTrue(0==indexSet.getChildren().size());
		
		//with evaluation path. 
		final String PATH2="http://domain.com/fjaskfl/dfjldks;jfds/evaluation/";
		results = new HashSet<JobResult>();

		push(results, 50L, new Job(), "dir", PATH2);
		
		for (String s : nameList) {
			push(results, id++, new Job(), "file", PATH2 + s);
		}
		indexSet=new DisplayResultSet(results);
		assertEquals(PATH2,indexSet.getRoot().getUrl());
		assertEquals("results",indexSet.getRoot().getDisplayString());
		assertNotNull(indexSet.getChildren());
		assertTrue(0==indexSet.getChildren().size());
		
		
	}
}

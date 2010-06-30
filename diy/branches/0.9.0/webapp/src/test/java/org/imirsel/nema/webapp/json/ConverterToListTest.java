/**
 * 
 */
package org.imirsel.nema.webapp.json;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.model.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * @author gzhu1
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/test-bean.xml", "/testJsonConverter-bean.xml" })
public class ConverterToListTest {

	@Resource
	Job job1;
	@Resource
	Job job2;
	@Resource
	Job job3;
	@Resource
	Map<String,String> shortJob1;
	@Resource
	Map<String,String> shortJob2;
	@Resource
	Map<String,String> shortJob3;

	
	@Resource
	Map<MeandreServerProxyConfig,MeandreServerProxyStatus> serverMap;
	@Resource 
	List<Map<String,String>> expectedServerList;


	/**
	 * Test method for {@link org.imirsel.nema.webapp.json.ConverterToList#convertToList(java.util.Collection, org.imirsel.nema.webapp.json.ConverterToMap)}.
	 */
	@DirtiesContext
	@Test
	public final void testConvertToList() {
		ConverterToList<Job> converter=new ConverterToList<Job>();
		
		List<Job> jobs=new ArrayList<Job>();
		jobs.add(job1);
		jobs.add(job2);
		jobs.add(job3);
		
		shortJob1.put("updateTimestamp",job1.getUpdateTimestamp().toString());
		shortJob2.put("updateTimestamp",job2.getUpdateTimestamp().toString());
		shortJob3.put("updateTimestamp",job3.getUpdateTimestamp().toString());
		
		List<Map<String,String>> expected=new ArrayList<Map<String,String>>();
		expected.add(shortJob1); 
		expected.add(shortJob2); 
		expected.add(shortJob3);
		assertEquals(expected, converter.convertToList(jobs, new ConverterToMapJob()));
		
		ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>> converter2
			=new ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>>();
		assertEquals(expectedServerList, converter2.convertToList(serverMap.entrySet(),new ConverterToMapServer()));
	}

}

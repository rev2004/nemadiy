package org.imirsel.nema.webapp.json;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.imirsel.nema.model.Job;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test for both {@link ConverterToMapJob} and {@link ConverterToMapJobLong} 
 * @author gzhu1
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/test-bean.xml","/testJsonConverter-bean.xml" })
public class ConverterToMapJobTest {

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
	Map<String,String> extraJob1;
	
	@Resource
	Map<String,String> extraJob2;
	
	@Resource
	Map<String,String> extraJob3;
	
	
	
	@DirtiesContext
	@Test
	public final void testConvertToMapShort() {
		
		ConverterToMap<Job> converter=new ConverterToMapJob();
		
		shortJob1.put("updateTimestamp",job1.getUpdateTimestamp().toString());
		assertEquals(shortJob1,converter.convertToMap(job1));
		shortJob2.put("updateTimestamp",job2.getUpdateTimestamp().toString());
		assertEquals(shortJob2,converter.convertToMap(job2));
		shortJob3.put("updateTimestamp",job3.getUpdateTimestamp().toString());
		assertEquals(shortJob3,converter.convertToMap(job3));
		
		Date date=new Timestamp(100000L);
		job3.setScheduleTimestamp(date);
		Date date2=new Timestamp(200000L);
		job3.setSubmitTimestamp(date2);
		shortJob3.put("scheduleTimestamp",date.toString());
		shortJob3.put("submitTimestamp", date2.toString());
		shortJob3.put("updateTimestamp",job3.getUpdateTimestamp().toString());
		assertEquals(shortJob3,converter.convertToMap(job3));
	}
	
	@DirtiesContext
	@Test
	public final void testConvertToMapLong() {
		
		ConverterToMap<Job> converter=new ConverterToMapJobLong();
		
		shortJob1.putAll(extraJob1);
		shortJob1.put("updateTimestamp",job1.getUpdateTimestamp().toString());
		assertEquals(shortJob1,converter.convertToMap(job1));
		
		shortJob2.putAll(extraJob2);
		shortJob2.put("updateTimestamp",job2.getUpdateTimestamp().toString());
		assertEquals(shortJob2,converter.convertToMap(job2));
		
		shortJob3.putAll(extraJob3);
		shortJob3.put("updateTimestamp",job3.getUpdateTimestamp().toString());
		assertEquals(shortJob3,converter.convertToMap(job3));
		
		Date date=new Timestamp(100000L);
		job3.setScheduleTimestamp(date);
		Date date2=new Timestamp(200000L);
		job3.setSubmitTimestamp(date2);
		shortJob3.put("scheduleTimestamp",date.toString());
		shortJob3.put("submitTimestamp", date2.toString());
		shortJob3.put("updateTimestamp",job3.getUpdateTimestamp().toString());
		assertEquals(shortJob3,converter.convertToMap(job3));
	}

}

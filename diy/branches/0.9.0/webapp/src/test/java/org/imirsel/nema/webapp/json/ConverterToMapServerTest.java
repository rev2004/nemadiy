/**
 * 
 */
package org.imirsel.nema.webapp.json;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Testing both {@link ConverterToMapServer} and  {@link ConverterToMapServerConfiger}
 * @author gzhu1
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/testJsonConverter-bean.xml" })
public class ConverterToMapServerTest {

	@Resource
	MeandreServerProxyConfig mockMeandreServerProxyConfig;
	@Resource
	MeandreServerProxyConfig mockMeandreServerProxyConfig2;
	@Resource
	MeandreServerProxyStatus mockMeandreServerProxyStatus1;
	@Resource
	MeandreServerProxyStatus mockMeandreServerProxyStatus2;
	@Resource 
	Map<String,String> expectedMeandreServerProxyConfig;
	@Resource 
	Map<String,String> expectedMeandreServerProxyConfig2;
	@Resource 
	Map<String,String> expectedMeandreServer1;
	@Resource 
	Map<String,String> expectedMeandreServer2;

	
	
	@Test
	public final void testConvertToMapSrverConfig(){
		ConverterToMap<MeandreServerProxyConfig> converter=new ConverterToMapServerConfig();
		assertEquals(expectedMeandreServerProxyConfig,converter.convertToMap(mockMeandreServerProxyConfig));
		assertEquals(expectedMeandreServerProxyConfig2,converter.convertToMap(mockMeandreServerProxyConfig2));
	}
	
	@DirtiesContext
	@Test
	public final void testConvertToMapSrver(){
		ConverterToMap<Entry<MeandreServerProxyConfig,MeandreServerProxyStatus>> converter=new ConverterToMapServer();
		
		Map<MeandreServerProxyConfig,MeandreServerProxyStatus> map=new HashMap<MeandreServerProxyConfig,MeandreServerProxyStatus>();
		map.put(mockMeandreServerProxyConfig, mockMeandreServerProxyStatus1);
		assertEquals(expectedMeandreServer1,converter.convertToMap((Entry<MeandreServerProxyConfig,MeandreServerProxyStatus>)map.entrySet().toArray()[0]));
		
		map=new HashMap<MeandreServerProxyConfig,MeandreServerProxyStatus>();
		map.put(mockMeandreServerProxyConfig2, mockMeandreServerProxyStatus2);
		assertEquals(expectedMeandreServer2,converter.convertToMap((Entry<MeandreServerProxyConfig,MeandreServerProxyStatus>)map.entrySet().toArray()[0]));
		
	}
	
		
}

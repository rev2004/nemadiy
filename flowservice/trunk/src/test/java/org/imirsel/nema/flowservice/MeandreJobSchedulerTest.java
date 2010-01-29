package org.imirsel.nema.flowservice;


import java.util.HashSet;
import java.util.Set;

import org.imirsel.nema.flowservice.config.MeandreJobSchedulerConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MeandreJobSchedulerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	      MeandreJobSchedulerConfig config =
	          new MeandreJobSchedulerConfig() {
	             public MeandreServerProxy getHead() {
	            		String host ="localhost";
	            		String password = "admin";
	            		String username ="admin";
	            		int port = 1714;
	            		int maxConcurrentJobs =1;
	            		
	            		MeandreServerProxyConfig config = new MeandreServerProxyConfig(
	            				username,password,host,port,maxConcurrentJobs);
	            				
	                    MeandreServerProxy server = new MeandreServerProxy(config);
	                    
	                    return server;
	             }

	             public Set<MeandreServerProxy> getServers() {
	                Set<MeandreServerProxy> servers = new HashSet<MeandreServerProxy>();
	                
	                
	                String host ="192.168.0.1";
	                String host1 ="192.168.0.2";
	                String host2 ="192.168.0.3";
	                String host3 ="192.168.0.4";
            		String password = "admin";
            		String username ="admin";
            		int port = 1714;
            		int maxConcurrentJobs =1;
            		
            		MeandreServerProxyConfig config = new 
            		MeandreServerProxyConfig(username,password,host,port,maxConcurrentJobs);
            		MeandreServerProxyConfig config1 = new 
            		MeandreServerProxyConfig(username,password,host1,port,maxConcurrentJobs);
            		MeandreServerProxyConfig config2 = new 
            		MeandreServerProxyConfig(username,password,host2,port,maxConcurrentJobs);
            		MeandreServerProxyConfig config3 = new 
            		MeandreServerProxyConfig(username,password,host3,port,maxConcurrentJobs);
    
            		servers.add(new MeandreServerProxy(config));
	                servers.add(new MeandreServerProxy(config1));
	                servers.add(new MeandreServerProxy(config2));
	                servers.add(new MeandreServerProxy(config3));
	                return servers;
	             }
	          };
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void emptyText(){}

}

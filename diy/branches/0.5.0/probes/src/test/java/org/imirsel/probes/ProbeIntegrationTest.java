package org.imirsel.probes;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.imirsel.dao.JobDao;
import org.imirsel.meandre.client.ExecResponse;
import org.imirsel.meandre.client.MeandreClient;
import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.model.Job;
import org.imirsel.model.Job.JobStatus;

public class ProbeIntegrationTest {
	
	private static Logger logger = Logger.getAnonymousLogger();

	
	
	public static void main(String args[]){
		Logger logger = Logger.getAnonymousLogger();
		String sFlowUrl ="http://test.org/datatypetest/";
		String token = "tokenDD-"+System.currentTimeMillis();
		MeandreClient meandreClient  = new MeandreClient("128.174.154.145", 11709);
		meandreClient.setLogger(logger);
		meandreClient.setCredentials("admin", "admin");
		ExecResponse uriExecutionInstance=null;
		try {
			HashMap<String,String> hmap = new HashMap<String,String>();
			hmap.put("nema","true");
			int statusCode = Job.JobStatus.SUBMITTED.getCode();
			DataSource dataSource = getDataSource();
			JobDao jdao = new JobDao(dataSource);
			Job job = new Job();
			job.setDescription("description");
			job.setExecutionInstanceId("N/A");
			job.setHost("N/A");
			job.setName("name");
			job.setOwnerEmail("amitku@uiuc.edu");
			job.setOwnerId(22l);
			job.setPort(-1);
			job.setStatusCode(statusCode);
			job.setToken(token);
			job.setSubmitTimestamp(new Date());
			
			jdao.insertJob(job);
			
			
			
			
			meandreClient.runAsyncFlow(sFlowUrl, token,hmap);
			uriExecutionInstance = meandreClient.getFlowExecutionInstanceId(token);
			
			job.setExecutionInstanceId(uriExecutionInstance.getUri());
			job.setHost(uriExecutionInstance.getHostname());
			job.setPort(uriExecutionInstance.getPort());
			//System.out.println("===> " + uriExecutionInstance.getUri());
			//System.out.println("END: " + System.currentTimeMillis() );
			
			
			job.setHost(uriExecutionInstance.getHostname());
			job.setPort(uriExecutionInstance.getPort());
			jdao.updateHostAndPort(job);
		
		
		
		} catch (TransmissionException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	private static DataSource getDataSource() throws Exception {
		String driver ="com.mysql.jdbc.Driver";//properties.getProperty("driver");
		String jdbc_url = "jdbc:mysql://128.174.154.145:3306/flowservice?relaxAutoCommit=true&amp;useUnicode=true&amp;characterEncoding=UTF-8&amp;autoReconnect=true&amp;autoReconnectForPools=true";//properties.getProperty("jdbc_url");
		String user = "nema_user";
		String password ="reduxer101";
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new Exception("Missing database driver: "+ driver);
		}

		
		DataSource	dataSource = setupDataSource(jdbc_url, user, password);
		try {
			Connection connection = dataSource.getConnection();
			if(connection==null){
				logger.severe("Error: Connection is null..." + jdbc_url);
				throw new Exception("Error: Connection is null..." + jdbc_url);
			}
			System.out.println(connection.getMetaData().getDatabaseProductName());
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error: Could not connect to the server: " + jdbc_url +" \nuser: " +user + "\npassword: " + password);
			System.exit(0);
		}
		return dataSource;
	}
	
	private static  DataSource setupDataSource(String jdbc_url, String user,String password) {
		//System.out.println("Setting up jdbc datasource: " + jdbc_url + " user: " + user);
		GenericObjectPool connectionPool = new GenericObjectPool(null);
		/*ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(jdbc_url, user, password);
		PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
				connectionFactory, connectionPool, null, null, false, false);
		*/PoolingDataSource dataSource = new PoolingDataSource(connectionPool);
		return dataSource;
	}

}

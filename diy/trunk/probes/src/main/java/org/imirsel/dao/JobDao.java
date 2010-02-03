package org.imirsel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.imirsel.annotations.SqlPersistence;
import org.imirsel.model.Job;
import org.imirsel.util.JobStatus;


public class JobDao extends SQLDao{

	public JobDao(DataSource dataSource) {
		super(dataSource);
	}
	
	
	public Job getJob(String _token){
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlSelect =mdata.select();
		if(sqlSelect.equals("[unassigned]")){
			System.out.println("Error: sql select for Job.class "+ sqlSelect);
			return null;
		}
		 Connection con = null;
         try {
                con = getDataSource().getConnection();
         }catch(SQLException e) {
                System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
                return  null;
         }
         PreparedStatement createSelect = null;
         try {
        	createSelect= con.prepareStatement(sqlSelect);
			createSelect.setString(1, _token);
			createSelect.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Job job=null;
		ResultSet rs=null;
		try {
			rs=createSelect.getResultSet();
			rs.first();
			String name=rs.getString("name");
			String description=rs.getString("description");
			String host=rs.getString("host");
			int port=rs.getInt("port");
			long su=rs.getDate("submitTimestamp").getTime();
			long st=rs.getDate("startTimestamp").getTime();
			long et=rs.getDate("endTimestamp").getTime();
			long ut=rs.getDate("updateTimestamp").getTime();
			int statusCode=rs.getInt("statusCode");
			long ownerId=rs.getLong("ownerId");
			String ownerEmail=rs.getString("ownerEmail");
			String executionInstanceId=rs.getString("executionInstanceId");
			String token = rs.getString("token");
			
			job = new Job();
			job.setName(name);
			job.setDescription(description);
			job.setHost(host);
			job.setPort(port);
			job.setOwnerEmail(ownerEmail);
			job.setExecutionInstanceId(executionInstanceId);
			job.setOwnerId(ownerId);
			job.setStatusCode(statusCode);
			job.setToken(token);
			
			job.setSubmitTimestamp(new Date(su));
			job.setStartTimestamp(new Date(st));
			job.setEndTimestamp(new Date(et));
			job.setUpdateTimestamp(new Date(ut));
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return job;
	}
	
	
	public int finishJob(String token){
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlFinish =mdata.finish();
		if(sqlFinish.equals("[unassigned]")){
			System.out.println("Error: sql select for Job.class "+ sqlFinish);
			return -1;
		}
		 Connection con = null;
         try {
                con = getDataSource().getConnection();
         }catch(SQLException e) {
                System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
                return  -1;
         }
         PreparedStatement updateStmt = null;
         int result=-1;
         try {
        	 updateStmt= con.prepareStatement(sqlFinish);
        	 updateStmt.setInt(1,JobStatus.FINISHED);
        	 updateStmt.setString(2,token);
        	 result=updateStmt.executeUpdate();
         }catch (SQLException e) {
         	// TODO Auto-generated catch block
 			e.printStackTrace();
 		}finally{
 			try {
 				con.commit();
 				con.close();
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}
         return result;
 	}

	
	public int startJob(String token, int status){
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlStart =mdata.start();
		if(sqlStart.equals("[unassigned]")){
			System.out.println("Error: sql select for Job.class "+ sqlStart);
			return -1;
		}
		 Connection con = null;
         try {
                con = getDataSource().getConnection();
         }catch(SQLException e) {
                System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
                return  -1;
         }
         PreparedStatement updateStmt = null;
         int result=-1;
         try {
        	 updateStmt= con.prepareStatement(sqlStart);
        	 updateStmt.setInt(1,status);
        	 updateStmt.setString(2,token);
        	 result=updateStmt.executeUpdate();
         }catch (SQLException e) {
         	// TODO Auto-generated catch block
 			e.printStackTrace();
 		}finally{
 			try {
 				con.commit();
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}
         return result;
 	}
	
	public boolean insertJob(Job job){
		boolean success=Boolean.FALSE;
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlStore =mdata.store();
		if(sqlStore.equals("[unassigned]")){
			System.out.println("Error: sql select for Job.class "+ sqlStore);
			return false;
		}
		 Connection con = null;
         try {
                con = getDataSource().getConnection();
         }catch(SQLException e) {
                System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
                return  false;
         }
         PreparedStatement createSelect = null;
         try {
			createSelect= con.prepareStatement(sqlStore);
			createSelect.setString(1,job.getName());
			createSelect.setString(2,job.getDescription());
			createSelect.setString(3,job.getToken());
			createSelect.setString(4,job.getHost());
			createSelect.setInt(5,job.getPort());
			createSelect.setString(6,job.getExecutionInstanceId());
			createSelect.setString(7,job.getStatusCode()+"");
			createSelect.setLong(8,job.getOwnerId());
			createSelect.setString(9,job.getOwnerEmail());
			if(job.getSubmitTimestamp()!=null){
				createSelect.setDate(10,new java.sql.Date(job.getSubmitTimestamp().getTime()));
			}
			createSelect.setString(11,job.getPort()+"");
			createSelect.setInt(12,0);
			createSelect.setInt(13,0);
			System.out.println("Executing: "+ createSelect.toString());
			success=createSelect.execute();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				con.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return success;
	}


	public int updateHostAndPort(Job job) {
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlFinish =mdata.updateHostAndPort();
		if(sqlFinish.equals("[unassigned]")){
			System.out.println("Error: sql select for Job.class "+ sqlFinish);
			return -1;
		}
		 Connection con = null;
         try {
                con = getDataSource().getConnection();
         }catch(SQLException e) {
                System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
                return  -1;
         }
         PreparedStatement updateStmt = null;
         int result=-1;
         try {
        	 updateStmt= con.prepareStatement(sqlFinish);
        	 System.out.println("UPDATE: " + job.getHost()  + "  " + job.getPort() + "  --> for " + job.getExecutionInstanceId() );
        	 updateStmt.setString(1,job.getHost());
        	 updateStmt.setInt(2,job.getPort());
        	 updateStmt.setString(3,job.getExecutionInstanceId());
        	 result=updateStmt.executeUpdate();
         }catch (SQLException e) {
         	// TODO Auto-generated catch block
 			e.printStackTrace();
 		}finally{
 			try {
 				con.commit();
 			} catch (SQLException e) {
 				// TODO Auto-generated catch block
 				e.printStackTrace();
 			}
 		}
         return result;
		
	}
	
	
	
}

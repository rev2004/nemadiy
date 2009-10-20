package org.imirsel.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.sql.DataSource;

import org.imirsel.annotations.SqlPersistence;
import org.imirsel.model.Job;


public class JobDao extends SQLDao{

	public JobDao(DataSource dataSource) {
		super(dataSource);
	}
	
	
	public Job getJob(String token){
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
			createSelect.setString(1, token);
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
			
			job = new Job();
			job.setName(name);
			job.setDescription(description);
			job.setHost(host);
			job.setPort(port);
			job.setOwnerEmail(ownerEmail);
			job.setExecutionInstanceId(executionInstanceId);
			job.setOwnerId(ownerId);
			job.setStatusCode(statusCode);
			
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

	
	public int updateJob(String token, int status){
		return status;
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
			createSelect.setString(6,job.getStatusCode()+"");
			createSelect.setLong(7,job.getOwnerId());
			createSelect.setString(8,job.getOwnerEmail());
			createSelect.setString(9,job.getExecutionInstanceId());
			
			
				if(job.getSubmitTimestamp()!=null){
					createSelect.setDate(10,new java.sql.Date(job.getSubmitTimestamp().getTime()));
				}
				if(job.getStartTimestamp()!=null){
					createSelect.setDate(11,new java.sql.Date(job.getStartTimestamp().getTime()));
				}
				if(job.getEndTimestamp()!=null){
					createSelect.setDate(12,new java.sql.Date(job.getEndTimestamp().getTime()));
				}
				if(job.getUpdateTimestamp()!=null){
					createSelect.setDate(13,new java.sql.Date(job.getUpdateTimestamp().getTime()));
				}
				
			
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
	
	
	
}

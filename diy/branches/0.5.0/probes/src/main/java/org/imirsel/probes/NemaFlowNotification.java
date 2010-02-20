package org.imirsel.probes;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.imirsel.annotations.SqlPersistence;
import org.imirsel.model.Job;
import org.imirsel.model.JobResult;
import org.imirsel.nema.artifactservice.ArtifactManagerImpl;
import org.imirsel.nema.directoryservice.JndiHelper;
import org.meandre.core.engine.Probe;


/** This FlowNotification is used by the Meandre Server to persist the 
 * notifications to the database.
 * 
 * @author Amit Kumar
 * @date-created 09/01/2009
 * @date-updated 11/12/2009 -probe finish updated
 *
 */
public class NemaFlowNotification implements Probe {
	
	
	private static final Logger  LOGGER = Logger.getAnonymousLogger();
	

	private static DataSource dataSource=null;
    static{
	try {
		dataSource= JndiHelper.getJobStatusDataSource();
	} catch (Exception e) {
		LOGGER.severe("Error could not get dataSource for NEMA...\n");
	}
    }

	/** Invoked when the probe object get instantiated.
	 * 
	 */
	public void initialize (){
		 
	}
	
	/** Invoked when the probe object has finished its life cycle.
	 * 
	 */
	public void dispose(){
		System.out.println("Probe Disposed");
	}
	
	/** Returns the serialized probe information.
	 * 
	 */
	public String serializeProbeInformation(){
		return "NEMAFlowNotification";
	}
	
	/** The flow started executing.
	 * 
	 * @param sFlowUniqueID The unique execution flow ID
	 * @param ts The time stamp
	 */
	public void probeFlowStart(String sFlowUniqueID, Date ts, String weburl){
		LOGGER.info("Flow Started: " + ts.toString()+ " " +sFlowUniqueID + "  " + weburl);
		LOGGER.info("Flow Started: " + Job.JobStatus.STARTED.getCode());
		// create the directories for this flow
		try {
			ArtifactManagerImpl.getInstance().getProcessWorkingDirectory(sFlowUniqueID);
			ArtifactManagerImpl.getInstance().getResultLocationForJob(sFlowUniqueID);
			ArtifactManagerImpl.getInstance().getCommonStorageLocation();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlUpdate =mdata.start();
		if(sqlUpdate.equals("[unassigned]")){
			LOGGER.severe("probeFlowStart: Ignoring sql Update for Job.class "+ sqlUpdate);
			return;
		}
		Connection con = null;
        try{
        	con = dataSource.getConnection();
        }catch(SQLException e) {
        	LOGGER.severe("Error getting connection from the Job dataSource " + e.getMessage());
        }
        PreparedStatement updateTable = null;
        try {
        	int indexOfSlash1 = sFlowUniqueID.lastIndexOf('/');
        	int indexOfSlash2 = sFlowUniqueID.substring(0,indexOfSlash1).lastIndexOf('/');
        	String token = sFlowUniqueID.substring(indexOfSlash2+1,indexOfSlash1);
			updateTable= con.prepareStatement(sqlUpdate);
        	updateTable.setInt(1, Job.JobStatus.STARTED.getCode());
        	updateTable.setString(2, sFlowUniqueID);
			updateTable.setString(3, token);
			//updateTable.setTimestamp(4, new Timestamp(new Date().getTime()));
			LOGGER.info("UPDATE: " + sqlUpdate);
			LOGGER.info("statusCode: " + Job.JobStatus.STARTED.getCode());
			LOGGER.info("sFlowUniqueID: "+ sFlowUniqueID);
			LOGGER.info("token: "+token);
			int result =updateTable.executeUpdate();
			if(result!=1){
				LOGGER.severe("probeFlowStart: update returned: "+ result);	
			}
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	
	}
	

	/** The flow stopped executing.
	 * 
	 * @param sFlowUniqueID The unique execution flow ID
	 * @param ts The time stamp
	 */
	public void probeFlowFinish(String sFlowUniqueID, Date ts){
		LOGGER.info("Flow Finished " + ts.toString()+ " " +sFlowUniqueID);
		LOGGER.info("Flow Finished: JobStatus.FINISHED "+ Job.JobStatus.FINISHED.getCode());
		databaseQuery(sFlowUniqueID, Job.JobStatus.FINISHED.getCode());
		int jobId = getJobIdFromFlowUniqueID(sFlowUniqueID);
		System.out.println("Found job : "+ jobId);
		if(jobId==-1){
			LOGGER.severe("Job "+ sFlowUniqueID + " not found");
			return;
		}
		//FIND THE RESULTS IN THE RESULT DIRECTORY and store them
		try {
		String dirLoc=ArtifactManagerImpl.getInstance().getResultLocationForJob(sFlowUniqueID);
		File dir = new File(dirLoc);
		savejobResult(jobId,dir.getAbsolutePath(),"dir");
		FileListFilter filter = new FileListFilter();
		File list[]=dir.listFiles(filter);
		
			for(int i=0;i<list.length;i++){
				savejobResult(jobId,list[i].getAbsolutePath(),"file");
			}
			
			DirListFilter dir_filter = new DirListFilter();
		    list=dir.listFiles(dir_filter);
			for(int i=0;i<list.length;i++){
				savejobResult(jobId,list[i].getAbsolutePath(),"dir");
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	
	/** The flow aborted the execution.
	 * 
	 * @param sFlowUniqueID The unique execution flow ID
	 * @param ts The time stamp
	 */
	public void probeFlowAbort(String sFlowUniqueID, Date ts,String message){
		System.out.println("Flow Aborted " + ts.toString()+ " " +sFlowUniqueID);
		databaseQuery(sFlowUniqueID, Job.JobStatus.ABORTED.getCode());
		int jobId = getJobIdFromFlowUniqueID(sFlowUniqueID);
		if(jobId==-1){
			return;
		}
		try {
			String dirLoc=ArtifactManagerImpl.getInstance().getResultLocationForJob(sFlowUniqueID);
			File dir = new File(dirLoc);
			savejobResult(jobId,dir.getAbsolutePath(),"dir");
			FileListFilter filter = new FileListFilter();
			File list[]=dir.listFiles(filter);
				for(int i=0;i<list.length;i++){
					savejobResult(jobId,list[i].getAbsolutePath(),"file");
				}
			
				DirListFilter dir_filter = new DirListFilter();
				    list=dir.listFiles(dir_filter);
					for(int i=0;i<list.length;i++){
						savejobResult(jobId,list[i].getAbsolutePath(),"dir");
					}
				
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

	/** The executable component finished initialization.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the initialization
	 * @param ts The time stamp
	 * @param bSerializeState The wrapped component is serialized
	 */
	public void probeExecutableComponentInitialized(String sECID, Object owc, 
			Date ts, boolean bSerializeState){
			LOGGER.info("Initialized "+ sECID);
		
	}

	/** The executable component requested execution abortion.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the initialization
	 * @param ts The time stamp
	 * @param bSerializeState The wrapped component is serialized
	 */
	public void probeExecutableComponentAbort(String sECID, Object owc, 
			Date ts, boolean bSerializeState){
		
	}

	/** The executable component finished disposing itself.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the disposing call
	 * @param ts The time stamp
	 * @param bSerializeState The wrapped component is serialized
	 */
	public void probeExecutableComponentDisposed(String sECID, Object owc, 
			Date ts, boolean bSerializeState){
		
	}

	/** The executable component pushed a piece of data.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the disposing call
	 * @param odata The data being pushed
	 * @param ts The time stamp
	 * @param portName 
	 * @param bSerializeState The wrapped component is serialized
	 * @param bSerializedData The data provided has been serialized
	 */
	public void probeExecutableComponentPushData(String sECID, Object owc, 
			Object odata, Date ts, String portName, boolean bSerializeState,
			boolean bSerializedData){
		
	}

	/** The executable component pulled a piece of data.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the disposing call
	 * @param odata The data being pulled
	 * @param ts The time stamp
	 */
	public void probeExecutableComponentPullData(String sECID, Object owc, 
			Object odata, Date ts,String portName, boolean bSerializeState, 
			boolean bSerializedData){
		
	}
	
	/** The executable component was fired.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the disposing call
	 * @param ts The time stamp
	 * @param bSerializeState The wrapped component is serialized
	 */
	public void probeExecutableComponentFired(String sECID, Object owc, 
			Date ts, boolean bSerializeState){
		
	}

	/** The executable component was fired.
	 * 
	 * @param sECID The unique executable component ID
	 * @param owc The wrapped component done with the disposing call
	 * @param ts The time stamp
	 * @param bSerializeState The wrapped component is serialized
	 */
	public void probeExecutableComponentCoolingDown(String sECID, Object owc, 
			Date ts, boolean bSerializeState){
		
	}

	/** The executable component requested a property value.
	 * 
	 * @param sECID The unique executable component ID
	 * @param sPropertyName The requested property
	 * @param sPropertyValue The property value
	 * @param ts The time stamp
	 */
	public void probeExecutableComponentGetProperty(String sECID, 
			String sPropertyName, String sPropertyValue, Date ts){
		
	}
	
	/*talks to the jobResult table*/
	private void savejobResult(int jobId, String url, String resultType) {
		SqlPersistence mdata=JobResult.class.getAnnotation(SqlPersistence.class);
		String sqlStore=mdata.store();
	
		Connection con = null;
        try {
               con = dataSource.getConnection();
        }catch(SQLException e) {
               System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
        }
        PreparedStatement insertTable = null;
        try {
        	insertTable= con.prepareStatement(sqlStore);
        	insertTable.setString(1, resultType);
        	insertTable.setString(2, url);
        	insertTable.setInt(3, jobId);
			insertTable.execute();
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	private int getJobIdFromFlowUniqueID(String sFlowUniqueID) {
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sql=mdata.queryByName();
		if(sql.equals("[unassigned]")){
			LOGGER.severe("Error Job class does not have the query specified for queryByName\n");
			return -1;
		}
		 Connection con = null;
        try {
               con = dataSource.getConnection();
        }catch(SQLException e) {
               System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
        }
        int jobId=-1;
        PreparedStatement getId = null;
        try {
        	getId= con.prepareStatement(sql);
        	getId.setString(1, sFlowUniqueID);
        	ResultSet results =getId.executeQuery();
        	
        	while (results.next()){
        		jobId=results.getInt("id");
        	}
        	
			if(jobId==-1){
				LOGGER.severe("could not get job id with execution instance id  "+ sFlowUniqueID +  " - " + sFlowUniqueID);	
			}
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return jobId;
	}

	/*talks to the job table*/
	private void databaseQuery(String sFlowUniqueID, int status) {
		SqlPersistence mdata=Job.class.getAnnotation(SqlPersistence.class);
		String sqlFinish =mdata.finish();
		if(sqlFinish.equals("[unassigned]")){
			System.out.println("Ignoring sql Update for Job.class "+ sqlFinish);
			return;
		}
		 Connection con = null;
        try {
               con = dataSource.getConnection();
        }catch(SQLException e) {
               System.out.println("Error getting connection from the Job dataSource " + e.getMessage());
        }
        PreparedStatement updateTable = null;
        try {
        	updateTable= con.prepareStatement(sqlFinish);
        	updateTable.setInt(1, status);
        	updateTable.setString(2, sFlowUniqueID);
			int result =updateTable.executeUpdate();
			if(result!=1){
				LOGGER.info("probeFlowStart: update returned: "+ result + "  flow unique ID: " + sFlowUniqueID);	
			}
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
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

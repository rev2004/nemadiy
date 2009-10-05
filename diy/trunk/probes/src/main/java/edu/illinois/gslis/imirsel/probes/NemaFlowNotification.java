package edu.illinois.gslis.imirsel.probes;

import java.util.Date;

import javax.sql.DataSource;

import org.meandre.core.engine.Probe;

import edu.illinois.gslis.imirsel.util.JndiHelper;

/** This FlowNotification is used by the Meandre Server to persist the 
 * notifications to the database.
 * 
 * @author Amit Kumar
 * @date-created 09/01/2009
 *
 */
public class NemaFlowNotification implements Probe {
	
	
	private DataSource dataSource=null;
	

	/** Invoked when the probe object get instantiated.
	 * 
	 */
	public void initialize (){
	// Initialize the log4 logger
	/*	System.out.println("Probe Initialized");
	     try {
			dataSource= JndiHelper.getFlowResultDataSource();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/                                                        
    }
	
	/** Invoked when the probe object has finished its live cycle.
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
		System.out.println("$$$$$ Flow Started " + ts.toString()+ " " +sFlowUniqueID + "  " + weburl);
	}
	
	/** The flow stopped executing.
	 * 
	 * @param sFlowUniqueID The unique execution flow ID
	 * @param ts The time stamp
	 */
	public void probeFlowFinish(String sFlowUniqueID, Date ts){
		System.out.println("Flow Finished " + ts.toString()+ " " +sFlowUniqueID);
	}
	
	/** The flow aborted the execution.
	 * 
	 * @param sFlowUniqueID The unique execution flow ID
	 * @param ts The time stamp
	 */
	public void probeFlowAbort(String sFlowUniqueID, Date ts,String message){
		
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
		System.out.println("Initialized "+ sECID);
		
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

}

package org.imirsel.nema.flowservice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.model.Component;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.Property;


/**
 * Provides operations for executing {@link Flow}s and monitoring {@link Job}s.
 *
 * @author shirk
 * @author kumaramit01
 * @since 0.4.0
 */
public interface FlowService {

   //~ Methods -----------------------------------------------------------------

	/**
	 * Kill a running {@link Job}.
	 * 
	 * @param jobId Unique ID of the {@link Job} to kill.
	 * @throws IllegalStateException if the {@link Job} is not running.
	 * @since 0.4.0
	 */
   public void abortJob(long jobId) throws IllegalStateException;

	/**
	 * Delete a {@link Job}, the flow instance that was executed, 
	 * and the results the {@link Job} produced.
	 * 
	 * @param jobId Unique ID of the {@link Job} to delete.
	 * @throws IllegalStateException if the {@link Job} is not finished.
	 * @since 0.4.0
	 */
   public void deleteJob(long jobId) throws IllegalStateException;

	/**
	 * Execute a {@link Job} using the specified {@link Flow} instance on 
	 * behalf of the specified user.
	 * 
	 * @param token A unique identifier for the {@link Job}.
	 * @param name User supplied name for the job.
	 * @param description User supplied description for the job.
	 * @param flowInstanceId Unique ID of the {@link Flow} instance to execute.
	 * @param userId Unique ID of the user on whose behalf the {@link Job} 
	 * will be executed.
	 * @param userEmail Email address for the user where notifications about 
	 * {@link Job} status changes should be sent.
	 * @return Job
	 * @since 0.4.0
	 */
   public Job executeJob(
      String token, String name, String description, long flowInstanceId,
      long userId,
      String userEmail);

	/**
	 * Return all {@link Flow}s that are templates. Template {@link Flow}s are
	 * never changed.
	 * 
	 * @return All {@link Flow}s that are templates.
	 * @since 0.4.0
	 */
   public Set<Flow> getFlowTemplates();

	/**
	 * Return the details of a job with the specified ID.
	 * 
	 * @param jobId The ID of the job to get details for.
	 * @return Job
	 */
   public Job getJob(long jobId);

	/**
	 * Return all {@link Job}s owned by the specified user.
	 * 
	 * @param userId Unique ID of the user whose {@link Job}s will be returned.
	 * @return List<Job> List of jobs
	 * @since 0.4.0
	 */
   public List<Job> getUserJobs(long userId);

	/**
	 * Return {@link Notification}s that have been sent to the specified user.
	 * 
	 * @param userId Unique ID of the user for whom {@link Notification}s 
	 * should be returned.
	 * @return List<Notification> List of notifications
	 * @since 0.4.0
	 */
   public List<Notification> getUserNotifications(long userId);

	/**
	 * Persist the specified {@link Flow} instance.
	 * 
	 * @param instance The {@link Flow} instance that should be stored.
	 * @return The flow instance ID.
	 * @since 0.4.0
	 */
   public Long storeFlowInstance(Flow instance);
   
   /**
    * Returns {@link Flow} with the id
    * @param flowId flowId of the flow to be returned
    * @return Flow
    * @since 0.4.0
    */
   public Flow getFlow(long flowId);
   
   
   /**
    * Returns server list for the server
    * @return List<String> List of Meandre server string 
    * @since 0.5.1
    */
   public List<String> getMeandreServerList();
   
   
   /**
    * Returns a Map of the MeandreServerProxyConfig as key and the MeandreServerProxyStatus as value
    * @return Map of {@link org.imirsel.nema.flowservice.config.MeandreServerProxyConfig} as key {@link MeandreServerProxyStatus} as value
    * @since 0.5.1
    */
   public Map<String,MeandreServerProxyStatus> getMeandreServerProxyStatus();
   
   /**
    * Returns the MeandreServerProxyStatus for the Server
    * @param host The meandre servers address
    * @param port The meandre servers port
    * @return MeandreServerProxyStatus The Server's runtime status
    * @since 0.5.1
    */
   public MeandreServerProxyStatus getMeandreServerProxyStatus(String host, int port);
   
   
   /**
    * Returns the console for the executing flow 
    * @param URI The URI of the flow
    * @return console as string
    * @throws MeandreServerException 
    * @since 0.5.0
    */
   public String getConsole(String URI) throws MeandreServerException;
   
   /**
    * Creates a new flow with the parameters based on the flowURI template
    * @param paramMap HashMap of Parameter names and values.
    * @param flowURI  The URI of the flow
    * @param userId The user id who is creating this flow
    * @return URI The URI of the flow
    * @throws MeandreServerException 
    * @since 0.5.0
    */
   public String createNewFlow(HashMap<String,String> paramMap,String flowURI, long userId) throws MeandreServerException;

   /**
    * Returns the Component Data Property
    * @param component
    * @param URI The URI of the flow
    * @return Map<String, Property> The Map of component name and property.
    * @throws MeandreServerException 
    * @since 0.5.0
    */
   public Map<String, Property> getComponentPropertyDataType(Component component, String URI) throws MeandreServerException;

   /**
    * Returns the list of Component
    * @param URI The URI of the flow
    * @return List<Component> 
    * @throws MeandreServerException 
    * @since 0.5.0
    */
   public List<Component> getComponents(String URI) throws MeandreServerException;

   /** 
    * Remove the flow with the url
    * @param URI The URI of the flow
    * @return success True or False
    * @throws MeandreServerException 
    * @since 0.5.0
    */
   public boolean removeFlow(String URI) throws MeandreServerException;
		
   

}

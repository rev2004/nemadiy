package org.imirsel.nema.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.service.SubmissionManager;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;


/**
 * 
 * @author kumaramit01
 * @since 0.4.0
 */
public class JobController extends MultiActionController {

	static private Log logger=LogFactory.getLog(JobController.class);
	private UserManager userManager = null;
	private FlowService flowService = null;
    private SubmissionManager submissionManager = null;
	private RepositoryClientConnectionPool repositoryClientConnectionPool;

    

    public void setSubmissionManager(SubmissionManager submissionManager) {
		this.submissionManager = submissionManager;
	}

    public FlowService getFlowService() {
		return flowService;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @return the repositoryClientConnectionPool
	 */
	public RepositoryClientConnectionPool getRepositoryClientConnectionPool() {
		return repositoryClientConnectionPool;
	}


	/**
	 * @param repositoryClientConnectionPool the repositoryClientConnectionPool to set
	 */
	public void setRepositoryClientConnectionPool(
			RepositoryClientConnectionPool repositoryClientConnectionPool) {
		this.repositoryClientConnectionPool = repositoryClientConnectionPool;
	}


	/**
	 * @return the userManager
	 */
	public UserManager getUserManager() {
		return userManager;
	}


	/**
	 * @return the submissionManager
	 */
	public SubmissionManager getSubmissionManager() {
		return submissionManager;
	}


	/**Returns the submissions for the current user
	 * 
	 * @param req
	 * @param res
	 * @return submission/submissionList.jsp
	 */
	public ModelAndView getSubmissions(HttpServletRequest req,	HttpServletResponse res){
		
		ModelAndView mav;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav =new ModelAndView("submission/submissionList");
		}
		User user=this.userManager.getCurrentUser();
		List<Submission> submissions=this.submissionManager.getSubmissions(user);
		logger.info("Submissions are: " +submissions);
		if(submissions!=null){
			logger.info("submission size is: " + submissions.size());
		}
		mav.addObject(Constants.SUBMISSIONLIST, submissions);
		return mav;
	}
	
	
	/**Returns the submissions for all the users
	 * 
	 * @param req
	 * @param res
	 * @return submission/submissionListAll
	 */
	public ModelAndView getAllSubmissions(HttpServletRequest req,	HttpServletResponse res){
		String userIdString = req.getParameter("userId");
		List<Submission> list=null;
		
		if(userIdString==null){
			userIdString="";
		}
		userIdString = userIdString.trim();
		
		if(userIdString.length()>0){
			list = this.submissionManager.getSubmissions(this.userManager.getUser(userIdString));
		}else{
			list=this.submissionManager.getAllSubmissions();
		}
		List<User> userList=this.userManager.getUsers(new User(null));
		ModelAndView  mav = new ModelAndView("submission/submissionListAll");
		mav.addObject(Constants.SUBMISSIONLIST, list);
		mav.addObject(Constants.USER_LIST, userList);
		mav.addObject(Constants.USER_KEY, userIdString);
		System.out.println("users.... " + userList);
		System.out.println("number of users: " + userList.size());
		return mav;
	}
	
	
	


	/**selects the job for submission
	 * 
	 * @param req
	 * @param res
	 * @return job/job The Job detail
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public ModelAndView selectJobForSubmission(HttpServletRequest req,	HttpServletResponse res) throws SQLException, IOException{
		String _jobId = req.getParameter("jobId");
		Long jobId = Long.parseLong(_jobId);
		Job job=this.flowService.getJob(jobId);
		String username = this.userManager.getCurrentUser().getUsername();
		
		String submissionName = username+" "+ job.getName();
		Flow instanceOfFlow=job.getFlow().getInstanceOf();
		logger.info("getting job's flow "+ instanceOfFlow);
		User user=this.userManager.getCurrentUser();
		String type =  instanceOfFlow.getTypeName();
		Submission submission = new Submission();
		submission.setDateCreated(new Date());
		submission.setUser(user);
		submission.setJobId(jobId);
		submission.setType(type);
		submission.setName(submissionName);
		logger.info("Creating a submission with job:" + jobId + " and type: " + type);
		Submission thisSubmission=this.submissionManager.getSubmission(user,type);
		logger.info("Found a submission : "+ thisSubmission);
		boolean success=false;
		if(thisSubmission==null){
			Submission s=this.submissionManager.saveSubmission(submission);
			if (s!=null)  {
				logger.info("submission not found -adding new submission the id is " + s.getId());
				success=true;
			}
		}else{
			// remove the existing submission and add the new submission
			this.submissionManager.removeSubmission(thisSubmission.getId());
			Submission s=this.submissionManager.saveSubmission(submission);
			if (s!=null) {
				success=true;
				logger.info("submission found: removing it " +thisSubmission.getId()+" and adding new submission id is " + s.getId());
			}
		
		}
		
		
		Set<JobResult> results=job.getResults();
		
		String dataSetResultUrl = null;
		String token = null;
		String path = null;
		for(JobResult result:results){
			if(result.getUrl().endsWith("results") && result.getResultType().equals("dir")){
				path = result.getUrl();
				String[] list=path.split("/");
				int count = list.length;
				int tokenLoc = count-2;
				for(String s:list){System.out.println("ssss---> "+ s);}
				token = list[tokenLoc];
				break;
			}
		}


		dataSetResultUrl = "http://nema.lis.uiuc.edu/nema_out/"+ token+"/"+"datasetid.txt";
		
		URL url = new URL(dataSetResultUrl);
		BufferedReader in = new BufferedReader(	new InputStreamReader(url.openStream()));
	
		String _dataset_id_str="-1";
		
		String line=null;
		while ((line = in.readLine()) != null){
			_dataset_id_str=line;
			System.out.println("Data Set id read is: "+_dataset_id_str);
		}
		
		
		Integer dataset_id = -1;
		try{
		dataset_id= Integer.parseInt(_dataset_id_str);
		}catch(Exception ex){
			System.out.println("ERROR: could not convert  " + _dataset_id_str + " to an integer");
		}finally{
			in.close();
		}
		
		if(dataset_id!=-1){
			RepositoryClientInterface client=this.getRepositoryClient();
			try{
			    client.publishResultForDataset(dataset_id, username,  submissionName ,path);       
			}finally{
				this.repositoryClientConnectionPool.returnToPool(client);
			} 
        }
		
		
		
		ModelAndView mav= new  ModelAndView("job/job");
		mav.addObject("jobForSubmission",success);
		mav.addObject(Constants.JOB, job);
		logger.debug("change job "+job.getId()+"with "+job.getResults()+" to submission: "+success);
		return mav;
	}

	
	/**Returns the list of submission
	 * 
	 * @param req
	 * @param res
	 * @return submission/submission
	 */
	public ModelAndView submissionDetail(HttpServletRequest req,
			HttpServletResponse res) {
		String _submissionId = req.getParameter("id");
		long submissionId = Long.parseLong(_submissionId);
		Submission submission=this.submissionManager.getSubmission(submissionId);
		
		if(submission==null){
			// do something
		}
		
		Job job = flowService.getJob(submission.getJobId());
		ModelAndView mav= new ModelAndView("submission/submission");
		
		mav.addObject(Constants.JOB, job);
		mav.addObject(Constants.SUBMISSION, submission);
		mav.addObject(Constants.RESULTSET,job.getResults());
		
		
		
		
		DisplayResultSet resultSet=new DisplayResultSet(job.getResults());
		mav.addObject("resultSet", resultSet);
		return mav;
	}
	

	/**Returns the job detail of the job with id
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getJobDetail(HttpServletRequest req,
			HttpServletResponse res) {
		String hostName="http://" + req.getServerName() + ":"	+ req.getServerPort() ;
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("Show Job here: " + job.getName());
		System.out.println("STATUS CODE: " + job.getStatusCode());
		System.out.println("STATUS VAL: " + job.getJobStatus());
		System.out.println("NUMBER of RESULTS: "+ job.getResults().size());
		
		for(JobResult result:job.getResults()){
			logger.debug("RESULT: " + result.getUrl() + "  "+ result.getId());
			result.setUrl(processUrl(result.getUrl(),hostName));
			
		}
		ModelAndView mav;
		String uri=req.getRequestURI();
		if (uri.substring(uri.length()-4).equalsIgnoreCase ("json")){
			 mav=new ModelAndView("jsonView");
		}else {
			 mav= new ModelAndView("job/job");
		}
		mav.addObject(Constants.JOB, job);
		
		logger.debug("start to render displayed results");
		DisplayResultSet resultSet=new DisplayResultSet(job.getResults());
		mav.addObject("resultSet", resultSet);
		return mav;
	
	}



	/**Adds a job to the submission list
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws SQLException
	 */
	public ModelAndView submissionAction(HttpServletRequest req,
			HttpServletResponse res) throws SQLException {
		String _submissionId = req.getParameter("id");
		long submissionId = Long.parseLong(_submissionId);
		Submission submission=this.submissionManager.getSubmission(submissionId);
		String username= this.userManager.getCurrentUser().getUsername();
		String submissionName = submission.getName();
		
		RepositoryClientInterface rci = null;
		
		try{
			rci=this.getRepositoryClient();
			List<NemaPublishedResult> resultList=rci.getPublishedResultsForDataset(username);
			if(!resultList.isEmpty()){
				int id =-1;
				for(NemaPublishedResult pr:resultList){
					if(pr.getName().equals(submissionName)){
						id = pr.getId();
					}
				}
				if(id!=-1){
					rci.deletePublishedResult(id);
				}
			}
		}finally{
		this.getRepositoryClientConnectionPool().returnToPool(rci);
		
		}
		this.submissionManager.removeSubmission(submissionId);
		return new ModelAndView(new RedirectView("JobManager.getSubmissions",true));
	}

	
	/**Allows users to delete or abort a job
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws MeandreServerException
	 */
	public ModelAndView doJobAction(HttpServletRequest req,HttpServletResponse res) throws MeandreServerException{
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);

		Job job = flowService.getJob(jobId);
		String _submitType = req.getParameter("submit");

		if (_submitType.equals("Abort This Job")) {
			flowService.abortJob(job.getId());
		} else if (_submitType.equals("Delete This Job")) {
			flowService.deleteJob(job.getId());
			logger.info("deleting flow: "+ job.getFlow().getUri());
			flowService.removeFlow(job.getFlow().getUri());
		}
		//, Constants.JOB, job
		return new ModelAndView(new RedirectView("JobManager.getUserJobs",true));
	}



	/**Returns the list of jobs for the current user
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getUserJobs(HttpServletRequest req,
			HttpServletResponse res) {
		ModelAndView mav;
		String uri = (req!=null)?req.getRequestURI():""; 
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav =new ModelAndView("job/jobList");
		}
		User user = userManager.getCurrentUser();
		logger.debug("getting user " + user.getUsername());
    	long userId = user.getId();
		logger.debug("start to list the jobs of   " + user.getUsername());
		List<Job> jobs = flowService.getUserJobs(userId);
		mav.addObject(Constants.JOBLIST,jobs);
		return mav;
	}

	
	/**Returns list of notifications for the user
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView getNotification(HttpServletRequest req, HttpServletResponse res){
		User user=this.userManager.getCurrentUser();
		List<Notification> notifications=this.flowService.getUserNotifications(user.getId());
		ModelAndView mav=new ModelAndView("jsonView");
		mav.addObject(notifications);
		return mav;
	}
	
	
	
	/**Returns the console
	 * 
	 * @param request requires jobId http request parameter
	 * @param response
	 * @return console The String output
	 * @throws TransmissionException 
	 */
	public ModelAndView getConsole(HttpServletRequest request, HttpServletResponse response) throws MeandreServerException{
		String _jobId = request.getParameter("jobId");
		long jobId = Long.parseLong(_jobId);
		
		//We need either change the parameter to job or we need to add a new method in flowService.
		Job job=this.flowService.getJob(jobId);
		
		String text=this.flowService.getConsole(job);
		try {
			response.setContentType("text/plain");
			response.getOutputStream().println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	

	
	private String processUrl(String url,String hostName) {
		String identifier="published_resources/nema";
		int index = url.indexOf(identifier);
		String resultFolder = url.substring(index+identifier.length());
		return hostName+"/nema_out"+resultFolder;
		
	}
	private RepositoryClientInterface getRepositoryClient() {
		return repositoryClientConnectionPool.getFromPool();
	}
	
	public ModelAndView test(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("melodyResultIntro1","a",100);
	}
	
}

package org.imirsel.nema.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.flowservice.MeandreServerException;
import org.imirsel.nema.flowservice.config.MeandreServerProxyConfig;
import org.imirsel.nema.flowservice.config.MeandreServerProxyStatus;
import org.imirsel.nema.meandre.util.ConsoleUtil;
import org.imirsel.nema.meandre.util.MeandreConsoleDao;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.NemaPublishedResult;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;
import org.imirsel.nema.service.SubmissionManager;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.imirsel.nema.webapp.json.ConverterToList;
import org.imirsel.nema.webapp.json.ConverterToMap;
import org.imirsel.nema.webapp.json.ConverterToMapJob;
import org.imirsel.nema.webapp.json.ConverterToMapJobLong;
import org.imirsel.nema.webapp.json.ConverterToMapServer;
import org.imirsel.nema.webapp.json.ConverterToMapServerConfig;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

/**
 * 
 * @author kumaramit01
 * @since 0.4.0
 */
public class JobController extends MultiActionController {

	static private Log logger = LogFactory.getLog(JobController.class);
	private UserManager userManager = null;
	private FlowService flowService = null;
	private SubmissionManager submissionManager = null;
	private RepositoryClientConnectionPool repositoryClientConnectionPool;
	private MeandreConsoleDao meandreConsoleDao;
	private ConsoleUtil consoleUtil;

	public void setConsoleUtil(ConsoleUtil consoleUtil) {
		this.consoleUtil = consoleUtil;
	}

	public void setMeandreConsoleDao(MeandreConsoleDao meandreConsoleDao) {
		this.meandreConsoleDao = meandreConsoleDao;
	}

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
	 * @param repositoryClientConnectionPool
	 *            the repositoryClientConnectionPool to set
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

	/**
	 * Returns the submissions for the current user
	 * 
	 * @param req
	 * @param res
	 * @return submission/submissionList.jsp
	 */
	public ModelAndView getSubmissions(HttpServletRequest req,
			HttpServletResponse res) {

		ModelAndView mav;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav = new ModelAndView("submission/submissionList");
		}
		User user = this.userManager.getCurrentUser();
		List<Submission> submissions = this.submissionManager
				.getSubmissions(user);
		logger.info("Submissions are: " + submissions);
		if (submissions != null) {
			logger.info("submission size is: " + submissions.size());
		}
		mav.addObject(Constants.SUBMISSIONLIST, submissions);
		return mav;
	}

	/**
	 * Returns the submissions for all the users
	 * 
	 * @param req
	 * @param res
	 * @return submission/submissionListAll
	 */
	public ModelAndView getAllSubmissions(HttpServletRequest req,
			HttpServletResponse res) {
		String userIdString = req.getParameter("userId");
		List<Submission> list = null;

		if (userIdString == null) {
			userIdString = "";
		}
		userIdString = userIdString.trim();

		if (userIdString.length() > 0) {
			list = this.submissionManager.getSubmissions(this.userManager
					.getUser(userIdString));
		} else {
			list = this.submissionManager.getAllSubmissions();
		}
		List<User> userList = this.userManager.getUsers(new User(null));
		ModelAndView mav = new ModelAndView("submission/submissionListAll");
		mav.addObject(Constants.SUBMISSIONLIST, list);
		mav.addObject(Constants.USER_LIST, userList);
		mav.addObject(Constants.USER_KEY, userIdString);
		System.out.println("users.... " + userList);
		System.out.println("number of users: " + userList.size());
		return mav;
	}

	/**
	 * selects the job for submission
	 * 
	 * @param req
	 * @param res
	 * @return job/job The Job detail
	 * @throws SQLException
	 * @throws IOException
	 */
	public ModelAndView selectJobForSubmission(HttpServletRequest req,
			HttpServletResponse res) throws SQLException, IOException {
		String _jobId = req.getParameter("jobId");
		Long jobId = Long.parseLong(_jobId);
		Job job = this.flowService.getJob(jobId);
		String username = this.userManager.getCurrentUser().getUsername();

		String submissionName = username + " " + job.getName();
		Flow instanceOfFlow = job.getFlow().getInstanceOf();
		logger.info("getting job's flow " + instanceOfFlow);
		User user = this.userManager.getCurrentUser();
		String type = instanceOfFlow.getTypeName();
		Submission submission = new Submission();
		submission.setDateCreated(new Date());
		submission.setUser(user);
		submission.setJobId(jobId);
		submission.setType(type);
		submission.setName(submissionName);
		logger.info("Creating a submission with job:" + jobId + " and type: "
				+ type);
		Submission thisSubmission = this.submissionManager.getSubmission(user,
				type);
		logger.info("Found a submission : " + thisSubmission);
		boolean success = false;
		if (thisSubmission == null) {
			Submission s = this.submissionManager.saveSubmission(submission);
			if (s != null) {
				logger
						.info("submission not found -adding new submission the id is "
								+ s.getId());
				success = true;
			}
		} else {
			// remove the existing submission and add the new submission
			this.submissionManager.removeSubmission(thisSubmission.getId());
			Submission s = this.submissionManager.saveSubmission(submission);
			if (s != null) {
				success = true;
				logger.info("submission found: removing it "
						+ thisSubmission.getId()
						+ " and adding new submission id is " + s.getId());
			}

		}

		Set<JobResult> results = job.getResults();

		String dataSetResultUrl = null;
		String token = null;
		String path = null;
		for (JobResult result : results) {
			if (result.getUrl().endsWith("results")
					&& result.getResultType().equals("dir")) {
				path = result.getUrl();
				String[] list = path.split("/");
				int count = list.length;
				int tokenLoc = count - 2;
				for (String s : list) {
					System.out.println("ssss---> " + s);
				}
				token = list[tokenLoc];
				break;
			}
		}

		dataSetResultUrl = "http://nema.lis.uiuc.edu/nema_out/" + token + "/"
				+ "datasetid.txt";

		URL url = new URL(dataSetResultUrl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url
				.openStream()));

		String _dataset_id_str = "-1";

		String line = null;
		while ((line = in.readLine()) != null) {
			_dataset_id_str = line;
			System.out.println("Data Set id read is: " + _dataset_id_str);
		}

		Integer dataset_id = -1;
		try {
			dataset_id = Integer.parseInt(_dataset_id_str);
		} catch (Exception ex) {
			System.out.println("ERROR: could not convert  " + _dataset_id_str
					+ " to an integer");
		} finally {
			in.close();
		}
		/*
		 * TODO COMMENT for broken API if (dataset_id != -1) {
		 * RepositoryClientInterface client = this.getRepositoryClient(); try {
		 * client.publishResultForDataset(dataset_id, username, submissionName,
		 * path); } finally {
		 * this.repositoryClientConnectionPool.returnToPool(client); } }
		 */
		ModelAndView mav = new ModelAndView("job/job");
		mav.addObject("jobForSubmission", success);
		mav.addObject(Constants.JOB, job);
		logger.debug("change job " + job.getId() + "with " + job.getResults()
				+ " to submission: " + success);
		return mav;
	}

	/**
	 * Returns the list of submission
	 * 
	 * @param req
	 * @param res
	 * @return submission/submission
	 */
	public ModelAndView submissionDetail(HttpServletRequest req,
			HttpServletResponse res) {
		String _submissionId = req.getParameter("id");
		long submissionId = Long.parseLong(_submissionId);
		Submission submission = this.submissionManager
				.getSubmission(submissionId);

		if (submission == null) {
			// do something
		}

		Job job = flowService.getJob(submission.getJobId());
		ModelAndView mav = new ModelAndView("submission/submission");

		mav.addObject(Constants.JOB, job);
		mav.addObject(Constants.SUBMISSION, submission);
		mav.addObject(Constants.RESULTSET, job.getResults());

		DisplayResultSet resultSet = new DisplayResultSet(job.getResults());
		mav.addObject("resultSet", resultSet);
		return mav;
	}

	/**
	 * Returns the job detail of the job with id
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getJobDetail(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		String hostName = "http://" + req.getServerName() + ":"
				+ req.getServerPort();
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		for (JobResult result : job.getResults()) {
			result.setUrl(processUrl(result.getUrl(), hostName));
		}

		logger.debug("start to render displayed results");
		DisplayResultSet resultSet = new DisplayResultSet(job.getResults());

		ModelAndView mav = null;
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
			ConverterToMap<Job> converter = new ConverterToMapJobLong();
			mav.addObject(Constants.JOB, converter.convertToMap(job));
			mav.addObject("resultSet", resultSet);

		} else {
			mav = new ModelAndView("job/job");
			mav.addObject(Constants.JOB, job);
			mav.addObject("resultSet", resultSet);
			
			MeandreServerProxyConfig head = flowService.getHeadConfig();
			mav.addObject("head",head);
		}

		return mav;

	}

	/**
	 * Adds a job to the submission list
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
		Submission submission = this.submissionManager
				.getSubmission(submissionId);
		String username = this.userManager.getCurrentUser().getUsername();
		String submissionName = submission.getName();

		RepositoryClientInterface rci = null;
		/*
		 * TODO COMMENT for broken API try { rci = this.getRepositoryClient();
		 * List<NemaPublishedResult> resultList = rci
		 * .getPublishedResultsForDataset(username); if (!resultList.isEmpty())
		 * { int id = -1; for (NemaPublishedResult pr : resultList) { if
		 * (pr.getName().equals(submissionName)) { id = pr.getId(); } } if (id
		 * != -1) { rci.deletePublishedResult(id); } } } finally {
		 * this.getRepositoryClientConnectionPool().returnToPool(rci);
		 * 
		 * } this.submissionManager.removeSubmission(submissionId);
		 */
		return new ModelAndView(new RedirectView("JobManager.getSubmissions",
				true));
	}

	/**
	 * Allows users to delete or abort a job
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws MeandreServerException
	 */
	public ModelAndView doJobAction(HttpServletRequest req,
			HttpServletResponse res) throws MeandreServerException {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);

		Job job = flowService.getJob(jobId);
		String _submitType = req.getParameter("submit");

		if (_submitType.equals("Abort This Job")) {
			flowService.abortJob(job.getId());
		} else if (_submitType.equals("Delete This Job")) {
			flowService.deleteJob(job.getId());
			logger.info("deleting flow: " + job.getFlow().getUri());
			flowService.removeFlow(job.getFlow().getUri());
			if (job.isDone()) {
				consoleUtil.deleteConsole(job);
			}
		}
		// , Constants.JOB, job
		return new ModelAndView(
				new RedirectView("JobManager.getUserJobs", true));
	}

	/**
	 * Returns the list of jobs for the current user
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public ModelAndView getUserJobs(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		User user = userManager.getCurrentUser();
		logger.debug("getting user " + user.getUsername());
		long userId = user.getId();
		logger.debug("start to list the jobs of   " + user.getUsername());
		List<Job> jobs = flowService.getUserJobs(userId);

		ModelAndView mav = null;
		String uri = (req != null) ? req.getRequestURI() : "";
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			// XStream xstream = provideXstream();
			// // XStream xstream = new XStream();//new
			// JettisonMappedXmlDriver());
			// // xstream.registerLocalConverter(Job.class,"submitTimestamp",new
			// // LenientDateConverter());
			// // xstream.registerLocalConverter(Job.class,"startTimestamp",new
			// // LenientDateConverter());
			// // xstream.registerConverter(new
			// // LenientDateConverter(),XStream.PRIORITY_VERY_HIGH);
			// xstream.addDefaultImplementation(Date.class,
			// java.sql.Timestamp.class);
			// xstream.setMode(XStream.NO_REFERENCES);
			// // xstream.omitField(Job.class,"flow" );
			// // xstream.omitField(Job.class, "results");
			// xstream.alias(Constants.JOBLIST, List.class);
			// xstream.registerConverter(new ShortJobConverter(),
			// XStream.PRIORITY_VERY_HIGH);

			// fake test
			// Date date = new Date(System.currentTimeMillis());
			// Date date2 = new java.sql.Timestamp(System.currentTimeMillis());
			// Job job = new Job();
			// job.setName("test job");
			// job.setId(100L);
			// job.setFlow(new Flow());
			// job.setScheduleTimestamp(date);
			// job.setEndTimestamp(date2);
			// job.setStartTimestamp(date);
			// job.setSubmitTimestamp(date2);
			// job.setResults(null);
			// List<Job> list = new ArrayList<Job>();
			// Job job2 = jobs.get(1);
			// list.add(job);
			// list.add(job);
			// list.add(jobs.get(1));

			// res.setContentType("application/json");
			// res.getWriter().write(xstream.toXML(jobs));

			ConverterToList<Job> converter = new ConverterToList<Job>();
			mav = new ModelAndView("jsonView", Constants.JOBLIST, converter
					.convertToList(jobs, new ConverterToMapJob()));
			// mav =new ModelAndView("jsonView",Constants.JOBLIST,jobs);
		} else {
			mav = new ModelAndView("job/jobList", Constants.JOBLIST, jobs);
		}

		return mav;
	}

	/**
	 * Returns list of notifications for the user
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView getNotification(HttpServletRequest req,
			HttpServletResponse res) {
		User user = this.userManager.getCurrentUser();
		List<Notification> notifications = this.flowService
				.getUserNotifications(user.getId());
		ModelAndView mav = new ModelAndView("jsonView");

		// Testing Set
		// TODO remove when the real notification service is there
		// Notification notification = new Notification();
		// notification.setMessage("Your job XXX Failed miserably!");
		// notifications = new ArrayList<Notification>();
		// notifications.add(notification);
		// notification = new Notification();
		// notification
		// .setMessage("Server x-1-y-2-z-3 amd server fjdskfjdskfjsdljfdslkjf  both died");
		// notifications.add(notification);
		// notification = new Notification();
		// notification.setMessage("You suck badly!");
		// notifications.add(notification);

		if (System.currentTimeMillis() % 2 == 0)
			mav.addObject("notifications", notifications);
		else
			mav.addObject("notifications", null);

		return mav;
	}

	/**
	 * Returns the console
	 * 
	 * @param request
	 *            requires jobId http request parameter
	 * @param response
	 * @return console The String output
	 * @throws TransmissionException
	 */
	public ModelAndView getConsole(HttpServletRequest request,
			HttpServletResponse response) throws MeandreServerException {
		String _jobId = request.getParameter("jobId");
		long jobId = Long.parseLong(_jobId);
		Job job = this.flowService.getJob(jobId);
		response.setContentType("text/plain");

		try {
			if (job.isDone()) {
				response.getOutputStream().print(consoleUtil.findConsole(job));
			} else {
				String text = this.flowService.getConsole(job);
				response.getOutputStream().println(text);
			}
		} catch (IOException e) {
			logger.error(e, e);
		}

		return null;
	}

	/**
	 * Query for the satus of servers
	 */
	public ModelAndView getServerStatus(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		Map<MeandreServerProxyConfig, MeandreServerProxyStatus> workers = flowService
				.getWorkerStatus();
		MeandreServerProxyConfig head = flowService.getHeadConfig();
		List<Job> scheduledJobs = flowService.getScheduledJobs();

		ModelAndView mav;
		String uri = (req != null) ? req.getRequestURI() : "";
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {

			mav = new ModelAndView("jsonView");

			ConverterToList<Job> converter1 = new ConverterToList<Job>();
			mav.addObject("scheduledJobs", converter1.convertToList(
					scheduledJobs, new ConverterToMapJob()));

			ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>> converter2 = new ConverterToList<Entry<MeandreServerProxyConfig, MeandreServerProxyStatus>>();
			mav.addObject("workers", converter2.convertToList(workers
					.entrySet(), new ConverterToMapServer()));
			mav.addObject("anotherWorkers", workers);

			ConverterToMapServerConfig converter3 = new ConverterToMapServerConfig();
			mav.addObject("head", converter3.convertToMap(head));

		} else {
			mav = new ModelAndView("job/serverStatus");
			mav.addObject("workers", workers.entrySet());
			mav.addObject("head", head);
			mav.addObject("scheduledJobs", scheduledJobs);
		}

		return mav;
	}

	/**
	 * return the status of nema for the status bar, only Json interface.
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView getNemaStatus(HttpServletRequest req,
			HttpServletResponse res) {
		ModelAndView mav = new ModelAndView("jsonView");

		Map<MeandreServerProxyConfig, MeandreServerProxyStatus> workers = flowService
				.getWorkerStatus();

		List<Job> scheduledJobs = flowService.getScheduledJobs();
		int availableSlots = 0, runningNum = 0;
		for (Entry<MeandreServerProxyConfig, MeandreServerProxyStatus> entry : workers
				.entrySet()) {
			availableSlots += entry.getKey().getMaxConcurrentJobs();
			runningNum += entry.getValue().getNumRunning();
		}
		mav.addObject("load", runningNum * 1.0 / availableSlots);
		mav.addObject("jobsInQueue", scheduledJobs.size());
		return mav;
	}

	public ModelAndView dumpConsole(HttpServletRequest req,
			HttpServletResponse rep) {

		Map<Job,String> dumpResult=new HashMap<Job,String>();
		List<Job> jobs = flowService.getUserJobs(userManager.getCurrentUser()
				.getId());
		for (Job job : jobs) {
			if (job.isDone()) {
				try{
				consoleUtil.dumpConsoleToFile(job);
				dumpResult.put(job,"successfully dumped");
				}catch(IllegalArgumentException e){
					dumpResult.put(job,e.getMessage());
				}	catch(Exception e){
					dumpResult.put(job,"error in dumping");
				}
			}else{
				dumpResult.put(job,"not finished yet, no dump");
			}
		}
		
		ModelAndView mav=new ModelAndView("job/dumpResult","dumpResult",dumpResult);
		return mav;
	}

	private String processUrl(String url, String hostName) {
		String identifier = "published_resources/nema";
		int index = url.indexOf(identifier);
		String resultFolder = url.substring(index + identifier.length());
		return hostName + "/nema_out" + resultFolder;

	}

	private RepositoryClientInterface getRepositoryClient() throws SQLException {
		return repositoryClientConnectionPool.getFromPool();
	}

}

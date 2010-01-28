package org.imirsel.nema.webapp.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.meandre.client.TransmissionException;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Flow;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.JobResult;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.Submission;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.FlowMetadataService;
import org.imirsel.nema.service.SubmissionManager;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.jobs.DisplayResult;
import org.imirsel.nema.webapp.jobs.DisplayResultSet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.view.RedirectView;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

import org.imirsel.nema.model.PublishedResult;
import org.imirsel.nema.repository.RepositoryClientConnectionPool;
import org.imirsel.nema.repositoryservice.RepositoryClientInterface;

public class JobController extends MultiActionController {

	static protected Log logger = LogFactory.getLog(JobController.class);
	private UserManager userManager = null;
	private FlowService flowService = null;
	private SubmissionManager submissionManager = null;
	private FlowMetadataService flowMetadataService;
	private RepositoryClientConnectionPool repositoryClientConnectionPool;

	public void setSubmissionManager(SubmissionManager submissionManager) {
		this.submissionManager = submissionManager;
	}

	public FlowMetadataService getFlowMetadataService() {
		return flowMetadataService;
	}

	public void setFlowMetadataService(FlowMetadataService flowMetadataService) {
		this.flowMetadataService = flowMetadataService;
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

	public ModelAndView hello(HttpServletRequest req, HttpServletResponse res) {

		res.setContentType("text/xml");
		try {
			res.getWriter().write("<hello/>");
			res.flushBuffer();
		} catch (IOException e) {
			logger.error(e, e);
		}

		return null;
	}

	/**
	 * Returns the submissions for the current user
	 * 
	 * @param req
	 * @param res
	 * @return
	 */
	public ModelAndView getSubmissions(HttpServletRequest req,
			HttpServletResponse res) {
		User user = this.userManager.getCurrentUser();
		List<Submission> submissions = this.submissionManager
				.getSubmissions(user);
		logger.info("Submissions are: " + submissions);
		if (submissions != null) {
			logger.info("submission size is: " + submissions.size());
		}
		String uri = req.getRequestURI();
		ModelAndView mav;
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			mav = new ModelAndView("jsonView");
		} else {
			mav = new ModelAndView("submission/submissionList");
		}
		mav.addObject(Constants.SUBMISSIONLIST, submissions);
		return mav;
	}

	/**
	 * Returns the submissions for all the users
	 * 
	 * @param req
	 * @param res
	 * @return
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
	 * @return
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
		JobResult eresult = null;
		String token = null;
		String path = null;
		for (JobResult result : results) {
			if (result.getUrl().endsWith("results")
					&& result.getResultType().equals("dir")) {
				eresult = result;
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

		if (dataset_id != -1) {
			RepositoryClientInterface client = this.getRepositoryClient();
			try {
				client.publishResultForDataset(dataset_id, username,
						submissionName, path);
			} finally {
				this.repositoryClientConnectionPool.returnToPool(client);
			}
		}

		ModelAndView mav = new ModelAndView("job/job");
		mav.addObject("jobForSubmission", success);
		mav.addObject(Constants.JOB, job);
		logger.debug("change job " + job.getId() + "with " + job.getResults()
				+ " to submission: " + success);
		return mav;
	}

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

	public ModelAndView jobdetail(HttpServletRequest req,
			HttpServletResponse res) {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("Show Job here: " + job.getName());
		System.out.println("STATUS CODE: " + job.getStatusCode());
		System.out.println("STATUS VAL: " + job.getJobStatus());
		System.out.println("NUMBER of RESULTS: " + job.getResults().size());

		for (JobResult result : job.getResults()) {
			System.out.println("RESULT: " + result.getUrl() + "  "
					+ result.getId());
			result.setUrl(processUrl(result.getUrl()));

		}
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			ModelAndView mav = new ModelAndView("jsonView");
			mav.addObject(Constants.JOB, job);
			return mav;
		} else {
			ModelAndView mav = new ModelAndView("job/job");
			mav.addObject(Constants.JOB, job);

			logger.debug("start to render displayed results");
			DisplayResultSet resultSet = new DisplayResultSet(job.getResults());
			mav.addObject("resultSet", resultSet);
			return mav;
		}
	}

	private String processUrl(String url) {
		String identifier = "published_resources/nema";
		int index = url.indexOf(identifier);
		String resultFolder = url.substring(index + identifier.length());
		return "http://nema.lis.uiuc.edu/nema_out" + resultFolder;

	}

	public ModelAndView submissionAction(HttpServletRequest req,
			HttpServletResponse res) throws SQLException {
		String _submissionId = req.getParameter("id");
		long submissionId = Long.parseLong(_submissionId);
		Submission submission = this.submissionManager
				.getSubmission(submissionId);
		String username = this.userManager.getCurrentUser().getUsername();
		String submissionName = submission.getName();

		RepositoryClientInterface rci = null;

		try {
			rci = this.getRepositoryClient();
			List<PublishedResult> resultList = rci
					.getPublishedResultsForDataset(username);
			if (!resultList.isEmpty()) {
				int id = -1;
				for (PublishedResult pr : resultList) {
					if (pr.getName().equals(submissionName)) {
						id = pr.getId();
					}
				}
				if (id != -1) {
					rci.deletePublishedResult(id);
				}
			}
		} finally {
			this.getRepositoryClientConnectionPool().returnToPool(rci);

		}

		this.submissionManager.removeSubmission(submissionId);
		return new ModelAndView(new RedirectView("JobManager.getSubmissions",
				true));
	}

	public ModelAndView jobaction(HttpServletRequest req,
			HttpServletResponse res) throws TransmissionException {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);

		Job job = flowService.getJob(jobId);
		String _submitType = req.getParameter("submit");

		if (_submitType.equals("Abort This Job")) {
			flowService.abortJob(job.getId());
		} else if (_submitType.equals("Delete This Job")) {
			flowService.deleteJob(job.getId());
			logger.info("deleting flow: " + job.getFlow().getUrl());
			getFlowMetadataService().removeFlow(job.getFlow().getUrl());
		}
		// , Constants.JOB, job
		return new ModelAndView(
				new RedirectView("JobManager.getUserJobs", true));
	}

	public ModelAndView getuserjobs(HttpServletRequest req,
			HttpServletResponse res) {
		User user = userManager.getCurrentUser();
		logger.debug("USER IS ====> " + user);
		long userId = user.getId();
		logger.debug("start to list the jobs of   " + user);
		List<Job> jobs = flowService.getUserJobs(userId);
		/** SO FUGLY **/
		HashMap<Long, Job> jobMap = new HashMap<Long, Job>();

		for (Job job : jobs) {
			jobMap.put(job.getId(), job);
			logger.debug(job.getId() + " " + job.getName() + " "
					+ job.getJobStatus());
		}
		String uri = req.getRequestURI();
		if (uri.substring(uri.length() - 4).equalsIgnoreCase("json")) {
			ModelAndView mav = new ModelAndView("jsonView");
			mav.addObject(Constants.JOBLIST, jobMap.values());
			return mav;
		} else {
			return new ModelAndView("job/jobList", Constants.JOBLIST, jobMap
					.values());
		}
	}

	public ModelAndView getNotification(HttpServletRequest req,
			HttpServletResponse res) {
		User user = this.userManager.getCurrentUser();
		List<Notification> notifications = this.flowService
				.getUserNotifications(user.getId());
		// XStream xstream = new XStream(new JsonHierarchicalStreamDriver() {
		// public HierarchicalStreamWriter createWriter(Writer writer) {
		// return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
		// }
		// });
		XStream xstream = new XStream(new JsonHierarchicalStreamDriver());
		// XStream xstream = new XStream(new JettisonMappedXmlDriver());

		xstream.setMode(XStream.NO_REFERENCES);
		String xmlString = xstream.toXML(notifications);
		try {
			res.setContentType("application/json");
			res.getOutputStream().print(xmlString);
		} catch (IOException e) {

			logger.error(e, e);
		}
		return null;
	}

	/**
	 * Returns the console
	 * 
	 * @param req
	 * @param res
	 * @return
	 * @throws TransmissionException
	 */
	public ModelAndView getConsole(HttpServletRequest request,
			HttpServletResponse response) throws TransmissionException {
		String uri = request.getParameter("uri");
		String text = this.getFlowMetadataService().getConsole(uri);
		try {
			response.setContentType("text/plain");
			response.getOutputStream().println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void setRepositoryClientConnectionPool(
			RepositoryClientConnectionPool repositoryClientConnectionPool) {
		this.repositoryClientConnectionPool = repositoryClientConnectionPool;
	}

	public RepositoryClientConnectionPool getRepositoryClientConnectionPool() {
		return repositoryClientConnectionPool;
	}

	public RepositoryClientInterface getRepositoryClient() {
		return repositoryClientConnectionPool.getFromPool();
	}

}

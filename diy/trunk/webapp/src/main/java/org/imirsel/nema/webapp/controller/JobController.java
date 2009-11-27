package org.imirsel.nema.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.Constants;
import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.request.ExecuteJobRequest;
import org.imirsel.nema.webapp.request.GetJobRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.hp.hpl.jena.graph.query.Rewrite;

public class JobController extends MultiActionController {

	static protected Log logger=LogFactory.getLog(JobController.class);
	private UserManager userManager = null;
	private FlowService flowService = null;

	public FlowService getFlowService() {
		return flowService;
	}

	public void setFlowService(FlowService flowService) {
		this.flowService = flowService;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public ModelAndView runjob(HttpServletRequest req, HttpServletResponse res) {
		User user = userManager.getCurrentUser();
		System.out.println("USER IS ====> " + user);

		if (user == null) {
			user = userManager.getUserByUsername("admin");
		}
		long userId = user.getId();
		String email = user.getEmail();
		String token = userId + "-" + System.currentTimeMillis();
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		String _flowId = req.getParameter("flowId");
		long flowId = Long.parseLong(_flowId);
		System.out.println("JOB INFO IS: " + name + "  " + description + "  ");
		Job job = flowService.executeJob(token, name, description, flowId,
				userId, email);
		List<Job> jobs = flowService.getUserJobs(userId);
		return new ModelAndView("redirect:/getUserJobs.html");
		//redirect to avoid the the displaytag (sorting) or refresh to resend the submission for again.  
	}

	public ModelAndView jobdetail(HttpServletRequest req,
			HttpServletResponse res) {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("Show Job: " + job.getName());
		return new ModelAndView("job/jobdetail", Constants.JOB, job);
	}

	public ModelAndView jobaction(HttpServletRequest req,
			HttpServletResponse res) {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);

		Job job = flowService.getJob(jobId);
		String _submitType = req.getParameter("submit");

		if (_submitType.equals("Abort This Job")) {
			flowService.abortJob(job.getId());
		} else if (_submitType.equals("Delete This Job")) {
			flowService.deleteJob(job.getId());
		}

		System.out.println("HERE ACTION: " + req.getParameter(_submitType));
		return new ModelAndView("job/jobdetail", Constants.JOB, job);
	}

	public ModelAndView abortJob(HttpServletRequest req, HttpServletResponse res) {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);

		Job job = flowService.getJob(jobId);
		logger.debug("abort job:"+job.getId()+"("+job.getJobStatus()+","+job.getStatusCode()+")");
		if (job.isRunning()) flowService.abortJob(job.getId());
		return new ModelAndView("job/jobdetail", Constants.JOB, job);
	}

	public ModelAndView deleteJob(HttpServletRequest req,
			HttpServletResponse res, GetJobRequest jobRequest) {
		flowService.deleteJob(jobRequest.getJobId());
		return null;
	}

	public ModelAndView getJob(HttpServletRequest req, HttpServletResponse res,
			GetJobRequest jobRequest) {
		Job job = flowService.getJob(jobRequest.getJobId());
		return null;
	}

	public ModelAndView getUserJobs(HttpServletRequest req,
			HttpServletResponse res) {

		User user = userManager.getCurrentUser();
		logger.debug("USER IS ====> " + user);

		if (user == null) {
			user = userManager.getUserByUsername("admin");
		}
		long userId = user.getId();

		logger.debug("start to list the jobs of   " + user);
		List<Job> jobs = flowService.getUserJobs(userId);
		return new ModelAndView("job/jobList", Constants.JOBLIST, jobs);
	}

	public ModelAndView getUserNotifications(HttpServletRequest req,
			HttpServletResponse res) {
		User user = userManager.getCurrentUser();
		long userId = user.getId();
		List<Notification> notificationList = flowService
				.getUserNotifications(userId);
		return null;
	}

}

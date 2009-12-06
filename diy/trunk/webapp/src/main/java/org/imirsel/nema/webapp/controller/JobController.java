package org.imirsel.nema.webapp.controller;

import java.io.IOException;
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
import org.springframework.web.servlet.view.RedirectView;

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
	
	public ModelAndView hello(HttpServletRequest req,
			HttpServletResponse res) {
	
		res.setContentType("text/xml");
		try {
			res.getWriter().write("<hello/>");
			res.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
	}



	public ModelAndView jobdetail(HttpServletRequest req,
			HttpServletResponse res) {
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("Show Job here: " + job.getName());
		System.out.println("STATUS CODE: " + job.getStatusCode());
		System.out.println("STATUS VAL: " + job.getJobStatus());
		
		return new ModelAndView("job/job", Constants.JOB, job);
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
		//, Constants.JOB, job
		return new ModelAndView(new RedirectView("/JobManager.getUserJobs"));
	}



	public ModelAndView getuserjobs(HttpServletRequest req,
			HttpServletResponse res) {

		User user = userManager.getCurrentUser();
		logger.debug("USER IS ====> " + user);

		if (user == null) {
			user = userManager.getUserByUsername("admin");
		}
		long userId = user.getId();

		logger.debug("start to list the jobs of   " + user);
		List<Job> jobs = flowService.getUserJobs(userId);
	
		for(Job job:jobs){
			logger.debug(job.getId() +" " +job.getName() + " " + job.getJobStatus());
		}
		
		
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

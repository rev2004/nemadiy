package org.imirsel.nema.webapp.controller;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class JobController extends MultiActionController{

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
	
	
	public ModelAndView  runjob(HttpServletRequest req, HttpServletResponse res){
		User user=userManager.getCurrentUser();
		System.out.println("USER IS ====> "+ user);
		
		if(user==null){
			user=userManager.getUserByUsername("admin");
		}
		long userId = user.getId();
		String email = user.getEmail();
		String token = userId+ "-" + System.currentTimeMillis();
		String name = req.getParameter("name");
		String description = req.getParameter("description");
		String _flowId = req.getParameter("flowId");
		long flowId = Long.parseLong(_flowId);
		System.out.println("JOB INFO IS: " + name + "  " + description + "  " );
		Job job=flowService.executeJob(token, name,description, flowId,userId, email);
		List<Job> jobs=flowService.getUserJobs(userId);
		return new ModelAndView("job/jobList", Constants.JOBLIST, jobs); 
	}

	
	public ModelAndView  jobdetail(HttpServletRequest req, HttpServletResponse res){
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("Show Job: " + job.getName());
		return new ModelAndView("job/jobdetail", Constants.JOB, job);
	}
	
	
	public ModelAndView  jobaction(HttpServletRequest req, HttpServletResponse res){
		String _jobId = req.getParameter("id");
		long jobId = Long.parseLong(_jobId);
		Job job = flowService.getJob(jobId);
		System.out.println("HERE ACTION: " + req.getParameter("submit"));
		return new ModelAndView("job/jobdetail", Constants.JOB, job);
	}
	
	
	public ModelAndView  deleteJob(HttpServletRequest req, HttpServletResponse res, GetJobRequest jobRequest){
		flowService.deleteJob(jobRequest.getJobId());
		return null;
	}


	public ModelAndView  getJob(HttpServletRequest req, HttpServletResponse res,GetJobRequest jobRequest){
		Job job=flowService.getJob(jobRequest.getJobId());
		return null;
	}

	
	public ModelAndView getUserJobs(HttpServletRequest req, HttpServletResponse res){
		User user=userManager.getCurrentUser();
		long userId = user.getId();
		List<Job> jobList=flowService.getUserJobs(userId);
		return null;
	}
	

	public ModelAndView getUserNotifications(HttpServletRequest req, HttpServletResponse res){
		User user=userManager.getCurrentUser();
		long userId = user.getId();
		List<Notification> notificationList=flowService.getUserNotifications(userId);
		return null;
	}


}

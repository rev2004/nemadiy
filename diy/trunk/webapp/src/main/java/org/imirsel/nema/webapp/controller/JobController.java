package org.imirsel.nema.webapp.controller;

import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.Notification;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.request.ExecuteJobRequest;
import org.imirsel.nema.webapp.request.GetJobRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

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
	
	
	public ModelAndView  executeJob(HttpServletRequest req, HttpServletResponse res, ExecuteJobRequest jobRequest){
		User user=userManager.getCurrentUser();
		long userId = user.getId();
		String email = user.getEmail();
		Job job=flowService.executeJob(jobRequest.getToken(), jobRequest.getName(), jobRequest.getDescription(), jobRequest.getFlowInstanceId(),userId, email);
		return null;
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

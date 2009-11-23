package org.imirsel.nema.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.flowservice.FlowService;
import org.imirsel.nema.model.Job;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.request.ExecuteJobRequest;
import org.imirsel.nema.webapp.request.GetJobRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JobController extends MultiActionController{
	
	
   private UserManager mgr = null;
   private FlowService flowService = null;
   

   public FlowService getFlowService() {
	return flowService;
   }


   public void setFlowService(FlowService flowService) {
	   this.flowService = flowService;
   }


   public void setUserManager(UserManager userManager) {
	   this.mgr = userManager;
   }
	
	
	public ModelAndView  executeJob(HttpServletRequest req, HttpServletResponse res, ExecuteJobRequest jobRequest){
		User user=mgr.getCurrentUser();
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
		User user=mgr.getCurrentUser();
		long userId = user.getId();
		flowService.getUserJobs(userId);
		return null;
	}
	

	public ModelAndView getUserNotifications(HttpServletRequest req, HttpServletResponse res){
		User user=mgr.getCurrentUser();
		long userId = user.getId();
		flowService.getUserNotifications(userId);
		return null;
	}


}

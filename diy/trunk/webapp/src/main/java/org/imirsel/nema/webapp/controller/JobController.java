package org.imirsel.nema.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.imirsel.nema.service.UserManager;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class JobController extends MultiActionController{
	
	
   private UserManager mgr = null;

   public void setUserManager(UserManager userManager) {
	   this.mgr = userManager;
   }
	
	
	public ModelAndView  executeJob(HttpServletRequest req, HttpServletResponse res){
		return null;
	}

	
	public ModelAndView  deleteJob(HttpServletRequest req, HttpServletResponse res){
		return null;
	}


	public ModelAndView  getJob(HttpServletRequest req, HttpServletResponse res){
		return null;
	}

	
	public ModelAndView getUserJobs(HttpServletRequest req, HttpServletResponse res){
		return null;
	}
	

	public ModelAndView getUserNotifications(HttpServletRequest req, HttpServletResponse res){
		return null;
	}


}

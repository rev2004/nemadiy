/**
 * 
 */
package org.imirsel.nema.webapp.controller.mirex;

import java.util.List;
import java.util.Set;

import org.imirsel.nema.Constants;
import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.Profile;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controllers related to Mirex Submission
 * @author gzhu1
 *
 */
@Controller
@RequestMapping("/MirexManager")
public class MirexSubmissionController {

	
	MirexContributorDictionary mirexContributorDictionary;
	MirexSubmissionDao mirexSubmissionDao;
	UserManager userManager;
	
	@Autowired
	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}
	
	
	@Autowired
	public void setMirexSubmissionDao(MirexSubmissionDao mirexSubmissionDao) {
		this.mirexSubmissionDao = mirexSubmissionDao;
	}


	@Autowired
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	
	@RequestMapping(value="addContributor.frag", method=RequestMethod.GET)
	public ModelAndView setupForm(ModelMap model){
		Profile contributor=new Profile();
		contributor.setUrl("http://");
		//model.addAttribute("contributor",contributor);
		return new ModelAndView("mirex/contributorForm","contributor",contributor);
	}
	
	@RequestMapping(value="addContributor.frag",method=RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("contributor")Profile contributor){
		contributor=mirexContributorDictionary.add(contributor);
		return new ModelAndView("mirex/addContributorSuccess","contributor",contributor);
		
	}

	@RequestMapping(value="/contributorList.json",method=RequestMethod.GET)
	ModelAndView findContirubutor(@RequestParam("str") String str){
		ModelAndView mav=new ModelAndView("jsonView");
		List<Profile> list=mirexContributorDictionary.findSimilar(str);
		mav.addObject("contributorList", list);
		return mav;
	}
	
	@RequestMapping("/submissions")
	ModelAndView getSubmissions(){
		User user=userManager.getCurrentUser();
		List<MirexSubmission> submissions;
		if (isSuperUser(user)){
			submissions=mirexSubmissionDao.getAll();
		}else {
			submissions=mirexSubmissionDao.getSubmissions(user);
		}
		ModelAndView mav=new ModelAndView("/mirex/submissionList","submissions",submissions);
		return mav;
	}
	
	private boolean isSuperUser(User user){
		
		Set<Role>  roles=user.getRoles();
		for (Role role:roles){
			if ((Constants.MIREX_RUNNER_ROLE.equals(role.getName()))||
					(Constants.ADMIN_ROLE.equals(role.getName()))){
				return true;
			}
		}
		return false;
	}
}

/**
 * 
 */
package org.imirsel.nema.webapp.controller.annotation;

import java.util.List;
import java.util.Set;

import org.imirsel.nema.dao.MirexSubmissionDao;
import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.model.MirexSubmission;
import org.imirsel.nema.model.Role;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
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



	@RequestMapping(value="/contributorList.json",method=RequestMethod.GET)
	ModelAndView findContirubutor(@RequestParam("str") String str){
		ModelAndView mav=new ModelAndView("jsonView");
		List<Contributor> list=mirexContributorDictionary.findSimilar(str);
		mav.addObject("contributorList", list);
		return mav;
	}
	
	@RequestMapping("/submissions")
	ModelAndView getSubmissions(){
		User user=userManager.getCurrentUser();
		Set<Role>  roles=user.getRoles();
		if ()
		List<MirexSubmission> submissions=mirexSubmissionDao.getAll();
		ModelAndView mav=new ModelAndView("/mirex/submissions","submissions",submissions);
		return mav;
	}
}

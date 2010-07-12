/**
 * 
 */
package org.imirsel.nema.webapp.controller.annotation;

import java.util.List;

import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author gzhu1
 *
 */
@Controller
@RequestMapping("/MirexManager")
public class MirexSubmissionController {

	@Autowired
	MirexContributorDictionary mirexContributorDictionary;

	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}
	
	
	
	@RequestMapping("/findContributor.json")
	ModelAndView findContirubutor(@RequestParam("str") String str){
		ModelAndView mav=new ModelAndView("jsonView");
		List<Contributor> list=mirexContributorDictionary.findSimilar(str);
		mav.addObject("contributorList", list);
		return mav;
	}
}

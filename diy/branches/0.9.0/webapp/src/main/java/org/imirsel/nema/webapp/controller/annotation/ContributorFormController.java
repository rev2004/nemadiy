/**
 * 
 */
package org.imirsel.nema.webapp.controller.annotation;

import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author gzhu1
 *
 */
@Controller
@RequestMapping("/MirexManager.addContributor.frag")
@SessionAttributes("contributor")
public class ContributorFormController {

	MirexContributorDictionary mirexContributorDictionary;
	@Autowired
	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView setupForm(ModelMap model){
		Contributor contributor=new Contributor();
		contributor.setUrl("http://");
		//model.addAttribute("contributor",contributor);
		return new ModelAndView("mirex/contributorForm","contributor",contributor);
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("contributor")Contributor contributor){
		mirexContributorDictionary.add(contributor);
		return new ModelAndView("mirex/addContributorSuccess","contributor",contributor);
		
	}
}

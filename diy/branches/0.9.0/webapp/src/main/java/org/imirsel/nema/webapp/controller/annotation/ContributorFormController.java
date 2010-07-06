/**
 * 
 */
package org.imirsel.nema.webapp.controller.annotation;

import org.imirsel.nema.model.Contributor;
import org.imirsel.nema.webapp.service.MirexContributorDictionary;
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
@RequestMapping("/mirexManager.addContributor")
@SessionAttributes("contributor")
public class ContributorFormController {

	MirexContributorDictionary mirexContributorDictionary;
	public void setMirexContributorDictionary(
			MirexContributorDictionary mirexContributorDictionary) {
		this.mirexContributorDictionary = mirexContributorDictionary;
	}

	@RequestMapping(method=RequestMethod.GET)
	public String setupForm(ModelMap model){
		Contributor contributor=new Contributor();
		contributor.setUrl("http://");
		model.addAttribute("contributor",contributor);
		return "mirex/addContributor";
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("contributor")Contributor contributor){
		mirexContributorDictionary.add(contributor);
		return new ModelAndView("mirex/addContributorSuccess","contributor",contributor);
		
	}
}

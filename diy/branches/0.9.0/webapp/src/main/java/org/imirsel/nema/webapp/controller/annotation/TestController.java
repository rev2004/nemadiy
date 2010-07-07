package org.imirsel.nema.webapp.controller.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

	@RequestMapping("/MirexManager.test")
	public String test(){
		return ("mirex/test");
	}
}

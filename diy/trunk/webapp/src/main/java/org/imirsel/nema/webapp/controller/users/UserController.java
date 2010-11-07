package org.imirsel.nema.webapp.controller.users;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.model.User;
import org.imirsel.nema.service.UserManager;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;


public class UserController extends MultiActionController {
    private transient final Log log = LogFactory.getLog(UserController.class);
    private UserManager mgr = null;
    private PasswordEncoder passwordEncoder;


    public void setUserManager(UserManager userManager) {
        this.mgr = userManager;
    }

    
    public ModelAndView login(HttpServletRequest req,HttpServletResponse res) throws IOException{
    	
    	String username = req.getParameter("username");
        String password = req.getParameter("password");
        
        if(username==null || password==null){
        	res.sendError(HttpServletResponse.SC_EXPECTATION_FAILED,"Missing username or password");
        	return null;
        }
    	
        User user =this.mgr.getUserByUsername(username);
        
        if(user==null){
        	res.sendError(HttpServletResponse.SC_FORBIDDEN,"User: " + username + " does not exist.");
        	return null;
        }
        
        String encodedPassword=this.getPasswordEncoder().encodePassword(password, null);
        
        if(!encodedPassword.equals(user.getPassword())){
        	res.sendError(HttpServletResponse.SC_FORBIDDEN,"Invalid password for the user " + username);
        	return null;
        }
        
        // log user in automatically
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), user.getAuthorities());
        auth.setDetails(user);
        SecurityContextHolder.getContext().setAuthentication(auth);
       ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("success",true);
        return mav;
    }
    
    
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
    
    
}

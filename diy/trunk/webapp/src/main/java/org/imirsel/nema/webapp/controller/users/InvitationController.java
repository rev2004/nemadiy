/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.nema.webapp.controller.users;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import org.imirsel.nema.dao.ProfileDao;
import org.imirsel.nema.model.Profile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author gzhu1
 */
@Controller
@RequestMapping("/UserManager")
public class InvitationController {

    private ProfileDao profileDao;

    @RequestMapping("invitation")
    ModelAndView invitationHandler(HttpSession session, @RequestParam(required=false,value="code") String uuidStr) {

        Profile profile;
        try{
            UUID uuid=UUID.fromString(uuidStr);
            profile= profileDao.findByUuid(uuid);
        }catch(IllegalArgumentException ex){
            profile=null;
        }
        ModelAndView mav=new ModelAndView("user/invitation");
        if (profile != null) {
            session.setAttribute("profile", profile);
            mav.addObject("greeting",
                    "Hello "+profile.getFirstname()+" "
                    +profile.getLastname()
                    +". Thank you to accept our invitation. Please login with your OpenID first.");
        } else {
            mav.addObject("greeting",
                    "Sorry, we cannot find the record of your invitation. "
                    +"But you can still sign up for an account. "
                    +"Please login with your OpenID first.");
        }
        return mav;
    }

    /**
     * @param profileDao the profileDao to set
     */
    public void setProfileDao(ProfileDao profileDao) {
        this.profileDao = profileDao;
    }
}

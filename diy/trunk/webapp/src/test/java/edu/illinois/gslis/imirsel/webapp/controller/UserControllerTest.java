package edu.illinois.gslis.imirsel.webapp.controller;

import java.util.Map;

import org.imirsel.nema.Constants;
import org.springframework.web.servlet.ModelAndView;

public class UserControllerTest extends BaseControllerTestCase {

    public void testHandleRequest() throws Exception {
        UserController c = (UserController) applicationContext.getBean("userController");
        ModelAndView mav = c.handleRequest(null, null);
        Map m = mav.getModel();
        assertNotNull(m.get(Constants.USER_LIST));
        assertEquals("admin/userList", mav.getViewName());
    }
}

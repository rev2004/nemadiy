/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.imirsel.nema.webapp.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

/**
 *  Customized {@link AuthenticationFailureHandler} that redirect to sign-up page
 * if the OpenID authentication succeeds, but the user name is not yet in local DB of the container
 * @author gzhu1
 */
public class OpenIDAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    static private Log logger = LogFactory.getLog(OpenIDAuthenticationFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        logger.error(exception, exception);
        if (exception instanceof UsernameNotFoundException
                && exception.getAuthentication() instanceof OpenIDAuthenticationToken
                && ((OpenIDAuthenticationToken) exception.getAuthentication()).getStatus().equals(OpenIDAuthenticationStatus.SUCCESS)) {
            DefaultRedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
            request.getSession(true).setAttribute("USER_OPENID_CREDENTIAL", exception.getAuthentication().getPrincipal());
            // redirect to create account page

            logger.info("user (" + exception.getAuthentication().getPrincipal() + "," + exception.getExtraInformation() + ") is not found and redirect to signup.");
            redirectStrategy.sendRedirect(request, response, "/signup.html");

        } else {
            super.onAuthenticationFailure(request, response, exception);
        }
    }
}

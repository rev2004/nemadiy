/**
 * 
 */
package org.imirsel.nema.webapp.webflow.extention;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.imirsel.nema.webapp.webflow.TasksServiceImpl;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.mvc.builder.FlowResourceFlowViewResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

/**
 * 
 * An extension on  {@link FlowResourceFlowViewResolver} that try to resolve view with a list of MVC view,
 * and if no view can be found in the resolver list fall back to {@link FlowResourceFlowViewResolver} 
 * for jstl/jsp view where the jsp/jspx files can be in the same folder as flow definition.  
 * NOTE: The view resolvers in the list should return null if nothing found, so resolvers that returns a view always such as 
 * {@link org.springframework.web.servlet.view.InternalResourceViewResolver} should not be in the list. 
 * @author gzhu1
 *
 */
public class FlowResourceAndDelegatingFlowViewResolver extends
		FlowResourceFlowViewResolver {
	private List<ViewResolver> viewResolvers;

	static private Log logger = LogFactory.getLog(FlowResourceAndDelegatingFlowViewResolver.class);

	/**
	 * Creates a new flow view resolver.
	 * @param viewResolvers the Spring MVC view resolver chain to delegate to
	 */
	public void setViewResolvers(List<ViewResolver> viewResolvers) {
		this.viewResolvers = viewResolvers != null ? viewResolvers : new ArrayList<ViewResolver>();
	}

	public View resolveView(String viewId, RequestContext context) {
		logger.debug("start to resolve view: "+viewId);
		for (ViewResolver viewResolver: viewResolvers) {
			
			try {
				View view = viewResolver.resolveViewName(viewId, context.getExternalContext().getLocale());
				if (view != null) {
					return view;
				}
			} catch (Exception e) {
				
				IllegalStateException ise = new IllegalStateException("Exception resolving view with name '" + viewId
						+ "'");
				ise.initCause(e);
				logger.error(ise,ise);
				throw ise;
			}
		}
		return super.resolveView(viewId, context);
	}

	
}

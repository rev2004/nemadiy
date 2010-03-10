package org.imirsel.nema.diy.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

/**GWT Proxy Servlet to be used during development
 * for NEMA project
 * 
 * @author kumaramit01
 * @since 0.1.0
 *
 */
public final class HttpProxyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8273997595145942006L;
	private String host;
	private String username;
	private String password;
	private DefaultHttpMethodRetryHandler retryhandler;
	private AuthScope scope;
	private UsernamePasswordCredentials credentials;
	private HttpClient proxy;
  @Override
  public void init(final ServletConfig config) throws ServletException {
    super.init(config);
    host = config.getInitParameter("host");
    username = config.getInitParameter("username");
    password = config.getInitParameter("password");
    retryhandler = new DefaultHttpMethodRetryHandler(0,false);
    scope = new AuthScope(AuthScope.ANY);
    credentials = new UsernamePasswordCredentials(username, password);
    proxy = new HttpClient();
  }

  @Override
  protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
          throws ServletException, IOException {
	Map<String, String[]> requestParameters = request.getParameterMap();
	String call = request.getParameter("call");
	System.out.println("proxy call is: " + host+call);
	URL url;
	
	try {
	      url = new URL(host+call);
	    } catch (MalformedURLException me) {
	      throw new ServletException("Proxy URL is invalid", me);
	    }
	
	proxy.getHostConfiguration().setHost(url.getHost());
	proxy.getState().setCredentials(scope, credentials);
	proxy.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler); 
	
    StringBuilder query = new StringBuilder();
    for (String name : requestParameters.keySet()) {
    if(!name.equalsIgnoreCase("call")){
      for (String value : requestParameters.get(name)) {
        if (query.length() == 0) {
          query.append("?");
        } else {
          query.append("&");
        }

        name = URLEncoder.encode(name, "UTF-8");
        value = URLEncoder.encode(value, "UTF-8");

        query.append(String.format("%s=%s", name, value));
      }
    }
    }

    String uri = String.format("%s%s", url.toString(), query.toString());
    GetMethod proxyMethod = new GetMethod(uri);
    proxyMethod.setDoAuthentication(true);
    proxy.executeMethod(proxyMethod);
    write(proxyMethod.getResponseBodyAsStream(), response.getOutputStream());
    
  }
  
  @Override
  protected void doPost(final HttpServletRequest request, final HttpServletResponse response)
          throws ServletException, IOException {

    Map<String, String[]> requestParameters = request.getParameterMap();
	String call = request.getParameter("call");
	
	
	URL url;
	try {
	      url = new URL(host+call);
	    } catch (MalformedURLException me) {
	      throw new ServletException("Proxy URL is invalid", me);
	    }
	proxy.getHostConfiguration().setHost(url.getHost());
	proxy.getState().setCredentials(scope, credentials);
	proxy.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, retryhandler); 
	
    
    String uri = url.toString();
    PostMethod proxyMethod = new PostMethod(uri);
    for (String name : requestParameters.keySet()) {
    	if(!name.equalsIgnoreCase("call")){
    		for (String value : requestParameters.get(name)) {
    			proxyMethod.addParameter(name, value);
    		}
    	}
    }
    proxyMethod.setDoAuthentication(true);
    proxy.executeMethod(proxyMethod);
    write(proxyMethod.getResponseBodyAsStream(), response.getOutputStream());
  }


  private void write(final InputStream inputStream, final OutputStream outputStream) throws IOException {
    int b;
    while ((b = inputStream.read()) != -1) {
      outputStream.write(b);
    }
    outputStream.flush();
  }

  @Override
  public String getServletInfo() {
    return "Http Proxy Servlet";
  }
}

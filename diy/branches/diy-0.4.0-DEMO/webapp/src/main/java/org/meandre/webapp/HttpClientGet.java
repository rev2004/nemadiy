package org.meandre.webapp;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.*;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;

public class HttpClientGet {
  
  //private static String url = "http://www.apache.org/";
  
  public static String getResponse(String url) {
	  String response=null;
    // Create an instance of HttpClient.
    HttpClient client = new HttpClient();

    //Authenticate
    client.getState().setCredentials(
            new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
            new UsernamePasswordCredentials("admin", "admin"));

    // Create a method instance.
    GetMethod method = new GetMethod(url);
 //   method.setDoAuthentication( true );
    
    // Provide custom retry handler is necessary
    method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, 
    		new DefaultHttpMethodRetryHandler(3, false));

    try {
      // Execute the method.
      int statusCode = client.executeMethod(method);

      if (statusCode != HttpStatus.SC_OK) {
        System.err.println("Method failed: " + method.getStatusLine());
      }

      // Read the response body.
     // byte[] responseBody = method.getResponseBody();
      String responseBody = method.getResponseBodyAsString(); 
      // Deal with the response.
      // Use caution: ensure correct character encoding and is not binary data
      //System.out.println(new String(responseBody));
      //response =  new String(responseBody);
      response =  responseBody;
      
      return response;

    } catch (HttpException e) {
      System.err.println("Fatal protocol violation: " + e.getMessage());
      e.printStackTrace();
      return "Fatal protocol violation: " + e.getMessage();
    } catch (IOException e) {
      System.err.println("Fatal transport error: " + e.getMessage());
      e.printStackTrace();
      return "Fatal transport error: " + e.getMessage();
    } finally {
      // Release the connection.
      method.releaseConnection();
    }

  }
}

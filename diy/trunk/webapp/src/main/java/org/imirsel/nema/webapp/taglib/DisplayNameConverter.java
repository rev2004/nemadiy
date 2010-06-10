package org.imirsel.nema.webapp.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * A Tag class to convert a java variable name style string into a more display friendly string.
 * For example: "redDogHTML"->"Red Dog HTML"
 * @author gzhu1
 *
 */
public class DisplayNameConverter extends BodyTagSupport {
 
	private static final long serialVersionUID = 6192292620262900654L;

	public int doAfterBody() throws JspTagException{
    	 String key=getBodyContent().getString();
    	 assert !key.isEmpty():"key is empty";
         StringBuilder newkey=new StringBuilder(key.substring(0, 1).toUpperCase());
         for (int i=1;i<key.length();i++){
          boolean currentCharIsUpper = Character.isUpperCase(key.charAt(i));
          boolean previousCharIsNotUpper = !Character.isUpperCase(key.charAt(i-1));
          boolean previousCharIsNotSpace = key.charAt(i-1)!=' ';
        	 if (currentCharIsUpper && previousCharIsNotUpper && previousCharIsNotSpace){
        		 newkey.append(" ");
        	 }
        	 newkey.append(key.charAt(i));
         }
         try {
             getPreviousOut().print(newkey);
          } catch (IOException e) {
             throw new JspTagException("TransformTag: " +
                 e.getMessage());
          }

    	 return SKIP_BODY;
     }
}

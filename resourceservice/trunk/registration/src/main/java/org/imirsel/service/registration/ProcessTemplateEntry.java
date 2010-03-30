package org.imirsel.service.registration;

import net.jini.core.entry.Entry;
import net.jini.entry.AbstractEntry;
import net.jini.lookup.entry.ServiceType;

/**
 * 
 * @author kumaramit01
 *
 */
public class ProcessTemplateEntry implements Entry{

	/**Version of this class
	 * 
	 */
	private static final long serialVersionUID = 2184833010985122636L;
	
	private String templateName = null;
	
	public ProcessTemplateEntry(){}
	public ProcessTemplateEntry(String templateName){
		this.templateName = templateName;
	}
	public String getTemplateName() {
		return templateName;
	}
	
	public boolean equals(Object pte){
		if(pte instanceof ProcessTemplateEntry){
			ProcessTemplateEntry p1 = (ProcessTemplateEntry)pte;
			String templateName1=p1.getTemplateName();
			return templateName1.equalsIgnoreCase(templateName);
		}
		return false;
	}
	
	public String toString(){
		return this.templateName;
	}

}
